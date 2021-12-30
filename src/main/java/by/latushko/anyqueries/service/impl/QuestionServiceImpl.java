package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.QuestionDao;
import by.latushko.anyqueries.model.dao.impl.QuestionDaoImpl;
import by.latushko.anyqueries.service.QuestionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class QuestionServiceImpl implements QuestionService {
    private static final Logger logger = LogManager.getLogger();
    private static QuestionService instance;

    private QuestionServiceImpl() {
    }

    public static QuestionService getInstance() {
        if (instance == null) {
            instance = new QuestionServiceImpl();
        }
        return instance;
    }

    @Override
    public List<String> findTitleLikeOrderedAndLimited(String pattern, int limit) {
        BaseDao questionDao = new QuestionDaoImpl();
        List<String> result = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                result.addAll(((QuestionDao)questionDao).findTitleLikeOrderedAndLimited(pattern, limit));
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving questions titles by pattern", e);
        }
        return result;
    }

    @Override
    public Long countTotalQuestionsByAuthorId(Long authorId) {
        BaseDao questionDao = new QuestionDaoImpl();
        Long count = 0L;
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                count = ((QuestionDao)questionDao).countTotalQuestionsByAuthorId(authorId);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving questions count by author id", e);
        }
        return count;
    }

    @Override
    public Long countTotalNotClosedQuestions() {
        BaseDao questionDao = new QuestionDaoImpl();
        Long count = 0L;
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                count = ((QuestionDao)questionDao).countTotalNotClosedQuestions();
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving not closed questions count", e);
        }
        return count;
    }

    @Override
    public Long countTotalNotClosedQuestionsByAuthorId(Long authorId) {
        BaseDao questionDao = new QuestionDaoImpl();
        Long count = 0L;
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                count = ((QuestionDao)questionDao).countTotalNotClosedQuestionsByAuthorId(authorId);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving not closed questions count by author id", e);
        }
        return count;
    }
}
