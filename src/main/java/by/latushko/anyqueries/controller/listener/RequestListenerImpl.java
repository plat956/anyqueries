package by.latushko.anyqueries.controller.listener;

import by.latushko.anyqueries.controller.command.identity.RequestAttribute;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.LAYOUT_TOTAL_QUESTIONS;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.LAYOUT_TOTAL_USER_QUESTIONS;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;

@WebListener
public class RequestListenerImpl implements ServletRequestListener {
    private QuestionService questionService = QuestionServiceImpl.getInstance();

    @Override
    public void requestInitialized(ServletRequestEvent event) {
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        HttpSession session = request.getSession();
        Object message = session.getAttribute(MESSAGE);
        Object validationResult = session.getAttribute(VALIDATION_RESULT);
        if(message != null) {
            session.removeAttribute(MESSAGE);
            request.setAttribute(RequestAttribute.MESSAGE, message);
        }
        if(validationResult != null) {
            session.removeAttribute(VALIDATION_RESULT);
            request.setAttribute(RequestAttribute.VALIDATION_RESULT, validationResult);
        }

        Long totalQuestions = questionService.countTotalNotClosedQuestions();
        request.setAttribute(LAYOUT_TOTAL_QUESTIONS, totalQuestions);
        User user = (User) session.getAttribute(PRINCIPAL);
        if(user != null) {
            Long totalUserQuestions = questionService.countTotalNotClosedQuestionsByAuthorId(user.getId());
            request.setAttribute(LAYOUT_TOTAL_USER_QUESTIONS, totalUserQuestions);
        }

        //todo top 5 categories + join count question in it
    }
}
