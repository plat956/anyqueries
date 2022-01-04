package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;

import java.util.List;

public interface QuestionDao {
    List<String> findTitleLikeOrderedAndLimited(String likePattern, int limit) throws DaoException;
    Long countTotalByAuthorId(Long userId) throws DaoException;
    Long countTotalNotClosed() throws DaoException;
    Long countTotalNotClosedByAuthorId(Long userId) throws DaoException;
    boolean createQuestionAttachment(Long questionId, Long attachmentId) throws DaoException;
}
