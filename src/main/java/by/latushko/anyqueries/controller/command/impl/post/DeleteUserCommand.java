package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.http.QueryParameterHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.HeaderName.REFERER;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.ID;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.PAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_DELETE_FAILED;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_DELETE_SUCCESSFUL;

public class DeleteUserCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String referer = QueryParameterHelper.removeParameter(request.getHeader(REFERER), PAGE);
        CommandResult commandResult = new CommandResult(referer, REDIRECT);
        String idParameter = request.getParameter(ID);
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        ResponseMessage message;
        if(idParameter == null || idParameter.isEmpty()) {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_DELETE_FAILED));
            session.setAttribute(MESSAGE, message);
            return commandResult;
        }
        Long id = Long.valueOf(idParameter);
        UserService userService = UserServiceImpl.getInstance();
        boolean result = userService.delete(id);
        if(result) {
            message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_DELETE_SUCCESSFUL));
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_DELETE_FAILED));
        }
        session.setAttribute(MESSAGE, message);
        return commandResult;
    }
}
