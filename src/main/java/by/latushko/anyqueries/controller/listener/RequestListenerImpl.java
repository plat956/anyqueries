package by.latushko.anyqueries.controller.listener;

import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;

@WebListener
public class RequestListenerImpl implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent event) {
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        Object message = request.getSession().getAttribute(SessionAttribute.MESSAGE);
        request.getSession().removeAttribute(SessionAttribute.MESSAGE);
        request.setAttribute(SessionAttribute.MESSAGE, message);
    }
}
