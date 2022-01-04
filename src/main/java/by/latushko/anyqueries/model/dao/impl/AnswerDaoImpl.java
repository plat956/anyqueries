package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.AnswerDao;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.entity.Answer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AnswerDaoImpl extends BaseDao<Long, Answer> implements AnswerDao {
    private static final String SQL_COUNT_BY_USER_ID_QUERY = """
            SELECT count(id) 
            FROM answers 
            WHERE author_id = ?""";

    @Override
    public List<Answer> findAll() throws DaoException {
        return null;
    }

    @Override
    public Optional<Answer> findById(Long id) throws DaoException {
        return Optional.empty();
    }

    @Override
    public boolean create(Answer answer) throws DaoException {
        return false;
    }

    @Override
    public Optional<Answer> update(Answer answer) throws DaoException {
        return Optional.empty();
    }

    @Override
    public boolean delete(Answer answer) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        return false;
    }

    @Override
    public Long countTotalByUserId(Long userId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_COUNT_BY_USER_ID_QUERY)){
            statement.setLong(1, userId);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count answers by calling countTotalAnswersByUserId(Long userId) method", e);
        }
        return 0L;
    }
}
