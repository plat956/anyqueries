package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.dao.*;
import by.latushko.anyqueries.model.dao.impl.AnswerDaoImpl;
import by.latushko.anyqueries.model.dao.impl.AttachmentDaoImpl;
import by.latushko.anyqueries.model.dao.impl.QuestionDaoImpl;
import by.latushko.anyqueries.model.dao.impl.RatingDaoImpl;
import by.latushko.anyqueries.model.entity.Answer;
import by.latushko.anyqueries.model.entity.Attachment;
import by.latushko.anyqueries.model.entity.Rating;
import by.latushko.anyqueries.model.entity.User;
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
    public Paginated<Answer> findPaginatedByQuestionIdOrderByCreationDateAsc(RequestPage page, Long questionId, Long userId) {
        BaseDao answerDao = new AnswerDaoImpl();
        BaseDao attachmentDao = new AttachmentDaoImpl();
        List<Answer> answers = new ArrayList<>();
        try (EntityTransaction transaction = new EntityTransaction(answerDao, attachmentDao)) {
            try {
                answers = ((AnswerDao) answerDao).findAllWithUserGradeByQuestionIdOrderByCreationDateAscLimitedTo(questionId, userId, page.getOffset(), page.getLimit());
                for (Answer a : answers) {
                    List<Attachment> attachments = ((AttachmentDao) attachmentDao).findByAnswerId(a.getId());
                    a.setAttachments(attachments);
                }
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving answers by questionId and userId with limit", e);
        }
        return new Paginated<>(answers);
    }

    @Override
    public Optional<Answer> create(Long question, String text, User user, List<Part> attachments) {
        if (question != null) {
            BaseDao answerDao = new AnswerDaoImpl();
            BaseDao questionDao = new QuestionDaoImpl();
            BaseDao attachmentDao = new AttachmentDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(answerDao, attachmentDao, questionDao)) {
                try {
                    boolean questionExists = ((QuestionDao)questionDao).existsById(question);
                    if (!questionExists) {
                        return Optional.empty();
                    }
                    Answer answer = createAnswerObject(question, user, text);
                    boolean result = answerDao.create(answer);
                    if (result) {
                        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                        for (Part p : attachments) {
                            Optional<String> fileName = attachmentService.uploadFile(p);
                            if (fileName.isPresent()) {
                                Attachment attachment = new Attachment();
                                attachment.setFile(fileName.get());
                                boolean attachmentCreated = attachmentDao.create(attachment);
                                if (attachmentCreated) {
                                    ((AnswerDao) answerDao).createAnswerAttachment(answer.getId(), attachment.getId());
                                }
                            }
                        }
                        transaction.commit();
                        logger.info("Answer {} has been created by User {} successfully", answer.getId(), user.getId());
                        return Optional.of(answer);
                    }
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Failed to create answer", e);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Answer> update(Long id, String text, List<Part> attachments) {
        if (id != null) {
            BaseDao answerDao = new AnswerDaoImpl();
            BaseDao attachmentDao = new AttachmentDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(answerDao, attachmentDao)) {
                try {
                    Optional<Answer> answerOptional = answerDao.findById(id);
                    if (answerOptional.isEmpty()) {
                        return Optional.empty();
                    }
                    Answer answer = answerOptional.get();
                    updateAnswerObject(answer, text);
                    answerOptional = answerDao.update(answer);
                    if(answerOptional.isPresent()) {
                        if (!attachments.isEmpty()) {
                            AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                            List<Attachment> oldAttachments = ((AttachmentDao) attachmentDao).findByAnswerId(id);
                            for(Attachment a: oldAttachments) {
                                attachmentDao.delete(a.getId());
                                attachmentService.deleteFile(a.getFile());
                            }
                            for (Part p : attachments) {
                                Optional<String> fileName = attachmentService.uploadFile(p);
                                if (fileName.isPresent()) {
                                    Attachment attachment = new Attachment();
                                    attachment.setFile(fileName.get());
                                    boolean attachmentCreated = attachmentDao.create(attachment);
                                    if(attachmentCreated) {
                                        ((AnswerDao) answerDao).createAnswerAttachment(answer.getId(), attachment.getId());
                                    }
                                }
                            }
                        }
                        transaction.commit();
                        logger.info("Answer {} has been updated successfully by User {}", id, answer.getAuthor().getId());
                        return Optional.of(answer);
                    }
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Failed to update answer", e);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) {
        if (id != null) {
            BaseDao answerDao = new AnswerDaoImpl();
            BaseDao attachmentDao = new AttachmentDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(answerDao, attachmentDao)) {
                try {
                    List<Attachment> attachments = ((AttachmentDao) attachmentDao).findByAnswerId(id);
                    boolean deleteAnswer = answerDao.delete(id);
                    if(deleteAnswer) {
                        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                        for(Attachment a: attachments) {
                            attachmentDao.delete(a.getId());
                            attachmentService.deleteFile(a.getFile());
                        }
                        transaction.commit();
                        logger.info("Answer {} has been deleted successfully", id);
                        return true;
                    }
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Failed to delete answer", e);
            }
        }
        return false;
    }

    @Override
    public Long countByUserId(Long userId) {
        BaseDao answerDao = new AnswerDaoImpl();
        Long count = 0L;
        try (EntityTransaction transaction = new EntityTransaction(answerDao)) {
            try {
                count = ((AnswerDao) answerDao).countByUserId(userId);
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
    public boolean changeRating(Long answerId, Boolean grade, Long userId) {
        if (answerId != null) {
            BaseDao answerDao = new AnswerDaoImpl();
            BaseDao ratingDao = new RatingDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(answerDao, ratingDao)) {
                try {
                    boolean exists = ((AnswerDao) answerDao).existsByIdAndAuthorIdNot(answerId, userId);
                    if (exists) {
                        boolean changeRatingResult;
                        Optional<Rating> ratingOptional = ((RatingDao) ratingDao).findByAnswerIdAndUserId(answerId, userId);
                        if (ratingOptional.isPresent()) {
                            Rating rating = ratingOptional.get();
                            updateRatingObject(rating, grade, answerId, userId);
                            ratingOptional = ratingDao.update(rating);
                            changeRatingResult = ratingOptional.isPresent();
                        } else {
                            Rating rating = createRatingObject(grade, answerId, userId);
                            changeRatingResult = ratingDao.create(rating);
                        }
                        if(changeRatingResult) {
                            transaction.commit();
                            logger.info("User {} set grade {} to answer {} successfully", userId, grade, answerId);
                            return true;
                        }
                    }
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Something went wrong during changing answer rating", e);
            }
        }
        return false;
    }

    @Override
    public Integer calculateRatingByAnswerId(Long answerId) {
        Integer result = 0;
        if (answerId != null) {
            BaseDao ratingDao = new RatingDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(ratingDao)) {
                try {
                    result = ((RatingDao) ratingDao).sumGradeByAnswerId(answerId);
                    transaction.commit();
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Something went wrong during calculating rating by answer id", e);
            }
        }
        return result;
    }

    @Override
    public Integer calculateLastPageByQuestionId(Long id) {
        BaseDao answerDao = new AnswerDaoImpl();
        int result = 0;
        try (EntityTransaction transaction = new EntityTransaction(answerDao)) {
            try {
                Long count = ((AnswerDao) answerDao).countByQuestionId(id);
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

    @Override
    public boolean setSolution(Long answerId, boolean solution, Long userId) {
        if (answerId != null) {
            BaseDao answerDao = new AnswerDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(answerDao)) {
                try {
                    Optional<Answer> answerOptional = ((AnswerDao) answerDao).findByIdAndQuestionAuthorIdAndQuestionClosedIs(answerId, userId, false);
                    if (answerOptional.isPresent()) {
                        Answer answer = answerOptional.get();
                        boolean updateOthers = true;
                        if (solution) {
                            updateOthers = ((AnswerDao) answerDao).updateSolutionByQuestionIdAndSolution(answer.getQuestionId(), false);
                        }
                        if (updateOthers) {
                            answer.setSolution(solution);
                            answerOptional = answerDao.update(answer);
                            if (answerOptional.isPresent()) {
                                transaction.commit();
                                logger.info("User {} changed solution mark of answer {} to {}", userId, answerId, solution);
                                return true;
                            }
                        }
                    }
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Something went wrong during changing answer solution status", e);
            }
        }
        return false;
    }

    @Override
    public boolean checkEditAccess(Long id, Long userId) {
        boolean result = false;
        if(id != null && userId != null) {
            BaseDao answerDao = new AnswerDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(answerDao)) {
                try {
                    result = ((AnswerDao) answerDao).existsByIdAndAuthorIdAndQuestionClosedIs(id, userId, false);
                    transaction.commit();
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Failed to check if user has answer edit access", e);
            }
        }
        return result;
    }

    @Override
    public boolean checkDeleteAccess(Long id, User user) {
        boolean result = false;
        if (id != null) {
            if (user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.MODERATOR) {
                result = true;
            } else {
                BaseDao answerDao = new AnswerDaoImpl();
                try (EntityTransaction transaction = new EntityTransaction(answerDao)) {
                    try {
                        result = ((AnswerDao) answerDao).existsByIdAndAuthorIdAndQuestion(id, user.getId());
                        transaction.commit();
                        result = true;
                    } catch (DaoException e) {
                        transaction.rollback();
                    }
                } catch (EntityTransactionException e) {
                    logger.error("Failed to check if user has answer delete access", e);
                }
            }
        }
        return result;
    }

    private Answer createAnswerObject(Long questionId, User user, String text) {
        Answer answer = new Answer();
        answer.setQuestionId(questionId);
        answer.setSolution(false);
        answer.setAuthor(user);
        answer.setText(text);
        answer.setCreationDate(LocalDateTime.now());
        return answer;
    }

    private Rating createRatingObject(boolean grade, Long answerId, Long userId) {
        Rating rating = new Rating();
        rating.setGrade(grade ? 1 : -1);
        rating.setAnswerId(answerId);
        rating.setUserId(userId);
        return rating;
    }

    private void updateAnswerObject(Answer answer, String text) {
        answer.setText(text);
        answer.setEditingDate(LocalDateTime.now());
    }

    private void updateRatingObject(Rating rating, boolean grade, Long answerId, Long userId) {
        rating.setGrade(grade ? 1 : -1);
        rating.setAnswerId(answerId);
        rating.setUserId(userId);
    }
}