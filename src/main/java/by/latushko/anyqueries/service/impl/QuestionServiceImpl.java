package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.dao.AttachmentDao;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.QuestionDao;
import by.latushko.anyqueries.model.dao.impl.AttachmentDaoImpl;
import by.latushko.anyqueries.model.dao.impl.CategoryDaoImpl;
import by.latushko.anyqueries.model.dao.impl.QuestionDaoImpl;
import by.latushko.anyqueries.model.entity.Attachment;
import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.AttachmentService;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<String> findTitleByTitleContainsAndCategoryIdAndAuthorIdOrderByTitleAscLimitedTo(String pattern, Long categoryId,
                                                                                                 Long userId, int limit) {
        BaseDao questionDao = new QuestionDaoImpl();
        List<String> titles = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                titles = ((QuestionDao)questionDao).findTitleByTitleContainsAndCategoryIdAndAuthorIdOrderByTitleAscLimitedTo(pattern, categoryId, userId, limit);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving questions titles by title containing, categoryId and userId with limit", e);
        }
        return titles;
    }

    @Override
    public Paginated<Question> findPaginatedByResolvedAndAuthorIdAndCategoryIdAndTitleContainsOrderByNewest(RequestPage page,
                                                  boolean resolved, Long authorId, Long categoryId, String titlePattern, boolean newestFirst) {
        BaseDao questionDao = new QuestionDaoImpl();
        List<Question> questions = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                questions = ((QuestionDao)questionDao).findByResolvedAndAuthorIdAndCategoryIdAndTitleContainsOrderByNewestLimitetTo(resolved, newestFirst,
                        authorId, categoryId, titlePattern, page.getOffset(), page.getLimit());
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving questions by title containing and resolved, categoryId with limit", e);
        }
        return new Paginated<>(questions);
    }

    @Override
    public Optional<Question> findById(Long id) {
        Optional<Question> question = Optional.empty();
        if(id != null) {
            BaseDao questionDao = new QuestionDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
                try {
                    question = questionDao.findById(id);
                    transaction.commit();
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Something went wrong during retrieving question by id", e);
            }
        }
        return question;
    }

    @Override
    public Long countByAuthorId(Long authorId) {
        BaseDao questionDao = new QuestionDaoImpl();
        Long count = 0L;
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                count = ((QuestionDao)questionDao).countByAuthorId(authorId);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving questions count by author id", e);
        }
        return count;
    }

    @Override
    public Long countNotClosed() {
        BaseDao questionDao = new QuestionDaoImpl();
        Long count = 0L;
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                count = ((QuestionDao)questionDao).countNotClosed();
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving not closed questions count", e);
        }
        return count;
    }

    @Override
    public Long countNotClosedByAuthorId(Long authorId) {
        BaseDao questionDao = new QuestionDaoImpl();
        Long count = 0L;
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                count = ((QuestionDao)questionDao).countNotClosedByAuthorId(authorId);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving not closed questions count by author id", e);
        }
        return count;
    }

    @Override
    public Optional<Question> create(Long categoryId, String title, String text, User author, List<Part> attachments) {
        BaseDao questionDao = new QuestionDaoImpl();
        BaseDao categoryDao = new CategoryDaoImpl();
        BaseDao attachmentDao = new AttachmentDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(questionDao, categoryDao, attachmentDao)) {
            try {
                Optional<Category> category = categoryDao.findById(categoryId);
                if(category.isEmpty())  {
                    return Optional.empty();
                }
                Question question = createQuestionObject(category.get(), title, text, author);
                boolean result = questionDao.create(question);
                if(result) {
                    AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                    for (Part p : attachments) {
                        Optional<String> fileName = attachmentService.uploadFile(p);
                        if(fileName.isPresent()) {
                            Attachment attachment = new Attachment();
                            attachment.setFile(fileName.get());
                            boolean attachmentCreated = attachmentDao.create(attachment);
                            if (attachmentCreated) {
                                ((QuestionDao) questionDao).createQuestionAttachment(question.getId(), attachment.getId());
                            }
                        }
                    }
                    transaction.commit();
                    return Optional.of(question);
                }
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to create question", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id, User initiator) {
        boolean result = false;
        if(id != null && checkManagementAccess(id, initiator)) {
            BaseDao questionDao = new QuestionDaoImpl();
            BaseDao attachmentDao = new AttachmentDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(questionDao, attachmentDao)) {
                try {
                    List<Attachment> attachments = ((AttachmentDaoImpl) attachmentDao).findByQuestionId(id);
                    boolean deleteByQuestion = ((AttachmentDao) attachmentDao).deleteByQuestionId(id);
                    if (deleteByQuestion) {
                        boolean deleteQuestion = questionDao.delete(id);
                        if(deleteQuestion) {
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
                logger.error("Failed to delete question", e);
            }
        }
        return result;
    }

    @Override
    public boolean update(Long questionId, Long categoryId, String title, String text, List<Part> attachments) {
        if(questionId != null) {
            BaseDao questionDao = new QuestionDaoImpl();
            BaseDao categoryDao = new CategoryDaoImpl();
            BaseDao attachmentDao = new AttachmentDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(questionDao, categoryDao, attachmentDao)) {
                try {
                    Optional<Category> category = categoryDao.findById(categoryId);
                    if (category.isEmpty()) {
                        return false;
                    }
                    Optional<Question> questionOptional = questionDao.findById(questionId);
                    if (questionOptional.isEmpty()) {
                        return false;
                    }
                    Question question = updateQuestionObject(questionOptional.get(), category.get(), title, text);
                    questionOptional = questionDao.update(question);
                    if(questionOptional.isPresent()) {
                        if (!attachments.isEmpty()) {
                            AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                            List<Attachment> oldAttachments = ((AttachmentDaoImpl) attachmentDao).findByQuestionId(questionId);
                            boolean deleteOldAttachments = ((AttachmentDao) attachmentDao).deleteByQuestionId(questionId);
                            if(deleteOldAttachments) {
                                attachmentService.deleteAttachmentsFiles(oldAttachments);
                                for (Part p : attachments) {
                                    Optional<String> fileName = attachmentService.uploadFile(p);
                                    if (fileName.isPresent()) {
                                        Attachment attachment = new Attachment();
                                        attachment.setFile(fileName.get());
                                        boolean attachmentCreated = attachmentDao.create(attachment);
                                        if(attachmentCreated) {
                                            ((QuestionDao) questionDao).createQuestionAttachment(question.getId(), attachment.getId());
                                        }
                                    }
                                }
                            }
                        }
                        transaction.commit();
                        return true;
                    }
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Failed to update question", e);
            }
        }
        return false;
    }

    @Override
    public boolean checkManagementAccess(Long questionId, User user) {
        if (questionId == null || user == null) {
            return false;
        }
        if(user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.MODERATOR) {
            return true;
        }
        Optional<Long> authorId = findAuthorIdById(questionId);
        return authorId.isPresent() && authorId.get().equals(user.getId()); //todo сделать все на sql
    }

    @Override
    public boolean changeStatus(Long id, boolean status) {
        if(id != null) {
            BaseDao questionDao = new QuestionDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
                try {
                    Optional<Question> questionOptional = questionDao.findById(id);
                    if (questionOptional.isEmpty()) {
                        return false;
                    }
                    Question question = questionOptional.get();
                    question.setClosed(status);
                    questionOptional = questionDao.update(question);
                    if(questionOptional.isPresent()) {
                        transaction.commit();
                        return true;
                    }
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Failed to change question status", e);
            }
        }
        return false;
    }































    private Optional<Long> findAuthorIdById(Long id) {
        BaseDao questionDao = new QuestionDaoImpl();
        Optional<Long> authorId = Optional.empty();
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                authorId = ((QuestionDao)questionDao).findAuthorIdById(id);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving authorId by id", e);
        }
        return authorId;
    }

    private Question createQuestionObject(Category category, String title, String text, User author) {
        Question question = new Question();
        question.setCategory(category);
        question.setTitle(title);
        question.setText(text);
        question.setAuthor(author);
        question.setCreationDate(LocalDateTime.now());
        question.setClosed(false);
        return question;
    }

    private Question updateQuestionObject(Question question, Category category, String title, String text) {
        question.setCategory(category);
        question.setTitle(title);
        question.setText(text);
        question.setEditingDate(LocalDateTime.now());
        return question;
    }
}
