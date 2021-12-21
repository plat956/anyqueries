package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.UserDao;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.model.mapper.RowMapper;
import by.latushko.anyqueries.model.mapper.impl.UserMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl extends BaseDao<Long, User> implements UserDao {
    private static final String SQL_FIND_ALL_QUERY = """
            SELECT id, first_name, last_name, middle_name, login, password, email, telegram, avatar, last_login_date, status, role 
            FROM users""";
    private static final String SQL_FIND_BY_ID_QUERY = """
            SELECT id, first_name, last_name, middle_name, login, password, email, telegram, avatar, last_login_date, status, role 
            FROM users 
            WHERE id = ?""";
    private static final String SQL_CREATE_USER_QUERY = """
            INSERT INTO users(first_name, last_name, middle_name, login, password, email, telegram, avatar, last_login_date, status, role) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""";
    private static final String SQL_CREATE_HASH_QUERY = """
            INSERT INTO user_hash(hash, expires, user_id) 
            VALUES (?, ?, ?)""";
    private static final String SQL_UPDATE_QUERY = """
            UPDATE users 
            SET first_name = ?, last_name = ?, middle_name = ?, login = ?, password = ?, email = ?, telegram = ?, avatar = ?, last_login_date = ?, status = ?, role = ?  
            WHERE id = ?""";
    private static final String SQL_DELETE_QUERY = """
            DELETE FROM users WHERE id = ?""";

    private final RowMapper<User> mapper = new UserMapper();

        @Override
    public boolean create(User user) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getMiddleName());
            statement.setString(4, user.getLogin());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getEmail());
            statement.setString(7, user.getTelegram());
            statement.setString(8, user.getAvatar());
            statement.setObject(9, user.getLastLoginDate());
            statement.setString(10, user.getStatus().name());
            statement.setString(11, user.getRole().name());

            if(statement.executeUpdate() > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    Long generatedId = resultSet.getLong(1);
                    user.setId(generatedId);
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to create user by calling create(User user) method", e);
        }
        return false;
    }

    @Override
    public boolean createUserHash(UserHash hash) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_HASH_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setString(1, hash.getHash());
            statement.setObject(2, hash.getExpires());
            statement.setLong(3, hash.getUser().getId());

            if(statement.executeUpdate() > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    Long generatedId = resultSet.getLong(1);
                    hash.setId(generatedId);
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to create user by calling create(User user) method", e);
        }
        return false;
    }

    @Override
    public List<User> findAll() throws DaoException {
        List<User> users;
        try (Statement statement = connection.createStatement()){
            try(ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_QUERY)) {
                users = mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find all users by calling findAll() method", e);
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) throws DaoException {
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
            throw new DaoException("Failed to find user by calling findById(Long id) method", e);
        }
    }

    @Override
    public Optional<User> update(User user) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_QUERY)){
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getMiddleName());
            statement.setString(4, user.getLogin());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getEmail());
            statement.setString(7, user.getTelegram());
            statement.setString(8, user.getAvatar());
            statement.setObject(9, user.getLastLoginDate()); //todo проверить
            statement.setString(10, user.getStatus().name());
            statement.setString(11, user.getRole().name());
            statement.setLong(12, user.getId());

            if(statement.executeUpdate() > 0) {
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to update user by calling update(User user) method", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(User user) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_QUERY)){
            statement.setLong(1, user.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete user by calling delete(User user) method", e);
        }
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_QUERY)){
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete user by calling delete(Long id) method", e);
        }
    }
}
