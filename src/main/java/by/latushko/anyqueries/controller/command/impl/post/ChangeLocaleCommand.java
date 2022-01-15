package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.ChangeLocaleFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.LANG;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.REFERER;
import static by.latushko.anyqueries.util.AppProperty.APP_COOKIE_ALIVE_SECONDS;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_ERROR_UNEXPECTED;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_LANG_CHANGED;

public class ChangeLocaleCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String referer = request.getHeader(REFERER);
        CommandResult commandResult = new CommandResult(referer, REDIRECT);
        FormValidator validator = ChangeLocaleFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        MessageManager manager;
        ResponseMessage message;
        if(!validationResult.getStatus()) {
            String userLang = CookieHelper.readCookie(request, CookieName.LANG);
            manager = MessageManager.getManager(userLang);
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
            session.setAttribute(SessionAttribute.MESSAGE, message);
            return commandResult;
        }
        String lang = request.getParameter(LANG);
        CookieHelper.addCookie(response, CookieName.LANG, lang, APP_COOKIE_ALIVE_SECONDS);
        manager = MessageManager.getManager(lang);
        message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_LANG_CHANGED));
        session.setAttribute(SessionAttribute.MESSAGE, message);
        return commandResult;
    }
}
