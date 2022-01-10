package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.CategoryDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.impl.CategoryDaoImpl;
import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LogManager.getLogger();
    private static CategoryService instance;

    private CategoryServiceImpl() {
    }

    public static CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryServiceImpl();
        }
        return instance;
    }

    @Override
    public List<Category> findTop5() {
        BaseDao categoryDao = new CategoryDaoImpl();
        List<Category> categories = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(categoryDao)) {
            try {
                categories = ((CategoryDao)categoryDao).findTop(5);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to find top 5 categories", e);
        }
        return categories;
    }

    @Override
    public List<Category> findAllOrderByNameAsc() {
        BaseDao categoryDao = new CategoryDaoImpl();
        List<Category> categories = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(categoryDao)) {
            try {
                categories = ((CategoryDao)categoryDao).findAllOrderByNameAsc();
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to find all categories", e);
        }
        return categories;
    }

    @Override
    public Optional<String> findNameById(Long id) {
        BaseDao categoryDao = new CategoryDaoImpl();
        Optional<String> name = Optional.empty();
        try (EntityTransaction transaction = new EntityTransaction(categoryDao)) {
            try {
                name = ((CategoryDao)categoryDao).findNameById(id);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving category name by id", e);
        }
        return name;
    }
}
