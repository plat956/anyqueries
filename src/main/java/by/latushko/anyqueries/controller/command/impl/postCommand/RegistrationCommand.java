package by.latushko.anyqueries.controller.command.impl.postCommand;

import by.latushko.anyqueries.controller.command.*;
import by.latushko.anyqueries.exception.DaoException;
import by.latushko.anyqueries.model.dao.BaseDao;
import by.latushko.anyqueries.model.dao.EntityTransaction;
import by.latushko.anyqueries.model.dao.UserDao;
import by.latushko.anyqueries.model.dao.impl.UserDaoImpl;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.model.entity.UserHash;
import by.latushko.anyqueries.exception.MailSenderException;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;
import by.latushko.anyqueries.util.mail.MailSender;
import by.latushko.anyqueries.util.telegram.TelegramBot;
import by.latushko.anyqueries.validator.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static by.latushko.anyqueries.controller.command.ResponseMessage.Type.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Type.INFO;
import static by.latushko.anyqueries.service.impl.UserServiceImpl.ACTIVATION_HASH_EXPIRES_IN_HOURS;

public class RegistrationCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request) {
        String firstName = request.getParameter(RequestParameter.FIRST_NAME);
        String lastName = request.getParameter(RequestParameter.LAST_NAME);
        String middleName = request.getParameter(RequestParameter.MIDDLE_NAME);
        String email = request.getParameter(RequestParameter.EMAIL);
        String confirmationType = request.getParameter(RequestParameter.CONFIRMATION_TYPE);
        String telegram = request.getParameter(RequestParameter.TELEGRAM);
        String login = request.getParameter(RequestParameter.LOGIN);
        String password = request.getParameter(RequestParameter.PASSWORD);
        String passwordConfirmed = request.getParameter(RequestParameter.PASSWORD_CONFIRMED);

        //todo: validation must be here - ask to look at some wild options below

        RegistrationService registrationService = new RegistrationServiceImpl();
        boolean result = registrationService.registerUser(firstName, lastName, middleName, confirmationType, email, telegram, login, password);

        ResponseMessage message = new ResponseMessage(DANGER, "При регистрации возникла непредвиденная ошибка");
        if(result) {
            if (confirmationType.equals("email")) {
                String text = "Ссылка для подтверждения учетной записи отправлена на <b>" + email + "</b>";
                String notice = "Обратите внимание, что ссылка действительна в течении " + ACTIVATION_HASH_EXPIRES_IN_HOURS + " часов. " +
                        "По истечении данного срока Вам придется повторить процедуру регистрации с логином <b>" + login + "</b>";
                message = new ResponseMessage(INFO, text, notice);
            } else if (confirmationType.equals("telegram")) {
                message = new ResponseMessage(INFO, "Для активации учетной записи перейдите в чат telegram-бота <b>@" + TelegramBot.BOT_NAME + "</b>");
            }
        }

        request.getSession().setAttribute(RequestAttribute.MESSAGE, message);
        return new PreparedResponse(PagePath.REGISTRATION_URL, PreparedResponse.RoutingType.REDIRECT);
    }
}



//######TODO INDIAN CODE 1
//        if(userValidator.checkIfFirstNameValid(firstName)) {
//            if(userValidator.checkIfLastNameValid(firstName)) {
//                if(userValidator.checkIfMiddleNameValid(firstName)) {
//                    if(userValidator.checkIfPasswordValid(firstName)) {
//                        if(userValidator.checkIfPasswordConfirmed(password, passwordConfirmed)) {
//                            if(userValidator.checkIfLoginNotExists(login)) {
//                                if(userValidator.checkIfEmailNotExists(email)) {
//                                    if(userValidator.checkIfTelegramNotExists(telegram)) {
//                                        boolean result = registrationService.registerUser(firstName, lastName, middleName, email, telegram, login, password);
//                                        if(result) {
//                                            String aliveHours = request.getServletContext().getInitParameter(CONFIRMATION_LINK_ALIVE_PARAMETER);
//                                            String text = "Ссылка для подтверждения учетной записи отправлена на <b>" + email + "</b>";
//                                            String notice = "Обратите внимание, что ссылка действительна в течении " + aliveHours + " часов. " +
//                                                    "По истечении данного срока Вам придется повторить процедуру регистрации с логином <b>" + login + "</b>";
//                                            message = new ResponseMessage(INFO, text, notice);
//                                        } else {
//                                            message = new ResponseMessage(DANGER, "При регистрации возникла непредвиденная ошибка");
//                                        }
//                                    } else {
//                                        message = new ResponseMessage(INFO, "Пользователь с указанным email уже существует");
//                                    }
//                                } else {
//                                    message = new ResponseMessage(INFO, "Пользователь с указанным email уже существует");
//                                }
//                            } else {
//                                message = new ResponseMessage(INFO, "Пользователь с указанным логином уже существует");
//                            }
//                        } else {
//                            message = new ResponseMessage(INFO, "Пароли не совпадают, заполните поле подтверждения пароля корректно");
//                        }
//                    } else {
//                        message = new ResponseMessage(DANGER, "Заполните поле корректно");
//                    }
//                } else {
//                    message = new ResponseMessage(DANGER, "Заполните поле корректно");
//                }
//            } else {
//                message = new ResponseMessage(DANGER, "Заполните поле корректно");
//            }
//        } else {
//            message = new ResponseMessage(DANGER, "Заполните поле корректно");
//        }


//######TODO INDIAN CODE 2
//            request.getSession().setAttribute(RequestAttribute.MESSAGE, message);
//            return new PreparedResponse(PagePath.REGISTRATION_URL, PreparedResponse.RoutingType.REDIRECT);
//        }
//        if(!userValidator.checkIfLastNameValid(firstName)) {
//            request.getSession().setAttribute(RequestAttribute.MESSAGE, message);
//            return new PreparedResponse(PagePath.REGISTRATION_URL, PreparedResponse.RoutingType.REDIRECT);
//        }
//        if(!userValidator.checkIfMiddleNameValid(firstName)) {
//            request.getSession().setAttribute(RequestAttribute.MESSAGE, message);
//            return new PreparedResponse(PagePath.REGISTRATION_URL, PreparedResponse.RoutingType.REDIRECT);
//        }
//        if(!userValidator.checkIfPasswordValid(firstName)) {
//            request.getSession().setAttribute(RequestAttribute.MESSAGE, message);
//            return new PreparedResponse(PagePath.REGISTRATION_URL, PreparedResponse.RoutingType.REDIRECT);
//        }
//      and so on