package by.latushko.anyqueries.controller.listener;

import by.latushko.anyqueries.controller.command.identity.RequestAttribute;
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

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_KEY;
import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_TOKEN;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.ANSWER_OBJECT;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PREVIOUS_PAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.VALIDATION_RESULT;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;

@WebListener
public class RequestListenerImpl implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent event) {
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        HttpSession session = request.getSession();

        User currentUser = (User) session.getAttribute(PRINCIPAL);
        if(currentUser == null) {
            currentUser = restorePrincipal(request, session);
        }

        Object message = session.getAttribute(MESSAGE);
        Object validationResult = session.getAttribute(VALIDATION_RESULT);
        Object answerObject = session.getAttribute(ANSWER_OBJECT);
        Object previousPageObject = session.getAttribute(PREVIOUS_PAGE);

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
        if(previousPageObject != null) {
            session.removeAttribute(PREVIOUS_PAGE);
            request.setAttribute(RequestAttribute.PREVIOUS_PAGE, previousPageObject);
        }
        QuestionService questionService = QuestionServiceImpl.getInstance();
        request.setAttribute(LAYOUT_TOTAL_QUESTIONS, questionService.countNotClosed());
        if(currentUser != null) {
            request.setAttribute(LAYOUT_TOTAL_USER_QUESTIONS, questionService.countNotClosedByAuthorId(currentUser.getId()));
        }
        CategoryService categoryService = CategoryServiceImpl.getInstance();
        request.setAttribute(LAYOUT_TOP_CATEGORIES, categoryService.findTop5());
    }

    private User restorePrincipal(HttpServletRequest request, HttpSession session) {
        String credentialKey = CookieHelper.readCookie(request, CREDENTIAL_KEY);
        String credentialToken = CookieHelper.readCookie(request, CREDENTIAL_TOKEN);
        if (credentialKey != null && credentialToken != null) {
            UserService userService = UserServiceImpl.getInstance();
            Optional<User> userOptional = userService.findByCredentialKeyAndCredentialToken(credentialKey, credentialToken);
            if (userOptional.isPresent()){
                User user = userOptional.get();
                if(user.getStatus() == User.Status.ACTIVE) {
                    session.setAttribute(PRINCIPAL, user);
                    return user;
                }
            }
        }
        return null;
    }
}
