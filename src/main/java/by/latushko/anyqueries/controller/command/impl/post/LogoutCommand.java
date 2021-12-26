package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageKey;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LogoutCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        CookieHelper.eraseCookie(request, response, CookieName.CREDENTIAL_KEY, CookieName.CREDENTIAL_TOKEN);
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        String userLang = CookieHelper.readCookie(request, CookieName.LANG).orElse(null);
        MessageManager manager = MessageManager.getManager(userLang);
        session = request.getSession();
        session.setAttribute(SessionAttribute.MESSAGE, new ResponseMessage(ResponseMessage.Level.SUCCESS, manager.getMessage(MessageKey.MESSAGE_LOGOUT_SUCCESS)));
        return new CommandResult(PagePath.MAIN_URL, CommandResult.RoutingType.REDIRECT);
    }
}
