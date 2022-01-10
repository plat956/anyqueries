package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Attachment;

import java.util.List;

public interface AttachmentDao {
    boolean deleteByQuestionId(Long id) throws DaoException;
    List<Attachment> findByQuestionId(Long id) throws DaoException;
}
