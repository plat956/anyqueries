package by.latushko.anyqueries.model.pool;

import by.latushko.anyqueries.exception.ConnectionPoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import static by.latushko.anyqueries.model.pool.ConnectionFactory.DB_POOL_SIZE;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger();
    private static ConnectionPool instance;
    private static AtomicBoolean creator = new AtomicBoolean(false);
    private static ReentrantLock locker = new ReentrantLock();
    private BlockingQueue<ProxyConnection> freeConnections;
    private BlockingQueue<ProxyConnection> givenAwayConnections;

    private ConnectionPool() {
        freeConnections = new LinkedBlockingDeque<>(DB_POOL_SIZE);
        givenAwayConnections = new LinkedBlockingDeque<>(DB_POOL_SIZE);

        try {
            for (int i = 0; i < DB_POOL_SIZE; i++) {
                ProxyConnection connection = ConnectionFactory.getConnection();
                freeConnections.put(connection);
            }
        } catch (SQLException e) {
            logger.error("Failed to initialize connection pool", e);
            throw new ExceptionInInitializerError("Failed to initialize connection pool");
        } catch (InterruptedException e) {
            logger.error("Failed to initialize connection pool", e);
            Thread.currentThread().interrupt();
        }
    }

    public static ConnectionPool getInstance(){
        if(!creator.get()){
            try{
                locker.lock();
                if(instance == null){
                    instance = new ConnectionPool();
                    creator.set(true);
                }
            } finally {
                locker.unlock();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            givenAwayConnections.put(connection); //todo: возвращать обратно во freeConnections???
        } catch (InterruptedException e) {
            logger.error("Failed to get free connection", e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public void releaseConnection(Connection connection) throws ConnectionPoolException {
        if(connection == null) {
            throw new ConnectionPoolException("Filed to release connection. Connection can't be null");
        } else if(connection.getClass() != ProxyConnection.class) {
            throw new ConnectionPoolException("Connection hasn't been released because of the instance of a wrong class");
        }
        ProxyConnection c = (ProxyConnection) connection;
        givenAwayConnections.remove(c);
        try {
            freeConnections.put(c);
        } catch (InterruptedException e) {
            //todo: возвращать обратно в givenAwayConnections???
            logger.error("Failed to release connection", e);
            Thread.currentThread().interrupt();
        }
    }

    public void destroyPool() {
        for (int i = 0; i < DB_POOL_SIZE; i++) {
            try {
                freeConnections.take().reallyClose();
            } catch (SQLException e) {
                logger.error("Failed to destroy connection pool", e);
            } catch (InterruptedException e) {
                logger.error("Failed to destroy connection pool", e);
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
                logger.error("Failed to deregister drivers", e);
            }
        });
    }
}
