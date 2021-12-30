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
import static by.latushko.anyqueries.controller.command.identity.PagePath.EDIT_PROFILE_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.TOTAL_ANSWERS;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.TOTAL_QUESTIONS;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;

public class EditProfilePageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(PRINCIPAL);
        QuestionService questionService = QuestionServiceImpl.getInstance();
        AnswerService answerService = AnswerServiceImpl.getInstance();
        Long totalQuestions = questionService.countTotalQuestionsByAuthorId(user.getId());
        Long totalAnswers = answerService.countTotalAnswersByUserId(user.getId());
        request.setAttribute(TOTAL_QUESTIONS, totalQuestions);
        request.setAttribute(TOTAL_ANSWERS, totalAnswers);
        return new CommandResult(EDIT_PROFILE_PAGE, FORWARD);
    }
}
