package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.*;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageKey;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.util.telegram.TelegramBot;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.INFO;
import static by.latushko.anyqueries.util.AppProperty.APP_ACTIVATION_LINK_ALIVE_HOURS;

public class RegistrationCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
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

        String userLang = CookieHelper.readCookie(request, CookieName.LANG).orElse(null);
        MessageManager manager = MessageManager.getManager(userLang);

        ResponseMessage message = null;
        if(result) {
            if (confirmationType.equals(RequestParameter.CONFIRMATION_TYPE_EMAIL)) {
                String text = manager.getMessage(MessageKey.MESSAGE_ACTIVATION_EMAIL_TITLE, email);
                String notice = manager.getMessage(MessageKey.MESSAGE_ACTIVATION_EMAIL_NOTICE, APP_ACTIVATION_LINK_ALIVE_HOURS, login);
                message = new ResponseMessage(INFO, text, notice);
            } else if (confirmationType.equals(RequestParameter.CONFIRMATION_TYPE_TELEGRAM)) {
                message = new ResponseMessage(INFO, manager.getMessage(MessageKey.MESSAGE_ACTIVATION_TELEGRAM, TelegramBot.BOT_NAME));
            } else {
                message = new ResponseMessage(DANGER, manager.getMessage(MessageKey.MESSAGE_REGISTRATION_FAIL));
            }
        }

        request.getSession().setAttribute(SessionAttribute.MESSAGE, message);
        return new CommandResult(PagePath.REGISTRATION_URL, CommandResult.RoutingType.REDIRECT);
    }
}