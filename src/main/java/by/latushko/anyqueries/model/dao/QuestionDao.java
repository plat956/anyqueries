package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionDao {
    List<String> findTitleByTitleContainsAndCategoryIdAndAuthorIdOrderByTitleAscLimitedTo(String likePattern, Long categoryId, Long userId, int limit) throws DaoException;
    Long countByAuthorId(Long userId) throws DaoException;
    Long countNotClosed() throws DaoException;
    Long countNotClosedByAuthorId(Long userId) throws DaoException;
    boolean createQuestionAttachment(Long questionId, Long attachmentId) throws DaoException;
    List<Question> findByResolvedAndAuthorIdAndCategoryIdAndTitleContainsOrderByNewestLimitetTo(boolean resolved, boolean newestFirst, Long authorId, Long categoryId,
                                                                                                String titlePattern, int offset, int limit) throws DaoException;
    Optional<Long> findAuthorIdById(Long id) throws DaoException;
}
