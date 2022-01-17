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
                categories = ((CategoryDao)categoryDao).findByNameContainsOrderByNameAscLimitedTo(namePattern, page.getOffset(), page.getLimit());
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving categories by name containing with limit", e);
        }
        return new Paginated<>(categories);
    }

    @Override
    public List<String> findNameByNameContainsOrderByNameAscLimitedTo(String namePattern, int limit) {
        BaseDao categoryDao = new CategoryDaoImpl();
        List<String> names = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(categoryDao)) {
            try {
                names = ((CategoryDao)categoryDao).findNameByNameContainsOrderByNameAscLimitedTo(namePattern, limit);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving categories names by name containing with limit", e);
        }
        return names;
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
                boolean result = categoryDao.create(category);
                if(result) {
                    transaction.commit();
                    return Optional.of(category);
                }
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to create category", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Long id, String name, String color) {
        BaseDao categoryDao = new CategoryDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(categoryDao)) {
            try {
                Optional<Category> categoryOptional = categoryDao.findById(id);
                if(categoryOptional.isEmpty())  {
                    return false;
                }
                Category category = categoryOptional.get();
                category.setName(name);
                category.setColor(color);
                categoryOptional = categoryDao.update(category);
                if(categoryOptional.isPresent()) {
                    transaction.commit();
                    return true;
                }
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
        if(id != null) {
            BaseDao categoryDao = new CategoryDaoImpl();
            BaseDao attachmentDao = new AttachmentDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(categoryDao, attachmentDao)) {
                try {
                    List<Attachment> attachments = ((AttachmentDaoImpl) attachmentDao).findByCategoryId(id);
                    boolean deleteByCategory = ((AttachmentDao) attachmentDao).deleteByCategoryId(id);
                    if (deleteByCategory) {
                        boolean deleteCategory = categoryDao.delete(id);
                        if(deleteCategory) {
                            AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                            attachmentService.deleteAttachmentsFiles(attachments);
                            transaction.commit();
                            result = true;
                        }
                    }
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Failed to delete category", e);
            }
        }
        return result;
    }
}
