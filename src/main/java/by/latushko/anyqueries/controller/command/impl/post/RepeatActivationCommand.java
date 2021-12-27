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
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PagePath.REPEAT_ACTIVATION_URL;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_LOGOUT_SUCCESS;

public class RepeatActivationCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        //todo code to change data and send link
        String userLang = CookieHelper.readCookie(request, LANG).orElse(null);
        MessageManager manager = MessageManager.getManager(userLang);
        HttpSession session = request.getSession();
        session.setAttribute(MESSAGE, new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_LOGOUT_SUCCESS)));
        return new CommandResult(REPEAT_ACTIVATION_URL, REDIRECT);
    }
}
