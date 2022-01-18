package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.exception.MailSenderException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.UserDao;
import by.latushko.anyqueries.model.dao.impl.UserDaoImpl;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.service.EmailService;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.util.i18n.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Optional;

import static by.latushko.anyqueries.controller.command.identity.PageUrl.CONTROLLER_URL;

public class RegistrationServiceImpl implements RegistrationService {
    private static final Logger logger = LogManager.getLogger();
    private static RegistrationService instance;

    private RegistrationServiceImpl() {
    }

    public static RegistrationService getInstance() {
        if (instance == null) {
            instance = new RegistrationServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean registerUser(String firstName, String lastName, String middleName, boolean sendLink, String email,
                                String telegram, String login, String password, MessageManager manager, StringBuffer url) {
        BaseDao userDao = new UserDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                UserService userService = UserServiceImpl.getInstance();
                User user = userService.createUserObject(firstName, lastName, middleName, email, telegram, login, password);
                boolean createResult = userDao.create(user);
                if(createResult) {
                    if (sendLink) {
                        UserHash userHash = userService.generateUserHash(user);
                        ((UserDao) userDao).createUserHash(userHash);
                        String host = getHostFromUrl(url);
                        EmailService emailService = new EmailServiceImpl(manager, host);
                        emailService.sendActivationEmail(user, userHash);
                    }
                    transaction.commit();
                    return true;
                }
            } catch (DaoException | MailSenderException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to register user", e);
        }
        return false;
    }

    @Override
    public boolean updateRegistrationData(User user, String email, String telegram, boolean sendLink, MessageManager manager, StringBuffer url) {
        BaseDao userDao = new UserDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                user.setEmail(email);
                user.setTelegram(telegram);
                Optional<User> userOptional = userDao.update(user);
                if(userOptional.isPresent()) {
                    if (sendLink) {
                        ((UserDao) userDao).deleteUserHashByUserId(user.getId());
                        UserService userService = UserServiceImpl.getInstance();
                        UserHash userHash = userService.generateUserHash(user);
                        ((UserDao) userDao).createUserHash(userHash);
                        String host = getHostFromUrl(url);
                        EmailService emailService = new EmailServiceImpl(manager, host);
                        emailService.sendActivationEmail(user, userHash);
                    }
                    transaction.commit();
                    return true;
                }
            } catch (DaoException | MailSenderException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to update user registration data", e);
        }
        return false;
    }

    @Override
    public Optional<User> activateUserByHash(String hash) {
        if(hash != null && !hash.isEmpty()) {
            BaseDao userDao = new UserDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(userDao)) {
                try {
                    LocalDateTime validDate = LocalDateTime.now();
                    Optional<User> userOptional = ((UserDao) userDao).findInactiveByHashAndHashIsNotExpired(hash, validDate);
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        user.setStatus(User.Status.ACTIVE);
                        userOptional = userDao.update(user);
                        if (userOptional.isPresent()) {
                            ((UserDao) userDao).deleteUserHashByUserId(user.getId());
                            transaction.commit();
                            return userOptional;
                        }
                    }
                } catch (DaoException e) {
                    transaction.rollback();
                }
            } catch (EntityTransactionException e) {
                logger.error("Failed to activate user by hash", e);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean activateUserByTelegramAccount(String account) {
        BaseDao userDao = new UserDaoImpl();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                Optional<User> userOptional = ((UserDao)userDao).findInactiveByTelegram(account);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.setStatus(User.Status.ACTIVE);
                    userOptional = userDao.update(user);
                    if(userOptional.isPresent()) {
                        ((UserDao) userDao).deleteUserHashByUserId(user.getId());
                        transaction.commit();
                        return true;
                    }
                }
            } catch (DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to activate user by telegram account", e);
        }
        return false;
    }

    private String getHostFromUrl(StringBuffer url) {
        return url.substring(0, url.indexOf(CONTROLLER_URL));
    }
}
