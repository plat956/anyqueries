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

import static by.latushko.anyqueries.model.mapper.TableColumnName.CATEGORY_NAME;

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
    private static final String SQL_FIND_NAME_BY_ID_QUERY = """
            SELECT name  
            FROM categories
            WHERE id = ?""";
    private static final String SQL_FIND_ALL_LIMITED_ORDER_BY_NAME_ASC_QUERY = """
            SELECT id, name, color, count(id) OVER() AS total 
            FROM categories
            ORDER BY name ASC 
            LIMIT ?,?""";
    private static final String SQL_EXISTS_BY_NAME_QUERY = """
            SELECT 1 FROM categories
            WHERE name = ?""";
    private static final String SQL_CREATE_QUERY = """
            INSERT INTO categories(name, color) 
            VALUES (?, ?)""";

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
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setString(1, category.getName());
            statement.setString(2, category.getColor());

            if(statement.executeUpdate() >= 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    Long generatedId = resultSet.getLong(1);
                    category.setId(generatedId);
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to create category by calling create(Category category) method", e);
        }
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
    public List<Category> findTop(int count) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_TOP_QUERY)){
            statement.setInt(1, count);
            try(ResultSet resultSet = statement.executeQuery()) {
                return mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find top 5 categories by calling findTop5Categories() method", e);
        }
    }

    @Override
    public List<Category> findAllOrderByNameAsc() throws DaoException {
        try (Statement statement = connection.createStatement()){
            try(ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_ORDER_BY_NAME_QUERY)) {
                return mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all categories by calling findAllOrderByNameAsc() method", e);
        }
    }

    @Override
    public Optional<String> findNameById(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_NAME_BY_ID_QUERY)){
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return Optional.ofNullable(resultSet.getString(CATEGORY_NAME));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find category name by calling findNameById(Long id) method", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Category> findLimitedByOrderByNameAsc(int offset, int limit) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_LIMITED_ORDER_BY_NAME_ASC_QUERY)){
            statement.setInt(1, offset);
            statement.setInt(2, limit);

            try(ResultSet resultSet = statement.executeQuery()) {
                return mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find categories by calling findLimitedByOrderByNameAsc(int offset, int limit) method", e);
        }
    }

    @Override
    public boolean existsByName(String name) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_NAME_QUERY)){
            statement.setString(1, name);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed check if category exists by calling existsByName(String name) method", e);
        }
    }
}
