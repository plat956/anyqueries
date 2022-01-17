package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
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
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.CLOSE;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.ID;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_ERROR_UNEXPECTED;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_SUCCESS;

public class ChangeQuestionStatusCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Long id = getLongParameter(request, ID);
        User user = (User) session.getAttribute(PRINCIPAL);
        QuestionService questionService = QuestionServiceImpl.getInstance();
        if(!questionService.checkChangeStatusAccess(id, user.getId())) {
            return new CommandResult(ERROR_403_PAGE, FORWARD);
        }
        String referer = request.getHeader(REFERER);
        boolean close = Boolean.valueOf(request.getParameter(CLOSE));
        boolean result = questionService.changeStatus(id, close);
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        ResponseMessage message;
        if(result) {
            message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_SUCCESS));
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
        }
        session.setAttribute(MESSAGE, message);
        return new CommandResult(referer, REDIRECT);
    }
}
