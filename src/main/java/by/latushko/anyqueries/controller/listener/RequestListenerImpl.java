package by.latushko.anyqueries.controller.listener;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.VALIDATION_RESULT;

@WebListener
public class RequestListenerImpl implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent event) {
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        HttpSession session = request.getSession();
        Object message = session.getAttribute(MESSAGE);
        Object validationResult = session.getAttribute(VALIDATION_RESULT);
        if(message != null) {
            session.removeAttribute(MESSAGE);
            request.setAttribute(MESSAGE, message);
        }
        if(validationResult != null) {
            session.removeAttribute(VALIDATION_RESULT);
            request.setAttribute(VALIDATION_RESULT, validationResult);
        }
    }
}
