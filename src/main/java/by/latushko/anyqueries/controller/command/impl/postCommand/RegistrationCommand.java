package by.latushko.anyqueries.controller.command.impl.postCommand;

import by.latushko.anyqueries.controller.command.*;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import by.latushko.anyqueries.util.telegram.TelegramBot;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.INFO;
import static by.latushko.anyqueries.util.AppProperty.APP_ACTIVATION_LINK_ALIVE_HOURS;

public class RegistrationCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request, HttpServletResponse response) {
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

        RegistrationService registrationService = RegistrationServiceImpl.getInstance();
        boolean result = registrationService.registerUser(firstName, lastName, middleName, confirmationType, email, telegram, login, password);

        ResponseMessage message = null;
        if(result) {
            if (confirmationType.equals(RequestParameter.CONFIRMATION_TYPE_EMAIL)) {
                String text = "Ссылка для подтверждения учетной записи отправлена на <b>" + email + "</b>";
                String notice = "Обратите внимание, что ссылка действительна в течении " + APP_ACTIVATION_LINK_ALIVE_HOURS + " часов. " +
                        "По истечении данного срока Вам придется повторить процедуру регистрации с логином <b>" + login + "</b>";
                message = new ResponseMessage(INFO, text, notice);
            } else if (confirmationType.equals("telegram")) {
                message = new ResponseMessage(INFO, "Для активации учетной записи перейдите в чат telegram-бота <b>@" + TelegramBot.BOT_NAME + "</b>");
            } else {
                message = new ResponseMessage(DANGER, "При регистрации возникла непредвиденная ошибка");
            }
        }

        request.getSession().setAttribute(SessionAttribute.MESSAGE, message);
        return new PreparedResponse(PagePath.REGISTRATION_URL, PreparedResponse.RoutingType.REDIRECT);
    }
}


//todo chain of responibility?
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
//        } and so on......