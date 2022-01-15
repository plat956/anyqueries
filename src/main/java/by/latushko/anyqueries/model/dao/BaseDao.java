package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.BaseEntity;

import java.sql.Connection;
import java.util.Optional;

public abstract class BaseDao<K, T extends BaseEntity> {
    protected static final String LIKE_MARKER = "%";
    protected Connection connection;

    public abstract Optional<T> findById(K id) throws DaoException;
    public abstract boolean create(T t) throws DaoException;
    public abstract Optional<T> update(T t) throws DaoException;
    public abstract boolean delete(K id) throws DaoException;

    void setConnection(Connection connection){
        this.connection = connection;
    }
}
