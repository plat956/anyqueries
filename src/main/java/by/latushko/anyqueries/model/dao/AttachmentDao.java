package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Attachment;

import java.util.List;

/**
 * The Attachment Data Access Object interface.
 * Describes signatures of this DAO implementation methods
 * Contains abstract methods to create extended CRUD operations for Attachment entity
 */
public interface AttachmentDao {
    /**
     * Find by question id.
     *
     * @param id the question id
     * @return the list of found Attachment objects
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<Attachment> findByQuestionId(Long id) throws DaoException;

    /**
     * Find by category id.
     *
     * @param id the category id
     * @return the list of found Attachment objects
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<Attachment> findByCategoryId(Long id) throws DaoException;

    /**
     * Find by answer id.
     *
     * @param id the answer id
     * @return the list of found Attachment objects
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<Attachment> findByAnswerId(Long id) throws DaoException;

    /**
     * Find by user id.
     *
     * @param id the user id
     * @return the list of found Attachment objects
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<Attachment> findByUserId(Long id) throws DaoException;

    /**
     * Find all attachments by question id and attachments of this question answers as well.
     *
     * @param id the question id
     * @return the list of found Attachment objects
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<Attachment> findAllAndAnswersAttachmentsByQuestionId(Long id) throws DaoException;
}
