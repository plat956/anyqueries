package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.CategoryDao;
import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.model.mapper.RowMapper;
import by.latushko.anyqueries.model.mapper.impl.CategoryMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class CategoryDaoImpl extends BaseDao<Long, Category> implements CategoryDao {
    private static final String SQL_FIND_BY_ID_QUERY = """
            SELECT id, name, color 
            FROM categories 
            WHERE id = ?""";
    private static final String SQL_FIND_TOP_QUERY = """
            SELECT c.id, c.name, c.color, count(q.id) as questions_count 
            FROM categories c
            INNER JOIN questions q
            ON c.id = q.category_id
            GROUP BY c.id 
            ORDER BY questions_count DESC LIMIT ?""";
    private static final String SQL_FIND_ALL_ORDER_BY_NAME_QUERY = """
            SELECT id, name, color 
            FROM categories
            ORDER BY name ASC""";

    private RowMapper mapper = new CategoryMapper();

    @Override
    public List<Category> findAll() throws DaoException {
        return null;
    }

    @Override
    public Optional<Category> findById(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID_QUERY)){
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapper.mapRow(resultSet);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find category by calling findById(Long id) method", e);
        }
    }

    @Override
    public boolean create(Category category) throws DaoException {
        return false;
    }

    @Override
    public Optional<Category> update(Category category) throws DaoException {
        return Optional.empty();
    }

    @Override
    public boolean delete(Category category) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        return false;
    }

    @Override
    public List<Category> findTop5Categories() throws DaoException {
        List<Category> categories;
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_TOP_QUERY)){
            statement.setInt(1, 5);
            try(ResultSet resultSet = statement.executeQuery()) {
                categories = mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find top 5 categories by calling findTop5Categories() method", e);
        }
        return categories;
    }

    @Override
    public List<Category> findAllOrderByNameAsc() throws DaoException {
        List<Category> categories;
        try (Statement statement = connection.createStatement()){
            try(ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_ORDER_BY_NAME_QUERY)) {
                categories = mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all categories by calling findAllOrderByNameAsc() method", e);
        }
        return categories;
    }
}
