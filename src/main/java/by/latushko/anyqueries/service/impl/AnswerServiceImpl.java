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
    public Long countByUserId(Long userId) {
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
        if(answerId != null) {
            BaseDao answerDao = new AnswerDaoImpl();
            BaseDao ratingDao = new RatingDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(answerDao, ratingDao)) {
                try {
                    boolean exists = ((AnswerDao) answerDao).existsByIdAndAuthorIdNot(answerId, userId);
                    if (exists) {
                        Optional<Rating> ratingOptional = ((RatingDao) ratingDao).findByAnswerIdAndUserId(answerId, userId);
                        if (ratingOptional.isPresent()) {
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
        }
        return false;
    }

    @Override
    public Integer calculateRatingByAnswerId(Long answerId) {
        Integer result = 0;
        if(answerId != null) {
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
    public boolean setSolution(Long answerId, boolean solution, Long userId) {
        if(answerId != null) {
            BaseDao answerDao = new AnswerDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(answerDao)) {
                try {
                    Optional<Answer> answerOptional = ((AnswerDao) answerDao).findByIdAndQuestionAuthorId(answerId, userId);
                    if (answerOptional.isPresent()) {
                        Answer answer = answerOptional.get();
                        boolean updateOthers;
                        if (solution) {
                            updateOthers = ((AnswerDao) answerDao).updateSolutionByQuestionIdAndSolution(answer.getQuestionId(), false);
                        } else {
                            updateOthers = true;
                        }
                        if (updateOthers) {
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
        }
        return false;
    }

    @Override
    public Optional<Answer> create(Long question, String text, User user, List<Part> attachments) {
        if(question != null) {
            BaseDao answerDao = new AnswerDaoImpl();
            BaseDao attachmentDao = new AttachmentDaoImpl();

            try (EntityTransaction transaction = new EntityTransaction(answerDao, attachmentDao)) {
                try {
                    //todo check if question exists & access?+++
                    Answer answer = new Answer();
                    answer.setQuestionId(question);
                    answer.setSolution(false);
                    answer.setAuthor(user);
                    answer.setText(text);
                    answer.setCreationDate(LocalDateTime.now());

                    answerDao.create(answer);

                    AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                    for (Part p : attachments) {
                        Optional<String> fileName = attachmentService.uploadFile(p);
                        if (fileName.isEmpty()) {
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

    @Override
    public boolean delete(Long id) {
        boolean result = false;
        if(id != null) {
            //todo check access to del as well
            BaseDao answerDao = new AnswerDaoImpl();
            BaseDao attachmentDao = new AttachmentDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(answerDao, attachmentDao)) {
                try {
                    List<Attachment> attachments = ((AttachmentDaoImpl) attachmentDao).findByAnswerId(id);
                    result = ((AttachmentDao) attachmentDao).deleteByAnswerId(id);
                    if (result) {
                        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                        result = attachmentService.deleteAttachmentsFiles(attachments);
                        if (result) {
                            result = answerDao.delete(id);
                            transaction.commit();
                        }
                    }
                    //todo?? call rollback if commit is not reached?
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Failed to delete answer", e);
            }
        }
        return result;
    }

    @Override
    public Optional<Answer> update(Long id, String text, List<Part> attachments) {
        if(id != null) {
            BaseDao answerDao = new AnswerDaoImpl();
            BaseDao attachmentDao = new AttachmentDaoImpl();

            try (EntityTransaction transaction = new EntityTransaction(answerDao, attachmentDao)) {
                try {
                    Optional<Answer> answerOptional = answerDao.findById(id);
                    if (answerOptional.isEmpty()) {
                        throw new EntityTransactionException("Failed to update answer. Answer with id " + id + " does not exist"); //todo: or return false?
                    }
                    Answer answer = answerOptional.get();
                    answer.setText(text);
                    answer.setEditingDate(LocalDateTime.now());
                    answerDao.update(answer);


                    if (!attachments.isEmpty()) {
                        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
                        List<Attachment> oldAttachments = ((AttachmentDaoImpl) attachmentDao).findByAnswerId(id);
                        ((AttachmentDao) attachmentDao).deleteByAnswerId(id);
                        attachmentService.deleteAttachmentsFiles(oldAttachments);
                        for (Part p : attachments) {
                            Optional<String> fileName = attachmentService.uploadFile(p);
                            if (fileName.isEmpty()) {
                                throw new EntityTransactionException("Failed to upload file."); //todo: or return false?
                            }
                            Attachment attachment = new Attachment();
                            attachment.setFile(fileName.get());
                            attachmentDao.create(attachment);
                            ((AnswerDao) answerDao).createAnswerAttachment(answer.getId(), attachment.getId());
                        }
                    }

                    transaction.commit();
                    return Optional.of(answer);
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Failed to update answer", e);
            }
        }
        return Optional.empty();
    }
}
