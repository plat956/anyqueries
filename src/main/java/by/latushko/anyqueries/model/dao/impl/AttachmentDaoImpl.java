package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.AttachmentDao;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.entity.Attachment;
import by.latushko.anyqueries.model.mapper.RowMapper;
import by.latushko.anyqueries.model.mapper.impl.AttachmentMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AttachmentDaoImpl extends BaseDao<Long, Attachment> implements AttachmentDao {
    private static final Logger logger = LogManager.getLogger();
    private static final String SQL_FIND_BY_ANSWER_ID_QUERY = """
            SELECT a.id, a.file  
            FROM attachments a 
            INNER JOIN answer_attachment aa ON a.id = aa.attachment_id 
            WHERE aa.answer_id = ?""";
    private static final String SQL_FIND_BY_QUESTION_ID_QUERY = """
            SELECT a.id, a.file  
            FROM attachments a 
            INNER JOIN question_attachment qa ON a.id = qa.attachment_id 
            WHERE qa.question_id = ?""";
    private static final String SQL_FIND_BY_CATEGORY_ID_QUERY = """
            SELECT a.id, a.file 
            FROM attachments a 
            INNER JOIN question_attachment qa ON a.id = qa.attachment_id 
            INNER JOIN questions q ON q.id = qa.question_id 
            WHERE q.category_id = ? 
            UNION 
            SELECT a.id, a.file FROM attachments a 
            INNER JOIN answer_attachment aa ON a.id = aa.attachment_id 
            INNER JOIN answers an ON an.id = aa.answer_id 
            INNER JOIN questions q ON an.question_id = q.id 
            WHERE q.category_id = ?""";
    private static final String SQL_FIND_BY_USER_ID_QUERY = """
            SELECT a.id, a.file 
            FROM attachments a 
            INNER JOIN question_attachment qa ON a.id = qa.attachment_id 
            INNER JOIN questions q ON q.id = qa.question_id 
            WHERE q.author_id = ? 
            UNION 
            SELECT a.id, a.file FROM attachments a 
            INNER JOIN answer_attachment aa ON a.id = aa.attachment_id 
            INNER JOIN answers an ON an.id = aa.answer_id 
            WHERE an.author_id = ?""";
    private static final String SQL_FIND_ALL_AND_ANSWERS_ATTACHMENTS_BY_QUESTION_ID_QUERY = """
            SELECT a.id, a.file 
            FROM attachments a 
            INNER JOIN answer_attachment aa ON a.id = aa.attachment_id 
            INNER JOIN answers an ON aa.answer_id = an.id
            WHERE an.question_id = ? 
            UNION 
            SELECT a.id, a.file 
            FROM attachments a 
            INNER JOIN question_attachment qa ON a.id = qa.attachment_id 
            WHERE qa.question_id = ?""";
    private static final String SQL_CREATE_QUERY = """
            INSERT INTO attachments(file) 
            VALUES (?)""";
    private static final String SQL_DELETE_QUERY = """
            DELETE FROM attachments 
            WHERE id = ?""";
    private final RowMapper mapper = new AttachmentMapper();

    @Override
    public Optional<Attachment> findById(Long id) throws DaoException {
        logger.error("Method findById is unsupported");
        throw new UnsupportedOperationException("Method findById is unsupported");
    }

    @Override
    public boolean create(Attachment attachment) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setString(1, attachment.getFile());
            if(statement.executeUpdate() >= 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    Long generatedId = resultSet.getLong(1);
                    attachment.setId(generatedId);
                    return true;
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to create attachment by calling create method", e);
            throw new DaoException("Failed to create attachment by calling create method", e);
        }
        return false;
    }

    @Override
    public Optional<Attachment> update(Attachment attachment) throws DaoException {
        logger.error("Method update is unsupported");
        throw new UnsupportedOperationException("Method update is unsupported");
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_QUERY)){
            statement.setLong(1, id);
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            logger.error("Failed to delete attachment by calling delete method", e);
            throw new DaoException("Failed to delete attachment by calling delete method", e);
        }
    }

    @Override
    public List<Attachment> findByQuestionId(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_QUESTION_ID_QUERY)){
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                return mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            logger.error("Failed to find attachments by calling findByQuestionId method", e);
            throw new DaoException("Failed to find attachments by calling findByQuestionId method", e);
        }
    }

    @Override
    public List<Attachment> findByCategoryId(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_CATEGORY_ID_QUERY)){
            statement.setLong(1, id);
            statement.setLong(2, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                return mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            logger.error("Failed to find attachments by calling findByCategoryId method", e);
            throw new DaoException("Failed to find attachments by calling findByCategoryId method", e);
        }
    }

    @Override
    public List<Attachment> findByAnswerId(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ANSWER_ID_QUERY)){
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                return mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            logger.error("Failed to find attachments by calling findByAnswerId method", e);
            throw new DaoException("Failed to find attachments by calling findByAnswerId method", e);
        }
    }

    @Override
    public List<Attachment> findByUserId(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_USER_ID_QUERY)){
            statement.setLong(1, id);
            statement.setLong(2, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                return mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            logger.error("Failed to find attachments by calling findByUserId method", e);
            throw new DaoException("Failed to find attachments by calling findByUserId method", e);
        }
    }

    @Override
    public List<Attachment> findAllAndAnswersAttachmentsByQuestionId(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_AND_ANSWERS_ATTACHMENTS_BY_QUESTION_ID_QUERY)){
            statement.setLong(1, id);
            statement.setLong(2, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                return mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            logger.error("Failed to find attachments by calling findAllAndAnswersAttachmentsByQuestionId method", e);
            throw new DaoException("Failed to find attachments by calling findAllAndAnswersAttachmentsByQuestionId method", e);
        }
    }
}
