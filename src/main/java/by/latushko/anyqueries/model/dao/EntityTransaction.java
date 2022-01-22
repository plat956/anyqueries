package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.ConnectionPoolException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * The Entity transaction class.
 * This class serves database transaction from its start to end.
 */
public class EntityTransaction implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger();

    /**
     * The Connection object which is used to execute database queries.
     */
    private Connection connection;

    /**
     * The storage of DAO instances which take part in the transaction
     */
    private final BaseDao[] dao;

    /**
     * Instantiates a new Entity transaction.
     * Takes a free connection from pool, disables auto commit and share given connection to each DAO instance
     *
     * @param dao the array of DAO for current transaction
     * @throws EntityTransactionException if something goes wrong during a new transaction beginning
     */
    public EntityTransaction(BaseDao... dao) throws EntityTransactionException {
        if(dao == null || dao.length == 0) {
            logger.error("Unable to begin transaction with no given dao");
            throw new EntityTransactionException("Unable to begin transaction with no given dao");
        }
        if (connection == null) {
            connection = ConnectionPool.getInstance().getConnection();
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error("Unable to begin transaction because of impossible to disable connection autocommit", e);
            throw new EntityTransactionException("Unable to begin transaction because of impossible to disable connection autocommit", e);
        }
        Arrays.stream(dao).forEach(d -> d.setConnection(connection));
        this.dao = dao;
    }

    /***
     * Returns current connection to the pool, revokes it from DAO and as a result closes current transaction
     * @throws EntityTransactionException if something goes wrong during the transaction closing
     */
    @Override
    public void close() throws EntityTransactionException {
        if(connection == null) {
            logger.error("Unable to end transaction correctly because of transactional connection is empty");
            throw new EntityTransactionException("Unable to end transaction correctly because of transactional connection is empty");
        }
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("Unable to end transaction correctly because of impossible to revert connection autocommit state", e);
            throw new EntityTransactionException("Unable to begin transaction because of impossible to disable connection autocommit", e);
        }
        try {
            ConnectionPool.getInstance().releaseConnection(connection);
        } catch (ConnectionPoolException e) {
            logger.error("Unable to end transaction correctly because of impossible to release used connection", e);
            throw new EntityTransactionException("Unable to end transaction correctly because of impossible to release used connection", e);
        }
        connection = null;
        Arrays.stream(this.dao).forEach(d -> d.setConnection(null));
    }

    /**
     * Commit current transaction.
     *
     * @throws EntityTransactionException if something goes wrong during the transaction committing
     */
    public void commit() throws EntityTransactionException {
        if(connection == null) {
            logger.error("Unable to commit transaction because of transactional connection is empty");
            throw new EntityTransactionException("Unable to commit transaction because of transactional connection is empty");
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            logger.error("Unable to commit transaction", e);
            throw new EntityTransactionException("Unable to commit transaction", e);
        }
    }

    /**
     * Rollback current transaction.
     *
     * @throws EntityTransactionException if something goes wrong during rollback the transaction
     */
    public void rollback() throws EntityTransactionException {
        if(connection == null) {
            logger.error("Unable to rollback transaction because of transactional connection is empty");
            throw new EntityTransactionException("Unable to rollback transaction because of transactional connection is empty");
        }
        try {
            connection.rollback();
        } catch (SQLException e) {
            logger.error("Unable to rollback transaction", e);
            throw new EntityTransactionException("Unable to rollback transaction", e);
        }
    }
}
