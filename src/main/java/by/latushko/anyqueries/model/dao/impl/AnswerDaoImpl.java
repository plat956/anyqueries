package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.AnswerDao;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.entity.Answer;
import by.latushko.anyqueries.model.mapper.RowMapper;
import by.latushko.anyqueries.model.mapper.impl.AnswerMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AnswerDaoImpl extends BaseDao<Long, Answer> implements AnswerDao {
    private static final Logger logger = LogManager.getLogger();
    private static final String SQL_FIND_BY_ID_AND_QUESTION_AUTHOR_ID_AND_QUESTION_CLOSED_IS_QUERY = """
            SELECT a.id, a.text, a.creation_date, a.editing_date, a.solution, a.question_id, count(a.id) OVER() AS total, a.author_id as user_id, 
            u.first_name as user_first_name, u.last_name as user_last_name, u.middle_name as user_middle_name, u.login as user_login, 
            u.password as user_password, u.email as user_email, u.telegram as user_telegram, u.avatar as user_avatar, u.credential_key as user_credential_key, 
            u.last_login_date as user_last_login_date, u.status as user_status, u.role as user_role 
            FROM answers a 
            INNER JOIN users u ON a.author_id = u.id 
            INNER JOIN questions q ON a.question_id = q.id 
            WHERE a.id = ? AND q.author_id = ? AND q.closed = ?""";
    private static final String SQL_FIND_ALL_WITH_USER_GRADE_BY_QUESTION_ID_ORDER_BY_CREATION_DATE_ASC_LIMITED_TO_QUERY = """
            SELECT a.id, a.text, a.creation_date, a.editing_date, a.solution, a.question_id, count(a.id) OVER() AS total, a.author_id as user_id, 
            u.first_name as user_first_name, u.last_name as user_last_name, u.middle_name as user_middle_name, u.login as user_login, 
            u.password as user_password, u.email as user_email, u.telegram as user_telegram, u.avatar as user_avatar, u.credential_key as user_credential_key, 
            u.last_login_date as user_last_login_date, u.status as user_status, u.role as user_role, sum(r.grade) as rating, ur.grade 
            FROM answers a
            INNER JOIN users u ON a.author_id = u.id 
            LEFT JOIN rating r ON a.id = r.answer_id 
            LEFT JOIN rating ur ON a.id = ur.answer_id AND ur.user_id = ? 
            WHERE a.question_id = ? 
            GROUP BY a.id 
            ORDER BY a.creation_date ASC 
            LIMIT ?, ?""";
    private static final String SQL_FIND_BY_QUESTION_ID_ORDER_BY_CREATION_DATE_ASC_LIMITED_TO_QUERY = """
            SELECT a.id, a.text, a.creation_date, a.editing_date, a.solution, a.question_id, count(a.id) OVER() AS total, a.author_id as user_id, 
            u.first_name as user_first_name, u.last_name as user_last_name, u.middle_name as user_middle_name, u.login as user_login, 
            u.password as user_password, u.email as user_email, u.telegram as user_telegram, u.avatar as user_avatar, u.credential_key as user_credential_key, 
            u.last_login_date as user_last_login_date, u.status as user_status, u.role as user_role, sum(r.grade) as rating 
            FROM answers a
            INNER JOIN users u ON a.author_id = u.id 
            LEFT JOIN rating r ON a.id = r.answer_id 
            WHERE a.question_id = ? 
            GROUP BY a.id 
            ORDER BY a.creation_date ASC 
            LIMIT ?, ?""";
    private static final String SQL_FIND_BY_ID_QUERY = """
            SELECT a.id, a.text, a.creation_date, a.editing_date, a.solution, a.question_id, count(a.id) OVER() AS total, a.author_id as user_id, 
            u.first_name as user_first_name, u.last_name as user_last_name, u.middle_name as user_middle_name, u.login as user_login, 
            u.password as user_password, u.email as user_email, u.telegram as user_telegram, u.avatar as user_avatar, u.credential_key as user_credential_key, 
            u.last_login_date as user_last_login_date, u.status as user_status, u.role as user_role 
            FROM answers a 
            INNER JOIN users u ON a.author_id = u.id 
            WHERE a.id = ?""";
    private static final String SQL_EXISTS_BY_ID_AND_AUTHOR_ID_NOT_QUERY = """
            SELECT 1
            FROM answers a
            WHERE id = ? AND author_id <> ?""";
    private static final String SQL_EXISTS_BY_ID_AND_AUTHOR_ID_AND_QUESTION_CLOSED_IS_QUERY = """
            SELECT 1
            FROM answers a 
            INNER JOIN questions q ON a.question_id = q.id 
            WHERE a.id = ? AND a.author_id = ? AND q.closed = ?""";
    private static final String SQL_EXISTS_BY_ID_AND_AUTHOR_ID_QUERY = """
            SELECT 1
            FROM answers
            WHERE id = ? AND author_id = ?""";
    private static final String SQL_COUNT_BY_USER_ID_QUERY = """
            SELECT count(id) 
            FROM answers 
            WHERE author_id = ?""";
    private static final String SQL_COUNT_BY_QUESTION_ID_QUERY = """
            SELECT count(id) 
            FROM answers 
            WHERE question_id = ?""";
    private static final String SQL_CREATE_QUERY = """
            INSERT INTO answers(text, creation_date, editing_date, solution, question_id, author_id) 
            VALUES (?, ?, ?, ?, ?, ?)""";
    private static final String SQL_CREATE_ANSWER_ATTACHMENT_QUERY = """
            INSERT INTO answer_attachment(answer_id, attachment_id) 
            VALUES (?, ?)""";
    private static final String SQL_UPDATE_SOLUTION_BY_QUESTION_ID_AND_SOLUTION_QUERY = """
            UPDATE answers 
            SET solution = ? 
            WHERE question_id = ? and solution <> ?""";
    private static final String SQL_UPDATE_QUERY = """
            UPDATE answers 
            SET text = ?, creation_date = ?, editing_date = ?, solution = ?, question_id = ?, author_id = ? 
            WHERE id = ?""";
    private static final String SQL_DELETE_QUERY = """
            DELETE FROM answers 
            WHERE id = ?""";
    private final RowMapper mapper = new AnswerMapper();

    @Override
    public Optional<Answer> findById(Long id) throws DaoException {
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
            logger.error("Failed to find answer by calling findById method", e);
            throw new DaoException("Failed to find answer by calling findById method", e);
        }
    }

    @Override
    public boolean create(Answer answer) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setString(1, answer.getText());
            statement.setObject(2, answer.getCreationDate());
            statement.setObject(3, answer.getEditingDate());
            statement.setBoolean(4, answer.getSolution());
            statement.setObject(5, answer.getQuestionId());
            statement.setObject(6, answer.getAuthor().getId());
            if(statement.executeUpdate() >= 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    Long generatedId = resultSet.getLong(1);
                    answer.setId(generatedId);
                    return true;
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to create answer by calling create method", e);
            throw new DaoException("Failed to create answer by calling create method", e);
        }
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
            logger.error("Failed to update answer by calling update method", e);
            throw new DaoException("Failed to update answer by calling update method", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_QUERY)){
            statement.setLong(1, id);
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            logger.error("Failed to delete answer by calling delete method", e);
            throw new DaoException("Failed to delete answer by calling delete method", e);
        }
    }

    @Override
    public List<Answer> findAllWithUserGradeByQuestionIdOrderByCreationDateAscLimitedTo(Long questionId, Long userId, int offset, int limit) throws DaoException {
        String query;
        if(userId != null) {
            query = SQL_FIND_ALL_WITH_USER_GRADE_BY_QUESTION_ID_ORDER_BY_CREATION_DATE_ASC_LIMITED_TO_QUERY;
        } else {
            query = SQL_FIND_BY_QUESTION_ID_ORDER_BY_CREATION_DATE_ASC_LIMITED_TO_QUERY;
        }
        try (PreparedStatement statement = connection.prepareStatement(query)){
            int index = 0;
            if(userId != null) {
                statement.setLong(++index, userId);
            }
            statement.setLong(++index, questionId);
            statement.setInt(++index, offset);
            statement.setInt(++index, limit);
            try(ResultSet resultSet = statement.executeQuery()) {
                return mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            logger.error("Failed to find answers by calling findAllWithUserGradeByQuestionIdOrderByCreationDateAscLimitedTo method", e);
            throw new DaoException("Failed to find answers by calling findAllWithUserGradeByQuestionIdOrderByCreationDateAscLimitedTo method", e);
        }
    }

    @Override
    public Optional<Answer> findByIdAndQuestionAuthorIdAndQuestionClosedIs(Long answerId, Long userId, boolean closed) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID_AND_QUESTION_AUTHOR_ID_AND_QUESTION_CLOSED_IS_QUERY)){
            statement.setLong(1, answerId);
            statement.setLong(2, userId);
            statement.setBoolean(3, closed);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return mapper.mapRow(resultSet);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to find answer by calling findByIdAndQuestionAuthorIdAndQuestionClosedIs method", e);
            throw new DaoException("Failed to find answer by calling findByIdAndQuestionAuthorIdAndQuestionClosedIs method", e);
        }
    }

    @Override
    public boolean existsByIdAndAuthorIdNot(Long answerId, Long authorId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_ID_AND_AUTHOR_ID_NOT_QUERY)){
            statement.setLong(1, answerId);
            statement.setLong(2, authorId);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("Failed to find answer by calling existsByIdAndAuthorIdNot method", e);
            throw new DaoException("Failed to find answer by calling existsByIdAndAuthorIdNot method", e);
        }
    }

    @Override
    public boolean existsByIdAndAuthorIdAndQuestionClosedIs(Long id, Long authorId, boolean closed) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_ID_AND_AUTHOR_ID_AND_QUESTION_CLOSED_IS_QUERY)){
            statement.setLong(1, id);
            statement.setLong(2, authorId);
            statement.setBoolean(3, closed);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("Failed to find answer by calling existsByIdAndAuthorIdAndQuestionClosedIs method", e);
            throw new DaoException("Failed to find answer by calling existsByIdAndAuthorIdAndQuestionClosedIs method", e);
        }
    }

    @Override
    public boolean existsByIdAndAuthorIdAndQuestion(Long id, Long authorId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_ID_AND_AUTHOR_ID_QUERY)){
            statement.setLong(1, id);
            statement.setLong(2, authorId);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("Failed to find answer by calling existsByIdAndAuthorIdAndQuestion method", e);
            throw new DaoException("Failed to find answer by calling existsByIdAndAuthorIdAndQuestion method", e);
        }
    }

    @Override
    public Long countByUserId(Long userId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_COUNT_BY_USER_ID_QUERY)){
            statement.setLong(1, userId);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to count answers by calling countByUserId method", e);
            throw new DaoException("Failed to count answers by calling countByUserId method", e);
        }
        return 0L;
    }

    @Override
    public Long countByQuestionId(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_COUNT_BY_QUESTION_ID_QUERY)){
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to count answers by calling countByQuestionId method", e);
            throw new DaoException("Failed to count answers by calling countByQuestionId method", e);
        }
        return 0L;
    }

    @Override
    public boolean createAnswerAttachment(Long answerId, Long attachmentId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_ANSWER_ATTACHMENT_QUERY)){
            statement.setLong(1, answerId);
            statement.setLong(2, attachmentId);
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            logger.error("Failed to create answer-attachment relationship by calling createAnswerAttachment method", e);
            throw new DaoException("Failed to create answer-attachment relationship by calling createAnswerAttachment method", e);
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
            logger.error("Failed to answer solution by calling updateSolutionByQuestionIdAndSolution method", e);
            throw new DaoException("Failed to answer solution by calling updateSolutionByQuestionIdAndSolution method", e);
        }
    }
}
