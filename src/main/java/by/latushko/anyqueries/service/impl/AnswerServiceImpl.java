package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.dao.AnswerDao;
import by.latushko.anyqueries.model.dao.AttachmentDao;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.impl.AnswerDaoImpl;
import by.latushko.anyqueries.model.dao.impl.AttachmentDaoImpl;
import by.latushko.anyqueries.model.entity.Answer;
import by.latushko.anyqueries.model.entity.Attachment;
import by.latushko.anyqueries.service.AnswerService;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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
}
