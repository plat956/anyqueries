package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {
    List<Category> findTop(int count) throws DaoException;
    List<Category> findAllOrderByNameAsc() throws DaoException;
    Optional<String> findNameById(Long id) throws DaoException;
    List<Category> findLimitedByNameLikeOrderByNameAsc(int offset, int limit, String namePattern) throws DaoException;
    boolean existsByName(String name) throws DaoException;
    boolean existsByNameAndIdNot(String name, Long id) throws DaoException;
    List<String> findNameByNameLikeOrderedAndLimited(String namePatter, int limit) throws DaoException;
}
