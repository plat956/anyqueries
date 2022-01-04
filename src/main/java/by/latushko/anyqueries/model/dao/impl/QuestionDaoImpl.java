package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.QuestionDao;
import by.latushko.anyqueries.model.entity.Question;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.QUESTION_TITLE;

public class QuestionDaoImpl extends BaseDao<Long, Question> implements QuestionDao {
    private static final String SQL_FIND_TITLE_LIKE_ORDERED_AND_LIMITED_QUERY = """
            SELECT title 
            FROM questions 
            WHERE title like ? 
            ORDER BY title ASC 
            LIMIT ?""";
    private static final String SQL_CREATE_QUESTION_QUERY = """
            INSERT INTO questions(category_id, title, text, creation_date, closed, author_id) 
            VALUES (?, ?, ?, ?, ?, ?)""";
    private static final String SQL_CREATE_QUESTION_ATTACHMENT_QUERY = """
            INSERT INTO question_attachment(question_id, attachment_id) 
            VALUES (?, ?)""";
    private static final String SQL_COUNT_BY_AUTHOR_ID_QUERY = """
            SELECT count(*) 
            FROM questions 
            WHERE author_id = ?""";
    private static final String SQL_COUNT_CLOSED_IS_QUERY = """
            SELECT count(*) 
            FROM questions 
            WHERE closed = ?""";
    private static final String SQL_COUNT_CLOSED_IS_BY_AUTHOR_ID_QUERY = """
            SELECT count(*) 
            FROM questions 
            WHERE author_id = ? and closed = ?""";

    @Override
    public List<Question> findAll() throws DaoException {
        return null;
    }

    @Override
    public Optional<Question> findById(Long id) throws DaoException {
        return Optional.empty();
    }

    @Override
    public boolean create(Question question) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_QUESTION_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setLong(1, question.getCategory().getId());
            statement.setString(2, question.getTitle());
            statement.setString(3, question.getText());
            statement.setObject(4, question.getCreationDate());
            statement.setBoolean(5, question.getClosed());
            statement.setLong(6, question.getAuthor().getId());

            if(statement.executeUpdate() >= 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    Long generatedId = resultSet.getLong(1);
                    question.setId(generatedId);
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to create question by calling create(Question question) method", e);
        }
        return false;
    }

    @Override
    public Optional<Question> update(Question question) throws DaoException {
        return Optional.empty();
    }

    @Override
    public boolean delete(Question question) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        return false;
    }

    @Override
    public List<String> findTitleLikeOrderedAndLimited(String pattern, int limit) throws DaoException {
        List<String> result = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_TITLE_LIKE_ORDERED_AND_LIMITED_QUERY)){
            statement.setString(1, LIKE_MARKER + pattern + LIKE_MARKER);
            statement.setInt(2, limit);
            try(ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    String title = resultSet.getString(QUESTION_TITLE);
                    result.add(title);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find question titles by calling findTitleLike(String pattern, int limit) method", e);
        }
        return result;
    }

    @Override
    public Long countTotalQuestionsByAuthorId(Long authorId) throws DaoException {
        Long count = 0L;
        try (PreparedStatement statement = connection.prepareStatement(SQL_COUNT_BY_AUTHOR_ID_QUERY)){
            statement.setLong(1, authorId);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    count = resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count questions by calling countTotalQuestionsByAuthorId(Long authorId) method", e);
        }
        return count;
    }

    @Override
    public Long countTotalNotClosedQuestions() throws DaoException {
        Long count = 0L;
        try (PreparedStatement statement = connection.prepareStatement(SQL_COUNT_CLOSED_IS_QUERY)){
            statement.setLong(1, 0);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    count = resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count questions by calling countTotalNotClosedQuestions() method", e);
        }
        return count;
    }

    @Override
    public Long countTotalNotClosedQuestionsByAuthorId(Long authorId) throws DaoException {
        Long count = 0L;
        try (PreparedStatement statement = connection.prepareStatement(SQL_COUNT_CLOSED_IS_BY_AUTHOR_ID_QUERY)){
            statement.setLong(1, authorId);
            statement.setLong(2, 0);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    count = resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count questions by calling countTotalNotClosedQuestionsByAuthorId(Long authorId) method", e);
        }
        return count;
    }

    @Override
    public boolean createQuestionAttachment(Long questionId, Long attachmentId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_QUESTION_ATTACHMENT_QUERY)){
            statement.setLong(1, questionId);
            statement.setLong(2, attachmentId);
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to create question-attachment relationship by calling createQuestionAttachment(Long questionId, Long attachmentId) method", e);
        }
    }
}