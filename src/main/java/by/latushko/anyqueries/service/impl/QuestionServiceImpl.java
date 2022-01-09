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
    public List<String> findTitleLikeOrderedAndLimited(String pattern, int limit) {
        BaseDao questionDao = new QuestionDaoImpl();
        List<String> titles = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                titles = ((QuestionDao)questionDao).findTitleLikeOrderedAndLimited(pattern, limit);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving questions titles by pattern", e);
        }
        return titles;
    }

    @Override
    public Long countTotalByAuthorId(Long authorId) {
        BaseDao questionDao = new QuestionDaoImpl();
        Long count = 0L;
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                count = ((QuestionDao)questionDao).countTotalByAuthorId(authorId);
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
    public Long countTotalNotClosed() {
        BaseDao questionDao = new QuestionDaoImpl();
        Long count = 0L;
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                count = ((QuestionDao)questionDao).countTotalNotClosed();
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
    public Long countTotalNotClosedByAuthorId(Long authorId) {
        BaseDao questionDao = new QuestionDaoImpl();
        Long count = 0L;
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                count = ((QuestionDao)questionDao).countTotalNotClosedByAuthorId(authorId);
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

    @Override
    public Paginated<Question> findByQueryParametersOrderByNewest(RequestPage page, boolean resolved, boolean newestFirst, Long authorId, Long categoryId, String titlePattern) {
        BaseDao questionDao = new QuestionDaoImpl();
        List<Question> questions = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(questionDao)) {
            try {
                questions = ((QuestionDao)questionDao).findLimitedByResolvedAndAuthorIdAndCategoryIdAndTitleLikeOrderByNewest(page.getOffset(), page.getLimit(), resolved, newestFirst, authorId, categoryId, titlePattern);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving questions with requested limit and parameters", e);
        }
        return new Paginated<>(questions);
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
