package by.latushko.anyqueries.service.impl;

import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.exception.EntityTransactionException;
import by.latushko.anyqueries.exception.MailSenderException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.UserDao;
import by.latushko.anyqueries.model.dao.impl.UserDaoImpl;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.util.mail.MailSender;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Optional;

import static by.latushko.anyqueries.util.AppProperty.APP_HOST;

public class RegistrationServiceImpl implements RegistrationService {
    private static final String VELOCITY_PROPERTIES_LOADER_CLASS = "classpath.resource.loader.class";
    private static final String VELOCITY_RESOURCES_PATH = "classpath";
    private static final String VELOCITY_ACTIVATION_TEMPLATE_PATH = "/template/mail/activation.vm";
    private static RegistrationServiceImpl instance;
    private UserService userService = UserServiceImpl.getInstance();

    private RegistrationServiceImpl() {
    }

    public static RegistrationService getInstance() {
        if (instance == null) {
            instance = new RegistrationServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean registerUser(String firstName, String lastName, String middleName, String confirmationType,
                                String email, String telegram, String login, String password) {
        BaseDao userDao = new UserDaoImpl();

        try (EntityTransaction transaction = new EntityTransaction()) {
            try {
                transaction.begin(userDao);

                User user = userService.createNewUser(firstName, lastName, middleName, email, telegram, login, password);
                userDao.create(user);

                if (confirmationType.equals(RequestParameter.CONFIRMATION_TYPE_EMAIL)) {
                    UserHash userHash = userService.generateUserHash(user);
                    ((UserDao) userDao).createUserHash(userHash);

                    sendActivationEmail(user, userHash);
                }

                transaction.commit();
                return true;
            } catch (EntityTransactionException | DaoException | MailSenderException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            //todo error log
        }
        return false;
    }

    @Override
    public boolean activateUserByHash(String hash) {
        BaseDao userDao = new UserDaoImpl();

        try (EntityTransaction transaction = new EntityTransaction()) {
            try {
                transaction.begin(userDao);

                LocalDateTime validDate = LocalDateTime.now();
                Optional<User> userOptional = ((UserDao)userDao).findInactiveUserByHashAndHashIsNotExpired(hash, validDate);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.setStatus(User.Status.ACTIVE);
                    userDao.update(user);

                    ((UserDao)userDao).deleteUserHashByUserId(user.getId());
                    return true;
                }

                transaction.commit();
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            //todo err log
        }

        return false;
    }

    @Override
    public boolean activateUserByTelegramAccount(String account) {
        BaseDao userDao = new UserDaoImpl();

        try (EntityTransaction transaction = new EntityTransaction()) {
            try {
                transaction.begin(userDao);

                Optional<User> userOptional = ((UserDao)userDao).findInactiveUserByTelegram(account);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.setStatus(User.Status.ACTIVE);
                    userDao.update(user);
                    return true;
                }

                transaction.commit();
                return true;
            } catch (EntityTransactionException | DaoException e) {
                transaction.rollback();
            }
        } catch (EntityTransactionException e) {
            //todo err log
        }

        return false;
    }

    private void sendActivationEmail(User user, UserHash userHash) throws MailSenderException {
        String messageBody = compileActivationEmail(user, userHash);

        MailSender sender = MailSender.getInstance();
        sender.send(user.getEmail(), "Account activation", messageBody);
    }

    private String compileActivationEmail(User user, UserHash userHash) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, VELOCITY_RESOURCES_PATH);
        ve.setProperty(VELOCITY_PROPERTIES_LOADER_CLASS, ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate(VELOCITY_ACTIVATION_TEMPLATE_PATH);
        VelocityContext context = new VelocityContext();
        context.put("title", "Welcome to ANY-QUERIES.BY"); //todo: read product.domain from app.props file

        String fio = userService.getUserFio(user);
        context.put("text", "Dear " + fio + ", you've sent a registration request. Please, press the button below to activate your account");
        context.put("buttonText", "Activate");
        context.put("buttonLink", APP_HOST + "/controller?command=activate_user&hash=" + userHash.getHash());

        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }
}
