package by.latushko.anyqueries.model.dao.impl;

import by.latushko.anyqueries.model.dao.QuestionDao;
import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.exception.DaoException;

import java.util.List;
import java.util.Optional;

public class QuestionDaoImpl implements QuestionDao {
    @Override
    public List<Question> findAll() throws DaoException {
        return null;
    }

    @Override
    public Optional<Question> findById(Long id) throws DaoException {
        return Optional.empty();
    }

    @Override
    public boolean create(Question question) throws DaoException {
        return false;
    }

    @Override
    public Optional<Question> update(Question question) throws DaoException {
        return Optional.empty();
    }

    @Override
    public boolean delete(Question question) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        return false;
    }
}
