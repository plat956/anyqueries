package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.RepeatActivationFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.REPEAT_ACTIVATION_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;
import static by.latushko.anyqueries.util.AppProperty.APP_ACTIVATION_LINK_ALIVE_HOURS;
import static by.latushko.anyqueries.util.AppProperty.APP_TELEGRAM_LINK_HOST;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;
import static by.latushko.anyqueries.util.telegram.TelegramBot.BOT_NAME;

public class RepeatActivationCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        CommandResult commandResult = new CommandResult(REPEAT_ACTIVATION_URL, REDIRECT);
        FormValidator validator = RepeatActivationFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        UserService userService = UserServiceImpl.getInstance();
        User currentUser = (User) session.getAttribute(INACTIVE_PRINCIPAL);
        String email = request.getParameter(EMAIL);
        if(email != null && !email.isEmpty() && userService.existsByEmailExceptUserId(email, currentUser.getId())) {
            validationResult.setError(EMAIL, LABEL_EMAIL_EXISTS);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        String telegram = request.getParameter(TELEGRAM);
        if(telegram != null && !telegram.isEmpty() && userService.existsByTelegramExceptUserId(telegram, currentUser.getId())) {
            validationResult.setError(TELEGRAM, LABEL_TELEGRAM_EXISTS);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        boolean sendLink = request.getParameter(SEND_LINK) != null;

        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);

        RegistrationService registrationService = RegistrationServiceImpl.getInstance();
        boolean result = registrationService.updateRegistrationData(currentUser, email, telegram, sendLink, manager);

        ResponseMessage message;
        if (result) {
            String notice;
            String telegramBotUrl = APP_TELEGRAM_LINK_HOST + BOT_NAME;
            if (sendLink) {
                StringBuilder noticeBulder = new StringBuilder();
                noticeBulder.append(manager.getMessage(MESSAGE_ACTIVATION_EMAIL_TITLE, email));
                noticeBulder.append(manager.getMessage(MESSAGE_ACTIVATION_EMAIL_NOTICE, APP_ACTIVATION_LINK_ALIVE_HOURS, telegramBotUrl, BOT_NAME));
                notice = noticeBulder.toString();
            } else {
                notice = manager.getMessage(MESSAGE_ACTIVATION_TELEGRAM, telegramBotUrl, BOT_NAME);
            }
            message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_SAVE_SUCCESSFUL), notice);
        } else {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_SAVE_FAILED));
        }

        session.setAttribute(MESSAGE, message);
        return commandResult;
    }
}
