package by.latushko.anyqueries.controller.filter.security;

import by.latushko.anyqueries.controller.command.CommandType;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.util.http.CookieHelper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_KEY;
import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_TOKEN;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.QUESTIONS_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.COMMAND;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.INACTIVE_PRINCIPAL;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;

@WebFilter(filterName = "securityFilter", urlPatterns = "/controller")
public class SecurityFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String command = request.getParameter(COMMAND);
        Optional<CommandType> commandType = CommandType.getByName(command);

        if (commandType.isPresent()) {
            HttpSession session = request.getSession();

            User principal = null;
            if (session.getAttribute(PRINCIPAL) != null) {
                principal = (User) session.getAttribute(PRINCIPAL);
            } else {
                CookieHelper.eraseCookie(request, response, CREDENTIAL_KEY, CREDENTIAL_TOKEN);
            }

            AccessRole currentRole = AccessRole.GUEST;
            if (principal != null) {
                currentRole = AccessRole.valueOf(principal.getRole().name());
            } else if(session.getAttribute(INACTIVE_PRINCIPAL) != null) {
                currentRole = AccessRole.INACTIVE_USER;
            }

            if (!RestrictedCommand.hasAccess(commandType.get(), currentRole)) {
                response.sendRedirect(QUESTIONS_URL);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}