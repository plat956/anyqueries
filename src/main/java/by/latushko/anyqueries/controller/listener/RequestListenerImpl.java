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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_KEY;
import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_TOKEN;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.ANSWER_OBJECT;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.CREATE_RECORD;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PREVIOUS_PAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.VALIDATION_RESULT;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;

@WebListener
public class RequestListenerImpl implements ServletRequestListener {
    private static final Logger logger = LogManager.getLogger();

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
        Object createRecordObject = session.getAttribute(CREATE_RECORD);
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
        if(createRecordObject != null) {
            session.removeAttribute(CREATE_RECORD);
            request.setAttribute(RequestAttribute.CREATE_RECORD, createRecordObject);
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
            Optional<User> userOptional = userService.findActiveByCredentialKeyAndCredentialToken(credentialKey, credentialToken);
            if (userOptional.isPresent()){
                User user = userOptional.get();
                session.setAttribute(PRINCIPAL, user);
                logger.debug("User {} has been restored from cookies with session id {}", user.getId(), session.getId());
                return user;
            }
        }
        return null;
    }
}
