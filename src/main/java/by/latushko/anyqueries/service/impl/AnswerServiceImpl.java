package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.dao.*;
import by.latushko.anyqueries.model.dao.impl.*;
import by.latushko.anyqueries.model.entity.*;
import by.latushko.anyqueries.service.AnswerService;
import by.latushko.anyqueries.service.AttachmentService;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static by.latushko.anyqueries.util.AppProperty.APP_RECORDS_PER_PAGE;

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
                count = ((AnswerDao)answerDao).countByUserId(userId);
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

    @Override
    public boolean setSolution(Long answerId, boolean solution, Long userId) {
        BaseDao answerDao = new AnswerDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(answerDao)) {
            try {
                Optional<Answer> answerOptional = ((AnswerDao)answerDao).findByIdAndQuestionAuthorId(answerId, userId);
                if(answerOptional.isPresent()) {
                    Answer answer = answerOptional.get();
                    boolean updateOthers;
                    if(solution) {
                        updateOthers = ((AnswerDao)answerDao).updateSolutionByQuestionIdAndSolution(answer.getQuestionId(), false);
                    } else {
                        updateOthers = true;
                    }
                    if(updateOthers) {
                        answer.setSolution(solution);
                        answerDao.update(answer);
                        transaction.commit();
                        return true;
                    }
                }
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during setting answer solution", e);
        }
        return false;
    }

    @Override
    public Optional<Answer> create(Long question, String text, User user, List<Part> attachments) {
        BaseDao answerDao = new AnswerDaoImpl();
        BaseDao attachmentDao = new AttachmentDaoImpl();

        try (EntityTransaction transaction = new EntityTransaction(answerDao, attachmentDao)) {
            try {
                //todo check if question exists & access?
                Answer answer = new Answer();
                answer.setQuestionId(question);
                answer.setSolution(false);
                answer.setAuthor(user);
                answer.setText(text);
                answer.setCreationDate(LocalDateTime.now());

                answerDao.create(answer);

                AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                for(Part p: attachments) {
                    Optional<String> fileName = attachmentService.uploadFile(p);
                    if(fileName.isEmpty()) {
                        throw new EntityTransactionException("Failed to upload file."); //todo: or return false?
                    }
                    Attachment attachment = new Attachment();
                    attachment.setFile(fileName.get());
                    attachmentDao.create(attachment);
                    ((AnswerDao) answerDao).createAnswerAttachment(answer.getId(), attachment.getId());
                }

                transaction.commit();
                return Optional.of(answer);
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to answer question", e);
        }
        return Optional.empty();
    }

    @Override
    public Integer calculateLastPageByQuestionId(Long id) {
        BaseDao answerDao = new AnswerDaoImpl();
        int result = 0;
        try (EntityTransaction transaction = new EntityTransaction(answerDao)) {
            try {
                Long count = ((AnswerDao)answerDao).countByQuestionId(id);
                result = (int) Math.ceil((double) count / APP_RECORDS_PER_PAGE);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during calculating answers last page", e);
        }
        return result;
    }
}
