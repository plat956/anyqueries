package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.PagePath.MAIN_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.LANG;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.CURRENT_PAGE;
import static by.latushko.anyqueries.util.AppProperty.APP_COOKIE_ALIVE_SECONDS;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_LANG_CHANGED;

public class ChangeLocaleCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String lang = request.getParameter(LANG);
        CookieHelper.addCookie(response, CookieName.LANG, lang, APP_COOKIE_ALIVE_SECONDS);
        HttpSession session = request.getSession();
        String currentPage;
        if(session.getAttribute(CURRENT_PAGE) != null) {
            currentPage = session.getAttribute(CURRENT_PAGE).toString();
        } else {
            currentPage = MAIN_URL;
        }
        MessageManager manager = MessageManager.getManager(lang);
        ResponseMessage message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_LANG_CHANGED));
        session.setAttribute(SessionAttribute.MESSAGE, message);
        return new CommandResult(currentPage, REDIRECT);
    }
}
