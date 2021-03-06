package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.RegistrationFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.REGISTRATION_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.VALIDATION_RESULT;
import static by.latushko.anyqueries.util.AppProperty.APP_ACTIVATION_LINK_ALIVE_HOURS;
import static by.latushko.anyqueries.util.AppProperty.APP_TELEGRAM_LINK_HOST;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;
import static by.latushko.anyqueries.util.telegram.TelegramBot.BOT_NAME;

public class RegistrationCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        CommandResult commandResult = new CommandResult(REGISTRATION_URL, REDIRECT);
        FormValidator validator = RegistrationFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        UserService userService = UserServiceImpl.getInstance();
        String email = request.getParameter(EMAIL);
        if(email != null && !email.isEmpty() && userService.existsByEmail(email)) {
            validationResult.setError(EMAIL, LABEL_EMAIL_EXISTS);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        String telegram = request.getParameter(TELEGRAM);
        if(telegram != null && !telegram.isEmpty() && userService.existsByTelegram(telegram)) {
            validationResult.setError(TELEGRAM, LABEL_TELEGRAM_EXISTS);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        String login = request.getParameter(LOGIN);
        if(userService.existsByLogin(login)) {
            validationResult.setError(LOGIN, LABEL_LOGIN_EXISTS);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        String firstName = request.getParameter(FIRST_NAME);
        String lastName = request.getParameter(LAST_NAME);
        String middleName = request.getParameter(MIDDLE_NAME);
        String password = request.getParameter(PASSWORD);
        boolean sendLink = request.getParameter(SEND_LINK) != null;

        String userLang = CookieHelper.readCookie(request, CookieName.LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        RegistrationService registrationService = RegistrationServiceImpl.getInstance();
        boolean result = registrationService.registerUser(firstName, lastName, middleName, sendLink, email, telegram,
                login, password, manager, request.getRequestURL());
        ResponseMessage message = buildResponseMessage(result, session, manager, validationResult, sendLink, email);
        session.setAttribute(MESSAGE, message);
        return commandResult;
    }

    private ResponseMessage buildResponseMessage(boolean result, HttpSession session, MessageManager manager,
                                                 ValidationResult validationResult, boolean sendLink, String email) {
        ResponseMessage message;
        if (result) {
            String text;
            String notice;
            String telegramBotUrl = APP_TELEGRAM_LINK_HOST + BOT_NAME;
            if (sendLink) {
                text = manager.getMessage(MESSAGE_ACTIVATION_EMAIL_TITLE, email);
                StringBuilder noticeBulder = new StringBuilder(manager.getMessage(MESSAGE_ACTIVATION_EMAIL_NOTICE,
                        APP_ACTIVATION_LINK_ALIVE_HOURS, telegramBotUrl, BOT_NAME));
                noticeBulder.append(manager.getMessage(MESSAGE_ACTIVATION_GLOBAL_NOTICE));
                notice = noticeBulder.toString();
            } else {
                text = manager.getMessage(MESSAGE_ACTIVATION_TELEGRAM, telegramBotUrl, BOT_NAME);
                notice = manager.getMessage(MESSAGE_ACTIVATION_GLOBAL_NOTICE);
            }
            message = new ResponseMessage(SUCCESS, text, notice);
        } else {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_REGISTRATION_FAIL));
        }
        return message;
    }
}