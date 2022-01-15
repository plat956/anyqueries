package by.latushko.anyqueries.controller.listener;

import by.latushko.anyqueries.controller.command.identity.RequestAttribute;
import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.CategoryService;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.CategoryServiceImpl;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_KEY;
import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_TOKEN;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.ANSWER_OBJECT;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.VALIDATION_RESULT;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;

@WebListener
public class RequestListenerImpl implements ServletRequestListener {
    private QuestionService questionService = QuestionServiceImpl.getInstance();

    @Override
    public void requestInitialized(ServletRequestEvent event) {
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        HttpSession session = request.getSession();

        if(session.getAttribute(PRINCIPAL) == null) {
            String credentialKey = CookieHelper.readCookie(request, CREDENTIAL_KEY);
            String credentialToken = CookieHelper.readCookie(request, CREDENTIAL_TOKEN);
            if (credentialKey != null && credentialToken != null) {
                UserService userService = UserServiceImpl.getInstance();
                Optional<User> user = userService.findByCredentialsKeyAndToken(credentialKey, credentialToken);
                if (user.isPresent() && user.get().getStatus() == User.Status.ACTIVE) {
                    session.setAttribute(PRINCIPAL, user.get());
                }
            }
        }

        Object message = session.getAttribute(MESSAGE);
        Object validationResult = session.getAttribute(VALIDATION_RESULT);
        Object answerObject = session.getAttribute(ANSWER_OBJECT);

        if(message != null) {
            session.removeAttribute(MESSAGE);
            request.setAttribute(RequestAttribute.MESSAGE, message);
        }
        if(validationResult != null) {
            session.removeAttribute(VALIDATION_RESULT);
            request.setAttribute(RequestAttribute.VALIDATION_RESULT, validationResult);
        }
        if(answerObject != null) {
            session.removeAttribute(ANSWER_OBJECT);
            request.setAttribute(RequestAttribute.ANSWER_OBJECT, answerObject);
        }
        Long totalQuestions = questionService.countNotClosed();
        request.setAttribute(LAYOUT_TOTAL_QUESTIONS, totalQuestions);
        User user = (User) session.getAttribute(PRINCIPAL);
        if(user != null) {
            Long totalUserQuestions = questionService.countNotClosedByAuthorId(user.getId());
            request.setAttribute(LAYOUT_TOTAL_USER_QUESTIONS, totalUserQuestions);
        }
        CategoryService categoryService = CategoryServiceImpl.getInstance();
        List<Category> categories = categoryService.findTop5();
        request.setAttribute(LAYOUT_TOP_CATEGORIES, categories);
    }
}
