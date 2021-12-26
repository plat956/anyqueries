package by.latushko.anyqueries.controller.listener;

import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebListener
public class RequestListenerImpl implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent event) {
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        HttpSession session = request.getSession();
        Object message = session.getAttribute(SessionAttribute.MESSAGE);
        Object validationResult = session.getAttribute(SessionAttribute.VALIDATION_RESULT);
        if(message != null) {
            session.removeAttribute(SessionAttribute.MESSAGE);
            request.setAttribute(SessionAttribute.MESSAGE, message);
        }
        if(validationResult != null) {
            session.removeAttribute(SessionAttribute.VALIDATION_RESULT);
            request.setAttribute(SessionAttribute.VALIDATION_RESULT, validationResult);
        }
    }
}
