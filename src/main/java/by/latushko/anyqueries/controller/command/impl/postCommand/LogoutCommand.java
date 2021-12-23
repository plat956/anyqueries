package by.latushko.anyqueries.controller.command.impl.postCommand;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.PreparedResponse;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.util.http.CookieHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LogoutCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request, HttpServletResponse response) {
        CookieHelper.eraseCookie(request, response, CookieName.CREDENTIAL_KEY, CookieName.CREDENTIAL_TOKEN);
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        session = request.getSession();
        session.setAttribute(SessionAttribute.MESSAGE, new ResponseMessage(ResponseMessage.Level.SUCCESS, "Всего доброго! Заходите еще."));
        return new PreparedResponse(PagePath.MAIN_URL, PreparedResponse.RoutingType.REDIRECT);
    }
}
