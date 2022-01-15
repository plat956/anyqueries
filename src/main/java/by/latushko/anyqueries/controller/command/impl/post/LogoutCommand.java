package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.*;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.LOGIN_URL;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_LOGOUT_SUCCESS;

public class LogoutCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        CookieHelper.eraseCookie(request, response, CREDENTIAL_KEY, CREDENTIAL_TOKEN);
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        ResponseMessage message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_LOGOUT_SUCCESS));
        session = request.getSession();
        session.setAttribute(MESSAGE, message);
        return new CommandResult(LOGIN_URL, REDIRECT);
    }
}
