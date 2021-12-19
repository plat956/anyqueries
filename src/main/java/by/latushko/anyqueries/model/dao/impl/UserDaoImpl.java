package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.model.dao.UserDao;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.mapper.RowMapper;
import by.latushko.anyqueries.model.mapper.impl.UserMapper;
import by.latushko.anyqueries.model.pool.ConnectionPool;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class UserDaoImpl implements UserDao {
    private static final String SQL_FIND_ALL_QUERY = """
            SELECT id, first_name, last_name, middle_name, login, password, email, telegram, avatar, last_login_date, status, role 
            FROM users""";
    private static final String SQL_FIND_BY_ID_QUERY = """
            SELECT id, first_name, last_name, middle_name, login, password, email, telegram, avatar, last_login_date, status, role 
            FROM users 
            WHERE id = ?""";
    private static final String SQL_CREATE_QUERY = """
            INSERT INTO users(first_name, last_name, middle_name, login, password, email, telegram, avatar, last_login_date, status, role) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)""";
    private static final String SQL_UPDATE_QUERY = """
            UPDATE users 
            SET first_name = ?, last_name = ?, middle_name = ?, login = ?, password = ?, email = ?, telegram = ?, avatar = ?, last_login_date = ?, status = ?, role = ?  
            WHERE id = ?""";
    private static final String SQL_DELETE_QUERY = """
            DELETE FROM users WHERE id = ?""";
    private static UserDao instance;
    private static AtomicBoolean creator = new AtomicBoolean(false);
    private static ReentrantLock lockerSingleton = new ReentrantLock();
    private final RowMapper<User> mapper;

    private UserDaoImpl() {
        mapper = new UserMapper();
    }

    public static UserDao getInstance(){
        if(!creator.get()){
            try{
                lockerSingleton.lock();
                if(instance == null){
                    instance = new UserDaoImpl();
                    creator.set(true);
                }
            } finally {
                lockerSingleton.unlock();
            }
        }
        return instance;
    }

    @Override
    public boolean create(User user) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)){
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
    public List<User> findAll() throws DaoException {
        List<User> users;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             Statement statement = connection.createStatement()){
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
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID_QUERY)){
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
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_QUERY)){
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
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_QUERY)){
            statement.setLong(1, user.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete user by calling delete(User user) method", e);
        }
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_QUERY)){
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete user by calling delete(Long id) method", e);
        }
    }
}
