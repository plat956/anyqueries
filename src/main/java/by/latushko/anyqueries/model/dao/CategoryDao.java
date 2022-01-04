package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> findTop(int count) throws DaoException;
    List<Category> findAllOrderByNameAsc() throws DaoException;
}
