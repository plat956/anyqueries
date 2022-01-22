package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Question;

import java.util.List;

/**
 * The Question Data Access Object interface.
 * Describes signatures of this DAO implementation methods
 * Contains abstract methods to create extended CRUD operations for Question entity
 */
public interface QuestionDao {
    /**
     * Find title by title contains and category id and author id order by title asc limited to.
     *
     * @param likePattern the question title like pattern
     * @param categoryId  the category id
     * @param userId      the user id
     * @param limit       the limit
     * @return the list of found question titles
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<String> findTitleByTitleContainsAndCategoryIdAndAuthorIdOrderByTitleAscLimitedTo(String likePattern, Long categoryId,
                                                                                          Long userId, int limit) throws DaoException;

    /**
     * Find by resolved and author id and category id and title contains order by newest limited to.
     *
     * @param resolved     the resolved status
     * @param newestFirst  the newest first sorting flag
     * @param authorId     the author id
     * @param categoryId   the category id
     * @param titlePattern the question title pattern
     * @param offset       the offset
     * @param limit        the limit
     * @return the list of found questions
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<Question> findByResolvedAndAuthorIdAndCategoryIdAndTitleContainsOrderByNewestLimitedTo(boolean resolved, boolean
            newestFirst, Long authorId, Long categoryId, String titlePattern, int offset, int limit) throws DaoException;

    /**
     * Exists by id.
     *
     * @param id the question id
     * @return the boolean, true if question exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsById(Long id) throws DaoException;

    /**
     * Exists by id and author id and closed is.
     *
     * @param id       the question id
     * @param authorId the author id
     * @param closed   the closed status
     * @return the boolean, true if question exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByIdAndAuthorIdAndClosedIs(Long id, Long authorId, boolean closed) throws DaoException;

    /**
     * Exists by id and author id.
     *
     * @param id       the question id
     * @param authorId the author id
     * @return the boolean, true if question exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByIdAndAuthorId(Long id, Long authorId) throws DaoException;

    /**
     * Count by author id.
     *
     * @param userId the author id
     * @return the long, count of found questions
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Long countByAuthorId(Long userId) throws DaoException;

    /**
     * Count not closed.
     *
     * @return the long, count of found questions
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Long countNotClosed() throws DaoException;

    /**
     * Count not closed by author id.
     *
     * @param userId the author id
     * @return the long, count of found questions
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Long countNotClosedByAuthorId(Long userId) throws DaoException;

    /**
     * Create question attachment.
     *
     * @param questionId   the question id
     * @param attachmentId the attachment id
     * @return the boolean, true if the question was created successfully, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean createQuestionAttachment(Long questionId, Long attachmentId) throws DaoException;
}
