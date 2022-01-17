package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Attachment;

import java.util.List;

public interface AttachmentDao {
    List<Attachment> findByQuestionId(Long id) throws DaoException;
    List<Attachment> findByCategoryId(Long id) throws DaoException;
    List<Attachment> findByAnswerId(Long id) throws DaoException;
    List<Attachment> findByUserId(Long id) throws DaoException;
    List<Attachment> findAllAndAnswersAttachmentsByQuestionId(Long id) throws DaoException;
}
