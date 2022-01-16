package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.dao.AttachmentDao;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.CategoryDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.impl.AttachmentDaoImpl;
import by.latushko.anyqueries.model.dao.impl.CategoryDaoImpl;
import by.latushko.anyqueries.model.entity.Attachment;
import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.service.AttachmentService;
import by.latushko.anyqueries.service.CategoryService;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
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

    @Override
    public Paginated<Category> findPaginatedByNameContainsOrderByNameAsc(RequestPage page, String namePattern) {
        BaseDao categoryDao = new CategoryDaoImpl();
        List<Category> categories = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(categoryDao)) {
            try {
                categories = ((CategoryDao)categoryDao).findLimitedByNameLikeOrderByNameAsc(page.getOffset(), page.getLimit(), namePattern);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving categories with requested limit", e);
        }
        return new Paginated<>(categories);
    }

    @Override
    public boolean existsByName(String name) {
        BaseDao categoryDao = new CategoryDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(categoryDao)) {
            try {
                result = ((CategoryDao)categoryDao).existsByName(name);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking category existing by name", e);
        }
        return result;
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        BaseDao categoryDao = new CategoryDaoImpl();
        boolean result = false;
        try (EntityTransaction transaction = new EntityTransaction(categoryDao)) {
            try {
                result = ((CategoryDao)categoryDao).existsByNameAndIdNot(name, id);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during checking category existing by name and id not", e);
        }
        return result;
    }

    @Override
    public Optional<Category> create(String name, String color) {
        BaseDao categoryDao = new CategoryDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(categoryDao)) {
            try {
                Category category = new Category();
                category.setName(name);
                category.setColor(color);
                categoryDao.create(category);
                transaction.commit();
                return Optional.of(category);
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to create category", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Category> findById(Long id) {
        Optional<Category> categoryOptional = Optional.empty();
        if(id != null) {
            BaseDao categoryDao = new CategoryDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(categoryDao)) {
                try {
                    categoryOptional = categoryDao.findById(id);
                    transaction.commit();
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Something went wrong during retrieving category by id", e);
            }
        }
        return categoryOptional;
    }

    @Override
    public boolean update(Long id, String name, String color) {
        BaseDao categoryDao = new CategoryDaoImpl();

        try (EntityTransaction transaction = new EntityTransaction(categoryDao)) {
            try {
                Optional<Category> categoryOptional = categoryDao.findById(id);
                if(categoryOptional.isEmpty())  {
                    throw new EntityTransactionException("Failed to update category. Category with id " + id + " does not exist"); //todo: or return false?
                }
                Category category = categoryOptional.get();
                category.setName(name);
                category.setColor(color);
                categoryDao.update(category);
                transaction.commit();
                return true;
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to update category", e);
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        boolean result = false;
        BaseDao categoryDao = new CategoryDaoImpl();
        BaseDao attachmentDao = new AttachmentDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(categoryDao, attachmentDao)) {
            try {
                List<Attachment> attachments = ((AttachmentDaoImpl) attachmentDao).findByCategoryId(id);
                result = ((AttachmentDao)attachmentDao).deleteByCategoryId(id);
                if(result) {
                    AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                    result = attachmentService.deleteAttachmentsFiles(attachments);
                    if(result) {
                        result = categoryDao.delete(id);
                        transaction.commit();
                    }
                }
                //todo?? call rollback if commit is not reached?
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to delete category", e);
        }
        return result;
    }

    @Override
    public List<String> findNameByNameContainsOrderByNameAscLimitedTo(String namePattern, int limit) {
        BaseDao categoryDao = new CategoryDaoImpl();
        List<String> names = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(categoryDao)) {
            try {
                names = ((CategoryDao)categoryDao).findNameByNameLikeOrderedAndLimited(namePattern, limit);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving categories names by pattern", e);
        }
        return names;
    }
}
