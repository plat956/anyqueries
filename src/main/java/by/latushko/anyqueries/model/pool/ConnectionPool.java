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

/**
 * The Connection pool class.
 * It stores and manages all database connections available to use by the app
 */
public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger();
    private static final AtomicBoolean CREATOR = new AtomicBoolean(false);
    private static final ReentrantLock LOCKER = new ReentrantLock();
    /**
     * The connection pool instance
     */
    private static ConnectionPool instance;
    /**
     * The internal queue stores connections available for use
     */
    private final BlockingQueue<ProxyConnection> freeConnections;
    /**
     * The internal queue stores used connections
     */
    private final BlockingQueue<ProxyConnection> givenAwayConnections;

    /**
     * The connection pool initialization
     * Creates essential quantity of free connections and fills them the internal queue
     */
    private ConnectionPool() {
        freeConnections = new LinkedBlockingDeque<>(DB_POOL_SIZE);
        givenAwayConnections = new LinkedBlockingDeque<>(DB_POOL_SIZE);

        try {
            for (int i = 0; i < DB_POOL_SIZE; i++) {
                ProxyConnection connection = ConnectionFactory.getConnection();
                freeConnections.put(connection);
            }
        } catch (SQLException e) {
            logger.fatal("Failed to initialize connection pool", e);
            throw new ExceptionInInitializerError("Failed to initialize connection pool");
        } catch (InterruptedException e) {
            logger.fatal("Failed to initialize connection pool", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Get instance of the connection pool.
     *
     * @return the connection pool instance
     */
    public static ConnectionPool getInstance() {
        if (!CREATOR.get()) {
            try {
                LOCKER.lock();
                if (instance == null) {
                    instance = new ConnectionPool();
                    CREATOR.set(true);
                }
            } finally {
                LOCKER.unlock();
            }
        }
        return instance;
    }

    /**
     * Gives one free connection from the pool.
     *
     * @return a free connection
     */
    public Connection getConnection() {
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            givenAwayConnections.put(connection);
        } catch (InterruptedException e) {
            logger.error("Failed to get free connection", e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    /**
     * Release the used connection.
     *
     * @param connection the used connection
     * @throws ConnectionPoolException if passed a wrong connection
     */
    public void releaseConnection(Connection connection) throws ConnectionPoolException {
        if (connection == null) {
            throw new ConnectionPoolException("Filed to release connection. Connection can't be null");
        } else if (connection.getClass() != ProxyConnection.class) {
            throw new ConnectionPoolException("Connection hasn't been released because of the instance of a wrong class");
        }
        ProxyConnection c = (ProxyConnection) connection;
        givenAwayConnections.remove(c);
        try {
            freeConnections.put(c);
        } catch (InterruptedException e) {
            logger.error("Failed to release connection", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Destroy the connection pool.
     */
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

    /**
     * Deregister database drivers.
     */
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
