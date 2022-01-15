package by.latushko.anyqueries.model.mapper.impl;

import by.latushko.anyqueries.model.entity.Attachment;
import by.latushko.anyqueries.model.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static by.latushko.anyqueries.model.mapper.TableColumnName.ATTACHMENT_FILE;
import static by.latushko.anyqueries.model.mapper.TableColumnName.ATTACHMENT_ID;

public class AttachmentMapper implements RowMapper<Attachment> {
    @Override
    public Optional<Attachment> mapRow(ResultSet resultSet, String prefix) {
        try {
            Attachment attachment = new Attachment();
            attachment.setId(resultSet.getLong(prefix + ATTACHMENT_ID));
            attachment.setFile(resultSet.getString(prefix + ATTACHMENT_FILE));
            return Optional.of(attachment);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
