package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.AnswerService;
import by.latushko.anyqueries.service.impl.AnswerServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.http.QueryParameterHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.HeaderName.REFERER;
import static by.latushko.anyqueries.controller.command.identity.PagePath.ERROR_403_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_DELETE_FAILED;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_DELETE_SUCCESSFUL;

public class DeleteAnswerCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Long id = getLongParameter(request, ID);
        User user = (User) session.getAttribute(PRINCIPAL);
        AnswerService answerService = AnswerServiceImpl.getInstance();
        if(!answerService.checkDeleteAccess(id, user)) {
            return new CommandResult(ERROR_403_PAGE, FORWARD);
        }
        String referer = QueryParameterHelper.removeParameter(request.getHeader(REFERER), PAGE, EDIT);
        CommandResult commandResult = new CommandResult(referer, REDIRECT);
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        ResponseMessage message;
        boolean result = answerService.delete(id);
        if(result) {
            message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_DELETE_SUCCESSFUL));
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_DELETE_FAILED));
        }
        session.setAttribute(MESSAGE, message);
        return commandResult;
    }
}
