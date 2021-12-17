package by.latushko.anyqueries.pool;

import by.latushko.anyqueries.exception.ConnectionPoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger();
    private static final String DB_PROPERTIES_PATH = "config/database.properties";
    private static final String DB_DRIVER;
    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;
    private static final int DB_POOL_SIZE;

    private static ConnectionPool instance;
    private static AtomicBoolean creator = new AtomicBoolean(false);
    private static ReentrantLock lockerSingleton = new ReentrantLock();

    private BlockingQueue<ProxyConnection> freeConnections;
    private BlockingQueue<ProxyConnection> givenAwayConnections;

    static {
        Properties properties = new Properties();
        try {
            InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_PATH);
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to read connection pool properties from file: " + DB_PROPERTIES_PATH, e);
            throw new ExceptionInInitializerError("Failed to read connection pool properties from file: " + DB_PROPERTIES_PATH);
        }

        DB_DRIVER = properties.getProperty("driver");
        DB_URL = properties.getProperty("url");
        DB_USER = properties.getProperty("username");
        DB_PASSWORD = properties.getProperty("password");
        DB_POOL_SIZE = Integer.parseInt(properties.getProperty("pool-size"));
    }

    private ConnectionPool() {
        freeConnections = new LinkedBlockingDeque<>(DB_POOL_SIZE);
        givenAwayConnections = new LinkedBlockingDeque<>(DB_POOL_SIZE);

        try {
            Class.forName(DB_DRIVER);
            for (int i = 0; i < DB_POOL_SIZE; i++) {
                ProxyConnection connection = new ProxyConnection(DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD));
                freeConnections.offer(connection); //todo: offer или put всетаки?
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Failed to initialize connection pool", e);
            throw new ExceptionInInitializerError("Failed to initialize connection pool");
        }
    }

    //todo: CountDownLatch?
    public static ConnectionPool getInstance(){
        if(!creator.get()){
            try{
                lockerSingleton.lock();
                if(instance == null){
                    instance = new ConnectionPool();
                    creator.set(true);
                }
            } finally {
                lockerSingleton.unlock();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            givenAwayConnections.offer(connection); //todo: offer или put всетаки?
        } catch (InterruptedException e) {
            //todo: write err log
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public void releaseConnection(Connection connection) throws ConnectionPoolException {
        if(connection.getClass() != ProxyConnection.class) {
            throw new ConnectionPoolException("Connection hasn't been released because of the instance of a wrong class");
        }
        ProxyConnection c = (ProxyConnection) connection;
        givenAwayConnections.remove(c); //?
        freeConnections.offer(c); //todo: offer или put всетаки?
    }

    public void destroyPool() {
        for (int i = 0; i < DB_POOL_SIZE; i++) {
            try {
                freeConnections.take().reallyClose();
            } catch (SQLException e) {
                //todo: write err log
            } catch (InterruptedException e) {
                //todo: write err log
                Thread.currentThread().interrupt();
            }
        }
        deregisterDrivers();
    }

    private void deregisterDrivers() {
        DriverManager.getDrivers().asIterator().forEachRemaining(d -> {
            try {
                DriverManager.deregisterDriver(d);
            } catch (SQLException e) {
                //todo: write err log
            }
        });
    }
}
