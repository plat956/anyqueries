package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerDao{
    Long countByUserId(Long userId) throws DaoException;
    Long countByQuestionId(Long id) throws DaoException;
    List<Answer> findLimitedByQuestionIdOrderByCreationDateAsc(int offset, int limit, Long questionId, Long userId) throws DaoException;
    boolean checkIfExistsByIdAndAuthorIdNot(Long answerId, Long authorId) throws DaoException;
    Optional<Answer> findByIdAndQuestionAuthorId(Long answerId, Long userId) throws DaoException;
    boolean updateSolutionByQuestionIdAndSolution(Long questionId, Boolean solution) throws DaoException;
    boolean createAnswerAttachment(Long answerId, Long attachmentId) throws DaoException;
}
