package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.AnswerDao;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.entity.Answer;
import by.latushko.anyqueries.model.mapper.RowMapper;
import by.latushko.anyqueries.model.mapper.impl.AnswerMapper;

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
    private static final String SQL_FIND_LIMITED_BY_QUESTION_ID_ORDER_BY_CREATION_DATE_ASC_QUERY = """
            SELECT a.id, a.text, a.creation_date, a.editing_date, a.solution, count(a.id) OVER() AS total, a.author_id as user_id, u.first_name as user_first_name, u.last_name as user_last_name, u.middle_name as user_middle_name, u.login as user_login, 
            u.password as user_password, u.email as user_email, u.telegram as user_telegram, u.avatar as user_avatar, u.credential_key as user_credential_key, 
            u.last_login_date as user_last_login_date, u.status as user_status, u.role as user_role, sum(r.grade) as rating, ur.grade 
            FROM answers a
            INNER JOIN users u
            ON a.author_id = u.id 
            LEFT JOIN rating r ON a.id = r.answer_id 
            LEFT JOIN rating ur ON a.id = ur.answer_id AND ur.user_id = ? 
            WHERE a.question_id = ? 
            GROUP BY a.id 
            ORDER BY a.creation_date ASC 
            LIMIT ?, ?""";

    RowMapper mapper = new AnswerMapper();

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

    @Override
    public List<Answer> findLimitedByQuestionIdOrderByCreationDateAsc(int offset, int limit, Long questionId, Long userId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_LIMITED_BY_QUESTION_ID_ORDER_BY_CREATION_DATE_ASC_QUERY)){
            statement.setLong(1, userId);
            statement.setLong(2, questionId);
            statement.setInt(3, offset);
            statement.setInt(4, limit);

            try(ResultSet resultSet = statement.executeQuery()) {
                return mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find answers by calling findLimitedByQuestionIdOrderByCreationDateAsc method", e);
        }
    }
}
