package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.UserDao;
import by.latushko.anyqueries.model.dao.impl.UserDaoImpl;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.Optional;

import static by.latushko.anyqueries.util.AppProperty.APP_ACTIVATION_LINK_ALIVE_HOURS;
import static by.latushko.anyqueries.util.AppProperty.APP_AUTHORIZATION_COOKIE_ALIVE_DAYS;

public class UserServiceImpl implements UserService {
    private static final String ACTIVATION_HASH_ADDITIONAL_SALT = "#@бЫрвалГ?";
    private static final String CREDENTIAL_TOKEN_ADDITIONAL_SALT = "A3>rE(wY%.LA)4V!";
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User createNewUser(String firstName, String lastName, String middleName, String email, String telegram, String login, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        user.setEmail(email);
        user.setTelegram(telegram);
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.ROLE_USER);
        user.setStatus(User.Status.INACTIVE);
        return user;
    }

    @Override
    public UserHash generateActivationHash(User user) {
        UserHash userHash = new UserHash();
        userHash.setUser(user);
        userHash.setHash(passwordEncoder.encode(ACTIVATION_HASH_ADDITIONAL_SALT + user.getLogin()));
        userHash.setExpires(LocalDateTime.now().plusHours(APP_ACTIVATION_LINK_ALIVE_HOURS));
        return userHash;
    }

    @Override
    public String getUserFio(User user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String middleName = user.getMiddleName();
        String fio = lastName.substring(0, 1).toUpperCase() + lastName.substring(1) + " "
                + firstName.substring(0, 1).toUpperCase() + ". "
                + middleName.substring(0, 1).toUpperCase();
        return fio;
    }

    @Override
    public boolean authorize(User user, HttpServletRequest request, HttpServletResponse response) { //todo how to do this without request&response references???
        HttpSession session = request.getSession();
        session.setAttribute("principal", user);

        Integer cookieMaxAge = APP_AUTHORIZATION_COOKIE_ALIVE_DAYS * 24 * 60 * 60;
        Cookie keyCookie = new Cookie("CREDENTIAL_KEY", "qweerty"); //todo geenerate and store near the user
        keyCookie.setMaxAge(cookieMaxAge);
        response.addCookie(keyCookie);

        Cookie cookie = new Cookie("CREDENTIAL_TOKEN", passwordEncoder.encode(CREDENTIAL_TOKEN_ADDITIONAL_SALT + user.getLogin())); //todo geenerate
        cookie.setMaxAge(cookieMaxAge);
        response.addCookie(cookie);

        //work with sessions+cookies
        return true;
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        BaseDao userDao = new UserDaoImpl();

        Optional<User> userOptional = Optional.empty();
        try (EntityTransaction transaction = new EntityTransaction()) {
            try {
                transaction.begin(userDao);
                userOptional = ((UserDao)userDao).findUserByLogin(login);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        }
        return userOptional;
    }

    @Override
    public Optional<User> findUserByCredentialKey(String key) {
        BaseDao userDao = new UserDaoImpl();

        Optional<User> userOptional = Optional.empty();
        try (EntityTransaction transaction = new EntityTransaction()) {
            try {
                transaction.begin(userDao);
                userOptional = ((UserDao)userDao).findUserByCredentialKey(key);
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
            }
        }
        return userOptional;
    }
}
