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
import java.util.ArrayList;
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
    private static final String SQL_FIND_LIMITED_BY_NAME_LIKE_ORDER_BY_NAME_ASC_QUERY = """
            SELECT id, name, color, count(id) OVER() AS total 
            FROM categories""";
    private static final String SQL_EXISTS_BY_NAME_QUERY = """
            SELECT 1 FROM categories
            WHERE name = ?""";
    private static final String SQL_EXISTS_BY_NAME_AND_ID_NOT_QUERY = """
            SELECT 1 FROM categories
            WHERE name = ? and id <> ?""";
    private static final String SQL_CREATE_QUERY = """
            INSERT INTO categories(name, color) 
            VALUES (?, ?)""";
    private static final String SQL_UPDATE_QUERY = """
            UPDATE categories 
            SET name = ?, color = ? 
            WHERE id = ?""";
    private static final String SQL_DELETE_QUERY = """
            DELETE FROM categories 
            WHERE id = ?""";
    private static final String SQL_FIND_NAME_BY_NAME_LIKE_ORDER_ASC_QUERY = """
            SELECT name 
            FROM categories 
            WHERE name like ? 
            ORDER BY name ASC 
            LIMIT ?""";
    private static final String SQL_NAME_LIKE_CLAUSE = " WHERE name like ? ";
    private static final String SQL_LIMITED_QUERY_END_CLAUSE = " ORDER BY name ASC LIMIT ?,?";

    private RowMapper mapper = new CategoryMapper();

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
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_QUERY)){
            statement.setString(1, category.getName());
            statement.setString(2, category.getColor());
            statement.setLong(3, category.getId());

            if(statement.executeUpdate() >= 0) {
                return Optional.of(category);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to update category by calling update(Category category) method", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_QUERY)){
            statement.setLong(1, id);
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete category by calling delete(Long id) method", e);
        }
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
    public List<Category> findByNameContainsOrderByNameAscLimitedTo(String namePattern, int offset, int limit) throws DaoException {
        StringBuilder query = new StringBuilder(SQL_FIND_LIMITED_BY_NAME_LIKE_ORDER_BY_NAME_ASC_QUERY);
        if(namePattern != null) {
            query.append(SQL_NAME_LIKE_CLAUSE);
        }
        query.append(SQL_LIMITED_QUERY_END_CLAUSE);

        try (PreparedStatement statement = connection.prepareStatement(query.toString())){
            int index = 0;
            if(namePattern != null) {
                statement.setString(++index, LIKE_MARKER + namePattern + LIKE_MARKER);
            }
            statement.setInt(++index, offset);
            statement.setInt(++index, limit);

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

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_NAME_AND_ID_NOT_QUERY)){
            statement.setString(1, name);
            statement.setLong(2, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed check if category exists by calling existsByNameAndIdNot(String name, Long id) method", e);
        }
    }

    @Override
    public List<String> findNameByNameContainsOrderByNameAscLimitedTo(String namePatter, int limit) throws DaoException {
        List<String> result = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_NAME_BY_NAME_LIKE_ORDER_ASC_QUERY)){
            statement.setString(1, LIKE_MARKER + namePatter + LIKE_MARKER);
            statement.setInt(2, limit);
            try(ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    result.add(resultSet.getString(CATEGORY_NAME));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find categories names by calling findNameByNameLikeOrderedAndLimited method", e);
        }
        return result;
    }
}
