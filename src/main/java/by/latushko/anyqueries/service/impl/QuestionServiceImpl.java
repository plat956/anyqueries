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
    public List<String> findTitleByTitleContainsAndCategoryIdAndAuthorIdOrderByTitleAscLimitedTo(String pattern, Long categoryId, Long userId, int limit) {
        BaseDao questionDao = new QuestionDaoImpl();
        List<String> titles = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                titles = ((QuestionDao)questionDao).findTitleByTitleLikeAndCategoryIdAndAuthorIdLikeOrderedAndLimited(pattern, categoryId, userId, limit);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving questions titles by pattern and parameters", e);
        }
        return titles;
    }

    @Override
    public Long countByAuthorId(Long authorId) {
        BaseDao questionDao = new QuestionDaoImpl();
        Long count = 0L;
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                count = ((QuestionDao)questionDao).countTotalByAuthorId(authorId);
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
                count = ((QuestionDao)questionDao).countTotalNotClosed();
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
                count = ((QuestionDao)questionDao).countTotalNotClosedByAuthorId(authorId);
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
                    throw new EntityTransactionException("Failed to create question. Category with id " + categoryId + " does not exist"); //todo: or return false?
                }
                Question question = createNewQuestion(category.get(), title, text, author);
                questionDao.create(question);

                AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                for(Part p: attachments) {
                    Optional<String> fileName = attachmentService.uploadFile(p);
                    if(fileName.isEmpty()) {
                        throw new EntityTransactionException("Failed to upload file."); //todo: or return false?
                    }
                    Attachment attachment = new Attachment();
                    attachment.setFile(fileName.get());
                    attachmentDao.create(attachment);
                    ((QuestionDao) questionDao).createQuestionAttachment(question.getId(), attachment.getId());
                }

                transaction.commit();
                return Optional.of(question);
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to create question", e);
        }
        return Optional.empty();
    }

    @Override
    public Paginated<Question> findPaginatedByQueryParametersOrderByNewest(RequestPage page, boolean resolved, Long authorId, Long categoryId, String titlePattern, boolean newestFirst) {
        BaseDao questionDao = new QuestionDaoImpl();
        List<Question> questions = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                questions = ((QuestionDao)questionDao).findLimitedByResolvedAndAuthorIdAndCategoryIdAndTitleLikeOrderByNewest(page.getOffset(), page.getLimit(), resolved, newestFirst, authorId, categoryId, titlePattern);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving questions with requested limit and parameters", e);
        }
        return new Paginated<>(questions);
    }

    @Override
    public boolean delete(Long id, User initiator) {
        if(!checkManagementAccess(id, initiator)) {
            return false;
        }
        boolean result = false;
        BaseDao questionDao = new QuestionDaoImpl();
        BaseDao attachmentDao = new AttachmentDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(questionDao, attachmentDao)) {
            try {
                List<Attachment> attachments = ((AttachmentDaoImpl) attachmentDao).findByQuestionId(id);
                result = ((AttachmentDao)attachmentDao).deleteByQuestionId(id);
                if(result) {
                    AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                    result = attachmentService.deleteAttachmentsFiles(attachments);
                    if(result) {
                        result = questionDao.delete(id);
                        transaction.commit();
                    }
                }
                //todo?? call rollback if commit is not reached?
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to delete question", e);
        }
        return result;
    }

    @Override
    public boolean checkManagementAccess(Long questionId, User user) {
        if (user == null) {
            return false;
        }
        if(user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.MODERATOR) {
            return true;
        }
        Optional<Long> authorId = findAuthorIdById(questionId);
        return authorId.isPresent() && authorId.get().equals(user.getId());
    }

    @Override
    public Optional<Question> findById(Long id) {
        Optional<Question> questionOptional = Optional.empty();
        if(id != null) {
            BaseDao questionDao = new QuestionDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
                try {
                    questionOptional = questionDao.findById(id);
                    transaction.commit();
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Something went wrong during retrieving question by id", e);
            }
        }
        return questionOptional;
    }

    @Override
    public boolean update(Long questionId, Long categoryId, String title, String text, List<Part> attachments) {
        BaseDao questionDao = new QuestionDaoImpl();
        BaseDao categoryDao = new CategoryDaoImpl();
        BaseDao attachmentDao = new AttachmentDaoImpl();

        try (EntityTransaction transaction = new EntityTransaction(questionDao, categoryDao, attachmentDao)) {
            try {
                Optional<Category> category = categoryDao.findById(categoryId);
                if(category.isEmpty())  {
                    throw new EntityTransactionException("Failed to update question. Category with id " + categoryId + " does not exist"); //todo: or return false?
                }
                Optional<Question> questionOptional = questionDao.findById(questionId);
                if(questionOptional.isEmpty()) {
                    throw new EntityTransactionException("Failed to update question. Question with id " + questionId + " does not exist"); //todo: or return false?
                }
                Question question = updateQuestion(questionOptional.get(), category.get(), title, text);
                questionDao.update(question);

                if(!attachments.isEmpty()) {
                    AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                    List<Attachment> oldAttachments = ((AttachmentDaoImpl) attachmentDao).findByQuestionId(questionId);
                    ((AttachmentDao)attachmentDao).deleteByQuestionId(questionId);
                    attachmentService.deleteAttachmentsFiles(oldAttachments);
                    for (Part p : attachments) {
                        Optional<String> fileName = attachmentService.uploadFile(p);
                        if (fileName.isEmpty()) {
                            throw new EntityTransactionException("Failed to upload file."); //todo: or return false?
                        }
                        Attachment attachment = new Attachment();
                        attachment.setFile(fileName.get());
                        attachmentDao.create(attachment);
                        ((QuestionDao) questionDao).createQuestionAttachment(question.getId(), attachment.getId());
                    }
                }

                transaction.commit();
                return true;
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to update question", e);
        }
        return false;
    }

    @Override
    public boolean changeStatus(Long id, boolean status) {
        BaseDao questionDao = new QuestionDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                Optional<Question> questionOptional = questionDao.findById(id);
                if(questionOptional.isEmpty()) {
                    throw new EntityTransactionException("Failed to change question status. Question with id " + id + " does not exist"); //todo: or return false?
                }
                Question question = questionOptional.get();
                question.setClosed(status);
                questionDao.update(question);
                transaction.commit();
                return true;
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to change question status", e);
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

    private Question createNewQuestion(Category category, String title, String text, User author) {
        Question question = new Question();
        question.setCategory(category);
        question.setTitle(title);
        question.setText(text);
        question.setAuthor(author);
        question.setCreationDate(LocalDateTime.now());
        question.setClosed(false);
        return question;
    }

    private Question updateQuestion(Question question, Category category, String title, String text) {
        question.setCategory(category);
        question.setTitle(title);
        question.setText(text);
        question.setEditingDate(LocalDateTime.now());
        return question;
    }
}
