package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.AttachmentDao;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.entity.Attachment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AttachmentDaoImpl extends BaseDao<Long, Attachment> implements AttachmentDao {
    private static final String SQL_CREATE_QUERY = """
            INSERT INTO attachments(file) 
            VALUES (?)""";
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
}
