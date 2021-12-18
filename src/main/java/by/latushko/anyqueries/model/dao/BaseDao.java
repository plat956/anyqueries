package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.model.entity.BaseEntity;
import by.latushko.anyqueries.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface BaseDao<K, T extends BaseEntity> {
    List<T> findAll() throws DaoException;
    Optional<T> findById(K id) throws DaoException;
    boolean create(T t) throws DaoException;
    Optional<T> update(T t) throws DaoException;
    boolean delete(T t) throws DaoException;
    boolean delete(K id) throws DaoException;
}
