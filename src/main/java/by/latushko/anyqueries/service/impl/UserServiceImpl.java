package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.UserDao;
import by.latushko.anyqueries.model.dao.impl.UserDaoImpl;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;
import by.latushko.anyqueries.util.http.CookieHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Optional;

import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_KEY;
import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_TOKEN;
import static by.latushko.anyqueries.util.AppProperty.APP_ACTIVATION_LINK_ALIVE_HOURS;
import static by.latushko.anyqueries.util.AppProperty.APP_COOKIE_ALIVE_SECONDS;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static final String ACTIVATION_HASH_ADDITIONAL_SALT = "#@бЫрвалГ?";
    private static final String CREDENTIAL_KEY_ADDITIONAL_SALT = "A3>rE(wY%.LA)4V!";
    private static final String CREDENTIAL_TOKEN_ADDITIONAL_SALT = ";{(NP3yE4aG4fkZT";
    private static UserServiceImpl instance;
    private final PasswordEncoder passwordEncoder = BCryptPasswordEncoder.getInstance();

    private UserServiceImpl() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    @Override
    public User createNewUser(String firstName, String lastName, String middleName, String email, String telegram, String login, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setEmail(email);
        user.setTelegram(telegram);
        user.setLogin(login);
        user.setCredentialKey(passwordEncoder.encode(CREDENTIAL_KEY_ADDITIONAL_SALT + login));
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.INACTIVE);
        return user;
    }

    @Override
    public UserHash generateUserHash(User user) {
        UserHash userHash = new UserHash();
        userHash.setUser(user);
        userHash.setHash(passwordEncoder.encode(ACTIVATION_HASH_ADDITIONAL_SALT + user.getLogin()));
        userHash.setExpires(LocalDateTime.now().plusHours(APP_ACTIVATION_LINK_ALIVE_HOURS));
        return userHash;
    }

    @Override
    public boolean authorize(User user, HttpServletRequest request, HttpServletResponse response, boolean remember, boolean manually) {
        //fixme how to design this method without request&response references???
        if(manually) {
            user.setLastLoginDate(LocalDateTime.now());
            BaseDao userDao = new UserDaoImpl();
            try (EntityTransaction transaction = new EntityTransaction(userDao)) {
                try {
                    userDao.update(user);
                    transaction.commit();
                } catch (EntityTransactionException | DaoException e) {
                    transaction.rollback();
                    return false;
                }
            } catch (EntityTransactionException e) {
                logger.error("Something went wrong during retrieving user by login", e);
            }
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionAttribute.PRINCIPAL, user);

        if(remember) {
            CookieHelper.addCookie(response, CREDENTIAL_KEY, user.getCredentialKey(), APP_COOKIE_ALIVE_SECONDS);
            String tokenSource = getCredentialTokenSource(user);
            CookieHelper.addCookie(response, CREDENTIAL_TOKEN, passwordEncoder.encode(tokenSource), APP_COOKIE_ALIVE_SECONDS);
        }
        return true;
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        BaseDao userDao = new UserDaoImpl();

        Optional<User> userOptional = Optional.empty();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                userOptional = ((UserDao)userDao).findUserByLogin(login);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving user by login", e);
        }
        return userOptional;
    }

    @Override
    public Optional<User> findUserByCredentialKey(String key) {
        BaseDao userDao = new UserDaoImpl();

        Optional<User> userOptional = Optional.empty();
        try (EntityTransaction transaction = new EntityTransaction(userDao)) {
            try {
                userOptional = ((UserDao)userDao).findUserByCredentialKey(key);
                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            logger.error("Something went wrong during retrieving user by credential key", e);
        }
        return userOptional;
    }

    @Override
    public boolean changeLocale(String lang, HttpServletResponse response) {
        CookieHelper.addCookie(response, CookieName.LANG, lang, APP_COOKIE_ALIVE_SECONDS);
        return true;
    }

    @Override
    public String getCredentialTokenSource(User user) {
        return CREDENTIAL_TOKEN_ADDITIONAL_SALT + user.getLogin();
    }
}
