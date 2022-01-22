package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Answer;

import java.util.List;
import java.util.Optional;

/**
 * The Answer Data Access Object interface.
 * Describes signatures of this DAO implementation methods
 * Contains abstract methods to create extended CRUD operations for Answer entity
 */
public interface AnswerDao{
    /**
     * Find all with user grade by question id order by creation date asc limited to.
     *
     * @param questionId the question id
     * @param userId     the user id
     * @param offset     the offset
     * @param limit      the limit
     * @return the list of found Answer objects
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    List<Answer> findAllWithUserGradeByQuestionIdOrderByCreationDateAscLimitedTo(Long questionId, Long userId, int offset, int limit) throws DaoException;

    /**
     * Find by id and question author id and question closed.
     *
     * @param answerId the answer id
     * @param userId   the user id
     * @param closed   the closed
     * @return the optional with Answer object if it was found, otherwise the empty optional object
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Optional<Answer> findByIdAndQuestionAuthorIdAndQuestionClosedIs(Long answerId, Long userId, boolean closed) throws DaoException;

    /**
     * Exists by id and author id not.
     *
     * @param answerId the answer id
     * @param authorId the author id
     * @return the boolean, true if answer exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByIdAndAuthorIdNot(Long answerId, Long authorId) throws DaoException;

    /**
     * Exists by id and author id and question closed.
     *
     * @param id       the asnwer id
     * @param authorId the author id
     * @param closed   the closed status
     * @return the boolean, true if answer exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByIdAndAuthorIdAndQuestionClosedIs(Long id, Long authorId, boolean closed) throws DaoException;

    /**
     * Exists by id and author id and question.
     *
     * @param id       the answer id
     * @param authorId the author id
     * @return the boolean, true if answer exists, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean existsByIdAndAuthorIdAndQuestion(Long id, Long authorId) throws DaoException;

    /**
     * Count by user id.
     *
     * @param userId the user id
     * @return the long, count of found answers
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Long countByUserId(Long userId) throws DaoException;

    /**
     * Count by question id.
     *
     * @param id the question id
     * @return the long, count of found answers
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    Long countByQuestionId(Long id) throws DaoException;

    /**
     * Create answer attachment.
     *
     * @param answerId     the answer id
     * @param attachmentId the attachment id
     * @return the boolean, true if answer attachment was created successfully, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean createAnswerAttachment(Long answerId, Long attachmentId) throws DaoException;

    /**
     * Update solution by question id and solution.
     *
     * @param questionId the question id
     * @param solution   the solution status
     * @return the boolean, true if answer solution status was changed successfully, otherwise false
     * @throws DaoException if the dao exception was thrown during the method calling
     */
    boolean updateSolutionByQuestionIdAndSolution(Long questionId, Boolean solution) throws DaoException;
}
