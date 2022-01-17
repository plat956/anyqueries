package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerDao{
    List<Answer> findAllWithUserGradeByQuestionIdOrderByCreationDateAscLimitedTo(Long questionId,
                                                                                 Long userId, int offset, int limit) throws DaoException;
    Optional<Answer> findByIdAndQuestionAuthorIdAndQuestionClosedIs(Long answerId, Long userId, boolean closed) throws DaoException;
    boolean existsByIdAndAuthorIdNot(Long answerId, Long authorId) throws DaoException;
    boolean existsByIdAndAuthorIdAndQuestionClosedIs(Long id, Long authorId, boolean closed) throws DaoException;
    boolean existsByIdAndAuthorIdAndQuestion(Long id, Long authorId) throws DaoException;
    Long countByUserId(Long userId) throws DaoException;
    Long countByQuestionId(Long id) throws DaoException;
    boolean createAnswerAttachment(Long answerId, Long attachmentId) throws DaoException;
    boolean updateSolutionByQuestionIdAndSolution(Long questionId, Boolean solution) throws DaoException;
}
