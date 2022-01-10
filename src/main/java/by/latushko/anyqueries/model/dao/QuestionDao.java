package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.entity.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionDao {
    List<String> findTitleByTitleLikeAndCategoryIdAndAuthorIdLikeOrderedAndLimited(String likePattern, Long categoryId, Long userId, int limit) throws DaoException;
    Long countTotalByAuthorId(Long userId) throws DaoException;
    Long countTotalNotClosed() throws DaoException;
    Long countTotalNotClosedByAuthorId(Long userId) throws DaoException;
    boolean createQuestionAttachment(Long questionId, Long attachmentId) throws DaoException;
    List<Question> findLimitedByResolvedAndAuthorIdAndCategoryIdAndTitleLikeOrderByNewest(int offset, int limit, boolean resolved,
                                                                                          boolean newestFirst, Long authorId, Long categoryId,
                                                                                          String titlePattern) throws DaoException;
    Optional<Long> findAuthorIdById(Long id) throws DaoException;
}
