package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.AnswerService;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.AnswerServiceImpl;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.PagePath.CHANGE_PASSWORD_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.TOTAL_ANSWERS;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.TOTAL_QUESTIONS;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;

public class ChangePasswordPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(PRINCIPAL);
        QuestionService questionService = QuestionServiceImpl.getInstance();
        AnswerService answerService = AnswerServiceImpl.getInstance();
        request.setAttribute(TOTAL_QUESTIONS, questionService.countByAuthorId(user.getId()));
        request.setAttribute(TOTAL_ANSWERS, answerService.countByUserId(user.getId()));
        return new CommandResult(CHANGE_PASSWORD_PAGE, FORWARD);
    }
}
