package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * The Category Data Access Object interface.
 * Describes signatures of this DAO implementation methods
 * Contains abstract methods to create extended CRUD operations for Category entity
 */
public interface CategoryDao {
    /**
     * Find top.
     *
     * @param count the count of top categories to find
     * @return the list of found categories
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<Category> findTop(int count) throws DaoException;

    /**
     * Find all order by name asc.
     *
     * @return the list of found categories
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<Category> findAllOrderByNameAsc() throws DaoException;

    /**
     * Find name by id.
     *
     * @param id the category id
     * @return the optional of found categories names
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Optional<String> findNameById(Long id) throws DaoException;

    /**
     * Find by name contains order by name asc limited to.
     *
     * @param namePattern the category name pattern
     * @param offset      the offset
     * @param limit       the limit
     * @return the list of found categories
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<Category> findByNameContainsOrderByNameAscLimitedTo(String namePattern, int offset, int limit) throws DaoException;

    /**
     * Find name by name contains order by name asc limited to.
     *
     * @param namePattern the category name pattern
     * @param limit      the limit
     * @return the list of found category names
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<String> findNameByNameContainsOrderByNameAscLimitedTo(String namePattern, int limit) throws DaoException;

    /**
     * Exists by name.
     *
     * @param name the category name
     * @return the boolean, true if the category exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByName(String name) throws DaoException;

    /**
     * Exists by name and id not.
     *
     * @param name the category name
     * @param id   the category id
     * @return the boolean, true if the category exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByNameAndIdNot(String name, Long id) throws DaoException;
}
