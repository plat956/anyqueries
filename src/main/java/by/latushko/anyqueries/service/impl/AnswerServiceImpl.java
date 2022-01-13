package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.dao.*;
import by.latushko.anyqueries.model.dao.impl.AnswerDaoImpl;
import by.latushko.anyqueries.model.dao.impl.AttachmentDaoImpl;
import by.latushko.anyqueries.model.dao.impl.RatingDaoImpl;
import by.latushko.anyqueries.model.entity.Answer;
import by.latushko.anyqueries.model.entity.Attachment;
import by.latushko.anyqueries.model.entity.Rating;
import by.latushko.anyqueries.service.AnswerService;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnswerServiceImpl implements AnswerService {
    private static final Logger logger = LogManager.getLogger();
    private static AnswerService instance;

    private AnswerServiceImpl() {
    }

    public static AnswerService getInstance() {
        if (instance == null) {
            instance = new AnswerServiceImpl();
        }
        return instance;
    }

    @Override
    public Long countTotalAnswersByUserId(Long userId) {
        BaseDao answerDao = new AnswerDaoImpl();
        Long count = 0L;
        try (EntityTransaction transaction = new EntityTransaction(answerDao)) {
            try {
                count = ((AnswerDao)answerDao).countTotalByUserId(userId);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving answers count by user id", e);
        }
        return count;
    }

    @Override
    public Paginated<Answer> findPaginatedByQuestionIdOrderByCreationDateAsc(RequestPage page, Long questionId, Long userId) {
        BaseDao answerDao = new AnswerDaoImpl();
        BaseDao attachmentDao = new AttachmentDaoImpl();
        List<Answer> answers = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(answerDao, attachmentDao)) {
            try {
                answers = ((AnswerDao)answerDao).findLimitedByQuestionIdOrderByCreationDateAsc(page.getOffset(), page.getLimit(), questionId, userId);
                for (Answer a: answers) {
                    List<Attachment> attachments = ((AttachmentDao)attachmentDao).findByAnswerId(a.getId());
                    a.setAttachments(attachments);
                }
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving answers with requested limit", e);
        }
        return new Paginated<>(answers);
    }

    @Override
    public boolean changeRating(Long answerId, Boolean grade, Long userId) {
        BaseDao answerDao = new AnswerDaoImpl();
        BaseDao ratingDao = new RatingDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(answerDao, ratingDao)) {
            try {
                boolean exists = ((AnswerDao)answerDao).checkIfExistsByIdAndAuthorIdNot(answerId, userId);
                if(exists) {
                    Optional<Rating> ratingOptional = ((RatingDao)ratingDao).findByAnswerIdAndUserId(answerId, userId);
                    if(ratingOptional.isPresent()) {
                        Rating rating = ratingOptional.get();
                        rating.setGrade(grade ? 1 : -1);
                        rating.setAnswerId(answerId);
                        rating.setUserId(userId);
                        ratingDao.update(rating);
                    } else {
                        Rating rating = new Rating();
                        rating.setGrade(grade ? 1 : -1);
                        rating.setAnswerId(answerId);
                        rating.setUserId(userId);
                        ratingDao.create(rating);
                    }
                    transaction.commit();
                    return true;
                }
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during changing answer rating", e);
        }
        return false;
    }

    @Override
    public Integer calculateRatingByAnswerId(Long answerId) {
        BaseDao ratingDao = new RatingDaoImpl();
        Integer result = 0;
        try (EntityTransaction transaction = new EntityTransaction(ratingDao)) {
            try {
                result = ((RatingDao)ratingDao).sumGradeByAnswerId(answerId);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during calculating rating by answer id", e);
        }
        return result;
    }
}
