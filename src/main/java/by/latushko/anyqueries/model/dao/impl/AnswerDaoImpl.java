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
    private static final String SQL_FIND_BY_ID_AND_QUESTION_AUTHOR_ID_QUERY = """
            SELECT a.id, a.text, a.creation_date, a.editing_date, a.solution, a.question_id, count(a.id) OVER() AS total, a.author_id as user_id, u.first_name as user_first_name, u.last_name as user_last_name, u.middle_name as user_middle_name, u.login as user_login, 
            u.password as user_password, u.email as user_email, u.telegram as user_telegram, u.avatar as user_avatar, u.credential_key as user_credential_key, 
            u.last_login_date as user_last_login_date, u.status as user_status, u.role as user_role 
            FROM answers a 
            INNER JOIN users u 
            ON a.author_id = u.id 
            INNER JOIN questions q 
            ON a.question_id = q.id 
            WHERE a.id = ? AND q.author_id = ?""";
    private static final String SQL_FIND_LIMITED_BY_QUESTION_ID_ORDER_BY_CREATION_DATE_ASC_QUERY = """
            SELECT a.id, a.text, a.creation_date, a.editing_date, a.solution, a.question_id, count(a.id) OVER() AS total, a.author_id as user_id, u.first_name as user_first_name, u.last_name as user_last_name, u.middle_name as user_middle_name, u.login as user_login, 
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
    private static final String SQL_UPDATE_SOLUTION_BY_QUESTION_ID_AND_SOLUTION_QUERY = """
            UPDATE answers 
            SET solution = ? 
            WHERE question_id = ? and solution <> ?""";
    private static final String SQL_UPDATE_QUERY = """
            UPDATE answers 
            SET text = ?, creation_date = ?, editing_date = ?, solution = ?, question_id = ?, author_id = ? 
            WHERE id = ?""";
    private static final String SQL_EXISTS_BY_ID_AND_AUTHOR_ID_NOT_QUERY = """
            SELECT 1
            FROM answers a
            WHERE id = ? 
            AND author_id <> ?""";

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
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_QUERY)){
            statement.setString(1, answer.getText());
            statement.setObject(2, answer.getCreationDate());
            statement.setObject(3, answer.getEditingDate());
            statement.setBoolean(4, answer.getSolution());
            statement.setObject(5, answer.getQuestionId());
            statement.setObject(6, answer.getAuthor().getId());
            statement.setObject(7, answer.getId());
            if(statement.executeUpdate() >= 0) {
                return Optional.of(answer);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to update answer by calling update(Answer answer) method", e);
        }
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

    @Override
    public boolean checkIfExistsByIdAndAuthorIdNot(Long answerId, Long authorId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_ID_AND_AUTHOR_ID_NOT_QUERY)){
            statement.setLong(1, answerId);
            statement.setLong(2, authorId);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find answer by calling checkIfExistByIdAndAuthorIdNot(Long answerId, Long authorId) method", e);
        }
    }

    @Override
    public Optional<Answer> findByIdAndQuestionAuthorId(Long answerId, Long userId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID_AND_QUESTION_AUTHOR_ID_QUERY)){
            statement.setLong(1, answerId);
            statement.setLong(2, userId);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapper.mapRow(resultSet);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find answer by calling findByIdAndQuestionAuthorId(Long answerId, Long userId) method", e);
        }
    }

    @Override
    public boolean updateSolutionByQuestionIdAndSolution(Long questionId, Boolean solution) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_SOLUTION_BY_QUESTION_ID_AND_SOLUTION_QUERY)){
            statement.setBoolean(1, solution);
            statement.setLong(2, questionId);
            statement.setBoolean(3, solution);
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to answer solution category by calling updateSolutionByQuestionIdAndSolution method", e);
        }
    }
}
