package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.AttachmentDao;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.entity.Attachment;
import by.latushko.anyqueries.model.mapper.RowMapper;
import by.latushko.anyqueries.model.mapper.impl.AttachmentMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AttachmentDaoImpl extends BaseDao<Long, Attachment> implements AttachmentDao {
    private static final String SQL_CREATE_QUERY = """
            INSERT INTO attachments(file) 
            VALUES (?)""";
    private static final String SQL_DELETE_BY_QUESTION_ID_QUERY = """
            DELETE a 
            FROM attachments a 
            INNER JOIN question_attachment qa 
            ON a.id = qa.attachment_id 
            AND qa.question_id = ?""";
    private static final String SQL_FIND_BY_ANSWER_ID_QUERY = """
            SELECT a.id, a.file  
            FROM attachments a 
            INNER JOIN answer_attachment aa 
            ON a.id = aa.attachment_id 
            AND aa.answer_id = ?""";
    private static final String SQL_DELETE_BY_CATEGORY_ID_QUERY = """
            DELETE a 
            FROM attachments a
            INNER JOIN question_attachment qa ON a.id = attachment_id
            INNER JOIN questions q ON q.id = qa.question_id
            WHERE q.category_id = ?""";
    private static final String SQL_FIND_BY_QUESTION_ID_QUERY = """
            SELECT a.id, a.file  
            FROM attachments a 
            INNER JOIN question_attachment qa 
            ON a.id = qa.attachment_id 
            AND qa.question_id = ?""";
    private static final String SQL_FIND_BY_CATEGORY_ID_QUERY = """
            SELECT a.id, a.file  
            FROM attachments a
            INNER JOIN question_attachment qa ON a.id = attachment_id
            INNER JOIN questions q ON q.id = qa.question_id
            WHERE q.category_id = ?""";
    private RowMapper mapper = new AttachmentMapper();

    @Override
    public List<Attachment> findAll() throws DaoException {
        return null;
    }

    @Override
    public Optional<Attachment> findById(Long id) throws DaoException {
        return Optional.empty();
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
            throw new DaoException("Failed to create attachment by calling create(Attachment attachment) method", e);
        }
        return false;
    }

    @Override
    public Optional<Attachment> update(Attachment attachment) throws DaoException {
        return Optional.empty();
    }

    @Override
    public boolean delete(Attachment attachment) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        return false;
    }

    @Override
    public boolean deleteByQuestionId(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_QUESTION_ID_QUERY)){
            statement.setLong(1, id);
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete attachment by calling deleteByQuestionId(Long id) method", e);
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
            throw new DaoException("Failed to find attachments by calling findByQuestionId(Long id) method", e);
        }
    }

    @Override
    public boolean deleteByCategoryId(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_CATEGORY_ID_QUERY)){
            statement.setLong(1, id);
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete attachment by calling deleteByCategoryId(Long id) method", e);
        }
    }

    @Override
    public List<Attachment> findByCategoryId(Long id) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_CATEGORY_ID_QUERY)){
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                return mapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to find attachments by calling findByCategoryId(Long id) method", e);
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
            throw new DaoException("Failed to find attachments by calling findByAnswerId(Long id) method", e);
        }
    }
}
