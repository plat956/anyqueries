package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Attachment;

import java.util.List;

public interface AttachmentDao {
    boolean deleteByQuestionId(Long id) throws DaoException;
    boolean deleteByCategoryId(Long id) throws DaoException;
    boolean deleteByAnswerId(Long id) throws DaoException;
    boolean deleteByQuestionAuthorId(Long id) throws DaoException;
    boolean deleteByAnswerAuthorId(Long id) throws DaoException;
    List<Attachment> findByQuestionId(Long id) throws DaoException;
    List<Attachment> findByCategoryId(Long id) throws DaoException;
    List<Attachment> findByAnswerId(Long id) throws DaoException;
    List<Attachment> findByUserId(Long id) throws DaoException;
}
