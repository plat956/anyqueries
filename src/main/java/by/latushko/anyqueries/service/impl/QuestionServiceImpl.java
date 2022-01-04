package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
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
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to create question", e);
        }
        return Optional.empty();
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
}
