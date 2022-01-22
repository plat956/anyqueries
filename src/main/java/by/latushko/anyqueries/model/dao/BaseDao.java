package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.BaseEntity;

import java.sql.Connection;
import java.util.Optional;

/**
 * The Base Data Access Object abstract type.
 *
 * @param <K> the type of entity identifier
 * @param <T> the entity class extended from BaseEntity
 */
public abstract class BaseDao<K, T extends BaseEntity> {
    /**
     * The constant LIKE_MARKER to use in database queries for SQL like operator
     */
    protected static final String LIKE_MARKER = "%";

    /**
     * The Connection object which is used to execute database queries.
     */
    protected Connection connection;

    /**
     * Find entity record by id.
     *
     * @param id the entity id
     * @return the optional with found object or empty one
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    public abstract Optional<T> findById(K id) throws DaoException;

    /**
     * Create entity record.
     *
     * @param t the new entity object
     * @return the boolean, true if the entity was created successfully, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    public abstract boolean create(T t) throws DaoException;

    /**
     * Update entity record.
     *
     * @param t the existing entity object whose internal state was updated
     * @return the optional with updated entity object if update was done successfully or empty one
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    public abstract Optional<T> update(T t) throws DaoException;

    /**
     * Delete entity record.
     *
     * @param id the entity id
     * @return the boolean, true if deletion was completed, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    public abstract boolean delete(K id) throws DaoException;

    /**
     * Set connection method.
     *
     * @param connection the connection object
     */
    void setConnection(Connection connection){
        this.connection = connection;
    }
}
