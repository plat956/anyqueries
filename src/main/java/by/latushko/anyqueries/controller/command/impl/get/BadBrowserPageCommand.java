package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.identity.PagePath.BAD_BROWSER_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.MESSAGE;
import static by.latushko.anyqueries.util.AppProperty.APP_NAME;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_BAD_BROWSER;

public class BadBrowserPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String userLang = CookieHelper.readCookie(request, CookieName.LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        ResponseMessage message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_BAD_BROWSER, APP_NAME));
        request.setAttribute(MESSAGE, message);
        return new CommandResult(BAD_BROWSER_PAGE, FORWARD);
    }
}
