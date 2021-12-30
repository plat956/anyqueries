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

public class RegistrationServiceImpl implements RegistrationService {
    private static final Logger logger = LogManager.getLogger();
    private static RegistrationService instance;
    private final UserService userService = UserServiceImpl.getInstance();

    private RegistrationServiceImpl() {
    }

    public static RegistrationService getInstance() {
        if (instance == null) {
            instance = new RegistrationServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean registerUser(String firstName, String lastName, String middleName, boolean sendLink,
                                String email, String telegram, String login, String password, MessageManager manager) {
        BaseDao userDao = new UserDaoImpl();

        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                User user = userService.createNewUser(firstName, lastName, middleName, email, telegram, login, password);
                userDao.create(user);

                if (sendLink) {
                    UserHash userHash = userService.generateUserHash(user);
                    ((UserDao) userDao).createUserHash(userHash);

                    EmailService emailService = new EmailServiceImpl(manager);
                    emailService.sendActivationEmail(user, userHash);
                }

                transaction.commit();
                return true;
            } catch (EntityTransactionException | DaoException | MailSenderException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to register user", e);
        }
        return false;
    }

    @Override
    public boolean updateRegistrationData(User user, String email, String telegram, boolean sendLink, MessageManager manager) {
        BaseDao userDao = new UserDaoImpl();
        user.setEmail(email);
        user.setTelegram(telegram);
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                userDao.update(user);

                if (sendLink) {
                    UserHash userHash = userService.generateUserHash(user);
                    ((UserDao) userDao).createUserHash(userHash);

                    EmailService emailService = new EmailServiceImpl(manager);
                    emailService.sendActivationEmail(user, userHash);
                }

                transaction.commit();
                return true;
            } catch (EntityTransactionException | DaoException | MailSenderException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to update user registration data", e);
        }
        return false;
    }

    @Override
    public Optional<User> activateUserByHash(String hash) {
        BaseDao userDao = new UserDaoImpl();
        Optional<User> activatedUser = Optional.empty();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                LocalDateTime validDate = LocalDateTime.now();
                Optional<User> userOptional = ((UserDao)userDao).findInactiveUserByHashAndHashIsNotExpired(hash, validDate);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.setStatus(User.Status.ACTIVE);
                    userDao.update(user);

                    ((UserDao)userDao).deleteUserHashByUserId(user.getId());
                    transaction.commit();

                    activatedUser = userOptional;
                }
                //todo, нужно ли было здесь делать transaction.commit(); ?
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to activate user by hash", e);
        }

        return activatedUser;
    }

    @Override
    public boolean activateUserByTelegramAccount(String account) {
        BaseDao userDao = new UserDaoImpl();

        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                Optional<User> userOptional = ((UserDao)userDao).findInactiveUserByTelegram(account);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.setStatus(User.Status.ACTIVE);
                    userDao.update(user);
                    ((UserDao)userDao).deleteUserHashByUserId(user.getId());
                    transaction.commit();
                    return true;
                }

            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Failed to activate user by telegram account", e);
        }

        return false;
    }
}
