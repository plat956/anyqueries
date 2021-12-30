package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;

import java.util.List;

public interface QuestionDao {
    List<String> findTitleLikeOrderedAndLimited(String pattern, int limit) throws DaoException;
}
