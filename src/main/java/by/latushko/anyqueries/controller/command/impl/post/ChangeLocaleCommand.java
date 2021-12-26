package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageKey;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.util.AppProperty.APP_COOKIE_ALIVE_SECONDS;

public class ChangeLocaleCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String lang = request.getParameter(RequestParameter.LANG);
        CookieHelper.addCookie(response, CookieName.LANG, lang, APP_COOKIE_ALIVE_SECONDS);

        MessageManager manager = MessageManager.getManager(lang);
        ResponseMessage message = new ResponseMessage(ResponseMessage.Level.SUCCESS, manager.getMessage(MessageKey.MESSAGE_LANG_CHANGED));

        HttpSession session = request.getSession();
        String currentPage;
        if(session.getAttribute(SessionAttribute.CURRENT_PAGE) != null) {
            currentPage = session.getAttribute(SessionAttribute.CURRENT_PAGE).toString();
        } else {
            currentPage = PagePath.MAIN_URL;
        }
        session.setAttribute(SessionAttribute.MESSAGE, message);
        return new CommandResult(currentPage, CommandResult.RoutingType.REDIRECT);
    }
}
