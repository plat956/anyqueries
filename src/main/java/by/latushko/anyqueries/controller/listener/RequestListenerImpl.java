package by.latushko.anyqueries.controller.listener;

import by.latushko.anyqueries.controller.command.identity.RequestAttribute;
import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.CategoryService;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.CategoryServiceImpl;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.VALIDATION_RESULT;

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
        Long totalQuestions = questionService.countTotalNotClosed();
        request.setAttribute(LAYOUT_TOTAL_QUESTIONS, totalQuestions);
        User user = (User) session.getAttribute(PRINCIPAL);
        if(user != null) {
            Long totalUserQuestions = questionService.countTotalNotClosedByAuthorId(user.getId());
            request.setAttribute(LAYOUT_TOTAL_USER_QUESTIONS, totalUserQuestions);
        }
        CategoryService categoryService = CategoryServiceImpl.getInstance();
        List<Category> categories = categoryService.findTop5();
        request.setAttribute(LAYOUT_TOP_CATEGORIES, categories);
    }
}
