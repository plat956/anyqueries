package by.latushko.anyqueries.model.dao;

import by.latushko.anyqueries.exception.DaoException;

public interface AnswerDao{
    Long countTotalAnswersByUserId(Long userId) throws DaoException;
}
