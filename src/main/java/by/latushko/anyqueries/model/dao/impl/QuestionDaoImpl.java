package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.QuestionDao;
import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.mapper.impl.QuestionMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.QUESTION_TITLE;

public class QuestionDaoImpl extends BaseDao<Long, Question> implements QuestionDao {
    private static final String SQL_FIND_TITLE_BY_TITLE_CONTAINS_QUERY = """
            SELECT title 
            FROM questions 
            WHERE title like ?""";
    private static final String SQL_FIND_BY_ID_QUERY = """
            SELECT q.id, q.title, q.text, q.creation_date, q.editing_date, q.closed, q.category_id, c.name as category_name, c.color as category_color, 
            q.author_id as user_id, u.first_name as user_first_name, u.last_name as user_last_name, u.middle_name as user_middle_name, u.login as user_login, 
            u.password as user_password, u.email as user_email, u.telegram as user_telegram, u.avatar as user_avatar, u.credential_key as user_credential_key, 
            u.last_login_date as user_last_login_date, u.status as user_status, u.role as user_role 
            FROM questions q 
            INNER JOIN users u ON q.author_id = u.id 
            INNER JOIN categories c ON q.category_id = c.id
            WHERE q.id = ?""";
    private static final String SQL_FIND_ALL_PART_QUERY = """
            SELECT q.id, q.title, q.text, q.creation_date, q.editing_date, q.closed, q.category_id, c.name as category_name, c.color as category_color, 
            q.author_id as user_id, u.first_name as user_first_name, u.last_name as user_last_name, u.middle_name as user_middle_name, u.login as user_login, 
            u.password as user_password, u.email as user_email, u.telegram as user_telegram, u.avatar as user_avatar, u.credential_key as user_credential_key, 
            u.last_login_date as user_last_login_date, u.status as user_status, u.role as user_role, count(a.id) as answers_count, count(q.id) OVER() AS total, 
            count(s.id) > 0 as solved  
            FROM questions q 
            INNER JOIN users u ON q.author_id = u.id 
            INNER JOIN categories c ON q.category_id = c.id
            LEFT JOIN answers a ON q.id = a.question_id
            LEFT JOIN answers s ON q.id = s.question_id AND s.solution = true""";
    private static final String SQL_EXISTS_BY_ID_QUERY = """
            SELECT 1 FROM questions
            WHERE id = ?""";
    private static final String SQL_EXISTS_BY_ID_AND_AUTHOR_ID_AND_CLOSED_IS_QUERY = """
            SELECT 1 FROM questions
            WHERE id = ? AND author_id = ? AND closed = ?""";
    private static final String SQL_EXISTS_BY_ID_AND_AUTHOR_ID_QUERY = """
            SELECT 1 FROM questions
            WHERE id = ? AND author_id = ?""";
    private static final String SQL_COUNT_BY_AUTHOR_ID_QUERY = """
            SELECT count(id) 
            FROM questions 
            WHERE author_id = ?""";
    private static final String SQL_COUNT_NOT_CLOSED_QUERY = """
            SELECT count(id) 
            FROM questions 
            WHERE closed = ?""";
    private static final String SQL_COUNT_NOT_CLOSED_BY_AUTHOR_ID_QUERY = """
            SELECT count(id) 
            FROM questions 
            WHERE author_id = ? and closed = ?""";
    private static final String SQL_CREATE_QUERY = """
            INSERT INTO questions(category_id, title, text, creation_date, closed, author_id) 
            VALUES (?, ?, ?, ?, ?, ?)""";
    private static final String SQL_CREATE_QUESTION_ATTACHMENT_QUERY = """
            INSERT INTO question_attachment(question_id, attachment_id) 
            VALUES (?, ?)""";
    private static final String SQL_UPDATE_QUERY = """
            UPDATE questions 
            SET title = ?, text = ?, creation_date = ?, editing_date = ?, closed = ?, category_id = ?, author_id = ?   
            WHERE id = ?""";
    private static final String SQL_DELETE_QUERY = """
            DELETE FROM questions 
            WHERE id = ?""";
    private static final String SQL_WHERE_EXPRESSION = " WHERE ";
    private static final String SQL_AND_EXPRESSION = " AND ";
    private static final String SQL_WHERE_RESOLVED_CLAUSE = "s.id is not null ";
    private static final String SQL_WHERE_AUTHOR_CLAUSE = "q.author_id = ? ";
    private static final String SQL_WHERE_CATEGORY_CLAUSE = "q.category_id = ? ";
    private static final String SQL_WHERE_TITLE_LIKE_CLAUSE = "q.title like ? ";
    private static final String SQL_FIND_ALL_GROUP_ORDER_LIMIT_CLAUSE = " GROUP BY q.id ORDER BY %s DESC LIMIT ?, ?";
    private static final String SQL_SEARCH_END_CLAUSE = "ORDER BY title ASC LIMIT ?";
    private static final String SQL_SEARCH_WHERE_CATEGORY_CLAUSE = " AND category_id = ? ";
    private static final String SQL_SEARCH_WHERE_AUTHOR_CLAUSE = " AND author_id = ? ";
    private static final String SQL_CREATION_DATE_FIELD = "q.creation_date";
    private static final String SQL_ANSWERS_COUNT_FIELD = "answers_count";
    private final QuestionMapper mapper = new QuestionMapper();

    @Override
    public Optional<Question> findById(Long id) throws DaoException {
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
            throw new DaoException("Failed to find question by calling findById(Long id) method", e);
        }
    }

    @Override
    public boolean create(Question question) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)){
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
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_QUERY)){
            statement.setString(1, question.getTitle());
            statement.setString(2, question.getText());
            statement.setObject(3, question.getCreationDate());
            statement.setObject(4, question.getEditingDate());
            statement.setBoolean(5, question.getClosed());
            statement.setLong(6, question.getCategory().getId());
            statement.setLong(7, question.getAuthor().getId());
            statement.setLong(8, question.getId());
            if(statement.executeUpdate() >= 0) {
                return Optional.of(question);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to update question by calling update(Question question) method", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_QUERY)){
            statement.setLong(1, id);
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete question by calling delete(Long id) method", e);
        }
    }

    @Override
    public List<String> findTitleByTitleContainsAndCategoryIdAndAuthorIdOrderByTitleAscLimitedTo(String likePattern,
                                                      Long categoryId, Long authorId, int limit) throws DaoException {
        List<String> result = new ArrayList<>();
        String query = buildSearchQuery(categoryId, authorId);
        try (PreparedStatement statement = connection.prepareStatement(query)){
            int index = 0;
            statement.setString(++index, LIKE_MARKER + likePattern + LIKE_MARKER);
            if(categoryId != null) {
                statement.setLong(++index, categoryId);
            } else if(authorId != null) {
                statement.setLong(++index, authorId);
            }
            statement.setInt(++index, limit);
            try(ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    result.add(resultSet.getString(QUESTION_TITLE));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find question titles by calling findTitleByTitleContainsAndCategoryIdAndAuthorIdOrderByTitleAscLimitedTo method", e);
        }
        return result;
    }

    @Override
    public List<Question> findByResolvedAndAuthorIdAndCategoryIdAndTitleContainsOrderByNewestLimitedTo(boolean resolved,
                      boolean newestFirst, Long authorId, Long categoryId, String titlePattern, int offset, int limit) throws DaoException {
        String query = buildQuestionsQuery(resolved, newestFirst, authorId, categoryId, titlePattern);
        try (PreparedStatement statement = connection.prepareStatement(query)){
            int index = 0;
            if(authorId != null) {
                statement.setLong(++index, authorId);
            }
            if(categoryId != null) {
                statement.setLong(++index, categoryId);
            }
            if(titlePattern != null) {
                statement.setString(++index, LIKE_MARKER + titlePattern + LIKE_MARKER);
            }
            statement.setInt(++index, offset);
            statement.setInt(++index, limit);

            try(ResultSet resultSet = statement.executeQuery()) {
                return mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find questions by calling findByResolvedAndAuthorIdAndCategoryIdAndTitleContainsOrderByNewestLimitedTo method", e);
        }
    }

    @Override
    public boolean existsById(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_ID_QUERY)){
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed check if question exists by calling existsById(Long id) method", e);
        }
    }

    @Override
    public boolean existsByIdAndAuthorIdAndClosedIs(Long id, Long authorId, boolean closed) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_ID_AND_AUTHOR_ID_AND_CLOSED_IS_QUERY)){
            statement.setLong(1, id);
            statement.setLong(2, authorId);
            statement.setBoolean(3, closed);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed check if question exists by calling existsByIdAndAuthorIdAndClosedIs(Long id, Long authorId, boolean closed) method", e);
        }
    }

    @Override
    public boolean existsByIdAndAuthorId(Long id, Long authorId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_ID_AND_AUTHOR_ID_QUERY)){
            statement.setLong(1, id);
            statement.setLong(2, authorId);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed check if question exists by existsByIdAndAuthorId(Long id, Long authorId) method", e);
        }
    }

    @Override
    public Long countByAuthorId(Long authorId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_COUNT_BY_AUTHOR_ID_QUERY)){
            statement.setLong(1, authorId);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count questions by calling countByAuthorId(Long authorId) method", e);
        }
        return 0L;
    }

    @Override
    public Long countNotClosed() throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_COUNT_NOT_CLOSED_QUERY)){
            statement.setLong(1, 0);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count questions by calling countNotClosed() method", e);
        }
        return 0L;
    }

    @Override
    public Long countNotClosedByAuthorId(Long authorId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_COUNT_NOT_CLOSED_BY_AUTHOR_ID_QUERY)){
            statement.setLong(1, authorId);
            statement.setLong(2, 0);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to count questions by calling countNotClosedByAuthorId(Long authorId) method", e);
        }
        return 0L;
    }

    @Override
    public boolean createQuestionAttachment(Long questionId, Long attachmentId) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_QUESTION_ATTACHMENT_QUERY)){
            statement.setLong(1, questionId);
            statement.setLong(2, attachmentId);
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to create question-attachment relationship by calling createQuestionAttachment method", e);
        }
    }

    private String buildQuestionsQuery(boolean resolved, boolean newestFirst, Long authorId, Long categoryId, String titlePattern) {
        StringBuilder query = new StringBuilder(SQL_FIND_ALL_PART_QUERY);
        StringBuilder whereClause = new StringBuilder();
        if(resolved) {
            whereClause.append(SQL_WHERE_RESOLVED_CLAUSE);
        }
        if(authorId != null) {
            if(!whereClause.isEmpty()) {
                whereClause.append(SQL_AND_EXPRESSION);
            }
            whereClause.append(SQL_WHERE_AUTHOR_CLAUSE);
        }
        if(categoryId != null) {
            if(!whereClause.isEmpty()) {
                whereClause.append(SQL_AND_EXPRESSION);
            }
            whereClause.append(SQL_WHERE_CATEGORY_CLAUSE);
        }
        if(titlePattern != null) {
            if(!whereClause.isEmpty()) {
                whereClause.append(SQL_AND_EXPRESSION);
            }
            whereClause.append(SQL_WHERE_TITLE_LIKE_CLAUSE);
        }
        if(!whereClause.isEmpty()) {
            query.append(SQL_WHERE_EXPRESSION).append(whereClause);
        }
        query.append(String.format(SQL_FIND_ALL_GROUP_ORDER_LIMIT_CLAUSE, newestFirst ? SQL_CREATION_DATE_FIELD : SQL_ANSWERS_COUNT_FIELD));

        return query.toString();
    }

    private String buildSearchQuery(Long categoryId, Long authorId) {
        StringBuilder query = new StringBuilder(SQL_FIND_TITLE_BY_TITLE_CONTAINS_QUERY);
        if(categoryId != null) {
            query.append(SQL_SEARCH_WHERE_CATEGORY_CLAUSE);
        } else if(authorId != null) {
            query.append(SQL_SEARCH_WHERE_AUTHOR_CLAUSE);
        }
        query.append(SQL_SEARCH_END_CLAUSE);
        return query.toString();
    }
}
