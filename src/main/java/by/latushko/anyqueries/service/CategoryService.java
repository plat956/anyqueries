package by.latushko.anyqueries.service;

import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;

import java.util.List;
import java.util.Optional;

/**
 * The Category service interface.
 */
public interface CategoryService {
    /**
     * Find top 5 categories.
     *
     * @return the list of categories
     */
    List<Category> findTop5();

    /**
     * Find all order by name asc.
     *
     * @return the list of categories
     */
    List<Category> findAllOrderByNameAsc();

    /**
     * Find name by id.
     *
     * @param id the category id
     * @return the optional with category name or empty one
     */
    Optional<String> findNameById(Long id);

    /**
     * Find paginated by name contains order by name asc.
     *
     * @param page        the page
     * @param namePattern the category name pattern
     * @return the paginated category objects
     */
    Paginated<Category> findPaginatedByNameContainsOrderByNameAsc(RequestPage page, String namePattern);

    /**
     * Find name by name contains order by name asc limited to.
     *
     * @param namePattern the category name pattern
     * @param limit       the limit
     * @return the list of category names
     */
    List<String> findNameByNameContainsOrderByNameAscLimitedTo(String namePattern, int limit);

    /**
     * Find by id.
     *
     * @param id the category id
     * @return the optional with category or empty one
     */
    Optional<Category> findById(Long id);

    /**
     * Exists by name.
     *
     * @param name the category name
     * @return the boolean, true if the category exists, otherwise false
     */
    boolean existsByName(String name);

    /**
     * Exists by name and id not.
     *
     * @param name the category name
     * @param id   the category id
     * @return the boolean, true if the category exists, otherwise false
     */
    boolean existsByNameAndIdNot(String name, Long id);

    /**
     * Create category.
     *
     * @param name  the category name
     * @param color the category color
     * @return the optional with created category or empty one
     */
    Optional<Category> create(String name, String color);

    /**
     * Update category.
     *
     * @param id    the category id
     * @param name  the category name
     * @param color the category color
     * @return the boolean, true if the category was updated successfully, otherwise false
     */
    boolean update(Long id, String name, String color);

    /**
     * Delete category.
     *
     * @param id the category id
     * @return the boolean, true if the category was deleted successfully, otherwise false
     */
    boolean delete(Long id);
}
