package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageKey;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.*;
import static by.latushko.anyqueries.util.AppProperty.APP_ACTIVATION_LINK_ALIVE_HOURS;
import static by.latushko.anyqueries.util.telegram.TelegramBot.BOT_NAME;

public class RegistrationCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String firstName = request.getParameter(RequestParameter.FIRST_NAME);
        String lastName = request.getParameter(RequestParameter.LAST_NAME);
        String middleName = request.getParameter(RequestParameter.MIDDLE_NAME);
        String email = request.getParameter(RequestParameter.EMAIL);
        String telegram = request.getParameter(RequestParameter.TELEGRAM);
        String login = request.getParameter(RequestParameter.LOGIN);
        String password = request.getParameter(RequestParameter.PASSWORD);
        String passwordConfirmed = request.getParameter(RequestParameter.PASSWORD_CONFIRMED);
        boolean sendLink = request.getParameter(RequestParameter.SEND_LINK) != null;

        //todo: validation must be here - ask to look at some wild options below

        RegistrationService registrationService = RegistrationServiceImpl.getInstance();
        boolean result = registrationService.registerUser(firstName, lastName, middleName, sendLink, email, telegram, login, password);

        String userLang = CookieHelper.readCookie(request, CookieName.LANG).orElse(null);
        MessageManager manager = MessageManager.getManager(userLang);

        ResponseMessage message;
        if(result) {
            String text, notice;
            if (sendLink) {
                text = manager.getMessage(MessageKey.MESSAGE_ACTIVATION_EMAIL_TITLE, email);
                notice = manager.getMessage(MessageKey.MESSAGE_ACTIVATION_EMAIL_NOTICE, APP_ACTIVATION_LINK_ALIVE_HOURS, "https://t.me/" + BOT_NAME, BOT_NAME);
                notice += manager.getMessage(MessageKey.MESSAGE_ACTIVATION_GLOBAL_NOTICE);
            } else {
                text = manager.getMessage(MessageKey.MESSAGE_ACTIVATION_TELEGRAM, "https://t.me/" + BOT_NAME, BOT_NAME);
                notice = manager.getMessage(MessageKey.MESSAGE_ACTIVATION_GLOBAL_NOTICE);
            }
            message = new ResponseMessage(SUCCESS, text, notice);
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MessageKey.MESSAGE_REGISTRATION_FAIL));
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttribute.MESSAGE, message);
        return new CommandResult(PagePath.REGISTRATION_URL, CommandResult.RoutingType.REDIRECT);
    }
}