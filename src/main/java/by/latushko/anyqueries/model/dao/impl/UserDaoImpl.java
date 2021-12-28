package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.UserDao;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.model.mapper.RowMapper;
import by.latushko.anyqueries.model.mapper.impl.UserMapper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl extends BaseDao<Long, User> implements UserDao {
    private static final String SQL_FIND_ALL_QUERY = """
            SELECT id, first_name, last_name, middle_name, login, password, email, telegram, avatar, credential_key, last_login_date, status, role 
            FROM users""";
    private static final String SQL_FIND_BY_ID_QUERY = """
            SELECT id, first_name, last_name, middle_name, login, password, email, telegram, avatar, credential_key, last_login_date, status, role 
            FROM users 
            WHERE id = ?""";
    private static final String SQL_FIND_INACTIVE_BY_HASH_AND_DATE_QUERY = """
            SELECT u.id, u.first_name, u.last_name, u.middle_name, u.login, u.password, u.email, u.telegram, u.avatar, u.credential_key, u.last_login_date, u.status, u.role 
            FROM users u 
            INNER JOIN user_hash uh 
            ON u.id = uh.user_id 
            WHERE u.status = ? and uh.hash = ? and uh.expires >= ?""";
    private static final String SQL_FIND_INACTIVE_BY_TELEGRAM_QUERY = """
            SELECT id, first_name, last_name, middle_name, login, password, email, telegram, avatar, credential_key, last_login_date, status, role 
            FROM users 
            WHERE status = ? and telegram = ?""";
    private static final String SQL_FIND_BY_LOGIN_QUERY = """
            SELECT id, first_name, last_name, middle_name, login, password, email, telegram, avatar, credential_key, last_login_date, status, role 
            FROM users 
            WHERE login = ?""";
    private static final String SQL_FIND_BY_CREDENTIAL_KEY_QUERY = """
            SELECT id, first_name, last_name, middle_name, login, password, email, telegram, avatar, credential_key, last_login_date, status, role 
            FROM users 
            WHERE credential_key = ?""";
    private static final String SQL_CREATE_USER_QUERY = """
            INSERT INTO users(first_name, last_name, middle_name, login, password, email, telegram, avatar, credential_key, last_login_date, status, role) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""";
    private static final String SQL_CREATE_HASH_QUERY = """
            INSERT INTO user_hash(hash, expires, user_id) 
            VALUES (?, ?, ?)""";
    private static final String SQL_UPDATE_QUERY = """
            UPDATE users 
            SET first_name = ?, last_name = ?, middle_name = ?, login = ?, password = ?, email = ?, telegram = ?, avatar = ?, credential_key = ?, last_login_date = ?, status = ?, role = ?  
            WHERE id = ?""";
    private static final String SQL_DELETE_USER_QUERY = """
            DELETE FROM users 
            WHERE id = ?""";
    private static final String SQL_DELETE_HASH_QUERY = """
            DELETE FROM user_hash 
            WHERE user_id = ?""";
    private static final String SQL_EXISTS_BY_LOGIN_QUERY = """
            SELECT 1 FROM users
            WHERE login = ?""";
    private static final String SQL_EXISTS_BY_EMAIL_QUERY = """
            SELECT 1 FROM users
            WHERE email = ?""";
    private static final String SQL_EXISTS_BY_TELEGRAM_QUERY = """
            SELECT 1 FROM users
            WHERE telegram = ?""";
    private static final String SQL_EXISTS_BY_EMAIL_EXCEPT_USER_ID_QUERY = """
            SELECT 1 FROM users
            WHERE email = ? and id <> ?""";
    private static final String SQL_EXISTS_BY_TELEGRAM_EXCEPT_USER_ID_QUERY = """
            SELECT 1 FROM users
            WHERE telegram = ? and id <> ?""";
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
            statement.setString(9, user.getCredentialKey());
            statement.setObject(10, user.getLastLoginDate());
            statement.setString(11, user.getStatus().name());
            statement.setString(12, user.getRole().name());

            if(statement.executeUpdate() >= 0) {
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
            statement.setString(9, user.getCredentialKey());
            statement.setObject(10, user.getLastLoginDate());
            statement.setString(11, user.getStatus().name());
            statement.setString(12, user.getRole().name());
            statement.setLong(13, user.getId());

            if(statement.executeUpdate() >= 0) {
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to update user by calling update(User user) method", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(User user) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_USER_QUERY)){
            statement.setLong(1, user.getId());
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete user by calling delete(User user) method", e);
        }
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_USER_QUERY)){
            statement.setLong(1, id);
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete user by calling delete(Long id) method", e);
        }
    }

    @Override
    public boolean createUserHash(UserHash hash) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_HASH_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setString(1, hash.getHash());
            statement.setObject(2, hash.getExpires());
            statement.setLong(3, hash.getUser().getId());

            if(statement.executeUpdate() >= 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    Long generatedId = resultSet.getLong(1);
                    hash.setId(generatedId);
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to create user hash by calling createUserHash(UserHash hash) method", e);
        }
        return false;
    }

    @Override
    public boolean deleteUserHashByUserId(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_HASH_QUERY)){
            statement.setLong(1, id);
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete user hash by calling deleteHashByUserId(Long id) method", e);
        }
    }

    @Override
    public Optional<User> findInactiveUserByHashAndHashIsNotExpired(String hash, LocalDateTime validDate) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_INACTIVE_BY_HASH_AND_DATE_QUERY)){
            statement.setString(1, User.Status.INACTIVE.name());
            statement.setString(2, hash);
            statement.setObject(3, validDate);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapper.mapRow(resultSet);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by calling findInactiveUserByHashAndHashIsNotExpired(String hash, LocalDateTime validDate) method", e);
        }
    }

    @Override
    public Optional<User> findInactiveUserByTelegram(String account) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_INACTIVE_BY_TELEGRAM_QUERY)){
            statement.setString(1, User.Status.INACTIVE.name());
            statement.setString(2, account);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapper.mapRow(resultSet);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by calling findInactiveUserByTelegram(String account) method", e);
        }
    }

    @Override
    public Optional<User> findUserByLogin(String login) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_LOGIN_QUERY)){
            statement.setString(1, login);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapper.mapRow(resultSet);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by calling findUserByLogin(String login) method", e);
        }
    }

    @Override
    public Optional<User> findUserByCredentialKey(String key) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_CREDENTIAL_KEY_QUERY)){
            statement.setString(1, key);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapper.mapRow(resultSet);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by calling findUserByCredentialKey(String key) method", e);
        }
    }

    @Override
    public boolean existsByLogin(String login) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_LOGIN_QUERY)){
            statement.setString(1, login);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed check if user exists by calling existsByLogin(String login) method", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_EMAIL_QUERY)){
            statement.setString(1, email);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed check if user exists by calling existsByLogin(String email) method", e);
        }
    }

    @Override
    public boolean existsByTelegram(String telegram) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_TELEGRAM_QUERY)){
            statement.setString(1, telegram);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed check if user exists by calling existsByTelegram(String telegram) method", e);
        }
    }

    @Override
    public boolean existsByEmailExceptUserId(String email, Long userId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_EMAIL_EXCEPT_USER_ID_QUERY)){
            statement.setString(1, email);
            statement.setLong(2, userId);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed check if user exists by calling existsByEmailExceptUserId(String email, Long userId) method", e);
        }
    }

    @Override
    public boolean existsByTelegramExceptUserId(String telegram, Long userId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_TELEGRAM_EXCEPT_USER_ID_QUERY)){
            statement.setString(1, telegram);
            statement.setLong(2, userId);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed check if user exists by calling existsByTelegramExceptUserId(String telegram, Long userId) method", e);
        }
    }
}
