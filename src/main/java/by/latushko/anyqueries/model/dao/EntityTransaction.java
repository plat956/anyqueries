package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.ConnectionPoolException;
import by.latushko.anyqueries.model.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class EntityTransaction implements AutoCloseable {
    private Connection connection;
    private BaseDao[] daoStorage;

    public void begin(BaseDao... daos) {
        if (connection == null) {
            connection = ConnectionPool.getInstance().getConnection();
        }

        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {

        }

        daoStorage = daos;
        for (BaseDao dao : daos) {
            dao.setConnection(connection);
        }
    }

    @Override
    public void close() {
        //check of null connections
        try {
            //check connection for commit
            connection.setAutoCommit(true);
        } catch (SQLException e) {

        }
        try {
            ConnectionPool.getInstance().releaseConnection(connection);
        } catch (ConnectionPoolException e) {
            e.printStackTrace();
        }
        connection = null;
        for(BaseDao dao: daoStorage) {
            dao.setConnection(null);
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
