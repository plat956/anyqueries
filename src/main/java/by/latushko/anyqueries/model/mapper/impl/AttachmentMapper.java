package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.Attachment;
import by.latushko.anyqueries.model.mapper.RowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.ATTACHMENT_FILE;
import static by.latushko.anyqueries.model.mapper.TableColumnName.ATTACHMENT_ID;

public class AttachmentMapper implements RowMapper<Attachment> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<Attachment> mapRow(ResultSet resultSet, String prefix) {
        try {
            Attachment attachment = new Attachment();
            attachment.setId(resultSet.getLong(prefix + ATTACHMENT_ID));
            attachment.setFile(resultSet.getString(prefix + ATTACHMENT_FILE));
            return Optional.of(attachment);
        } catch (SQLException e) {
            logger.error("Failed to fetch attachment data from resultSet", e);
            return Optional.empty();
        }
    }
}
