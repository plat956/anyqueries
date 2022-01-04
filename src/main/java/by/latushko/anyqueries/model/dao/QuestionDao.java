package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;

import java.util.List;

public interface QuestionDao {
    List<String> findTitleLikeOrderedAndLimited(String pattern, int limit) throws DaoException;
    Long countTotalQuestionsByAuthorId(Long userId) throws DaoException;
    Long countTotalNotClosedQuestions() throws DaoException;
    Long countTotalNotClosedQuestionsByAuthorId(Long userId) throws DaoException;
    boolean createQuestionAttachment(Long questionId, Long attachmentId) throws DaoException;
}
