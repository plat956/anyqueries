package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.ConnectionPoolException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class EntityTransaction implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger();
    private Connection connection;
    private BaseDao[] daoStorage;

    public void begin(BaseDao... daos) throws EntityTransactionException {
        if(daos == null || daos.length == 0) {
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
        for (BaseDao dao : daos) {
            dao.setConnection(connection);
        }
        daoStorage = daos;
    }

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
        for(BaseDao dao: daoStorage) {
            dao.setConnection(null);
        }
    }

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
