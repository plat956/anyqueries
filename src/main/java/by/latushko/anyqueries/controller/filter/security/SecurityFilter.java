package by.latushko.anyqueries.controller.filter.security;

import by.latushko.anyqueries.controller.command.CommandType;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
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
import static by.latushko.anyqueries.controller.command.identity.PagePath.MAIN_URL;
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
                if(principal.getStatus() != User.Status.ACTIVE) {
                    CookieHelper.eraseCookie(request, response, CREDENTIAL_KEY, CREDENTIAL_TOKEN);
                    session.invalidate();
                    principal = null;
                }
            } else {
                Optional<String> credentialKey = CookieHelper.readCookie(request, CREDENTIAL_KEY);
                Optional<String> credentialToken = CookieHelper.readCookie(request, CREDENTIAL_TOKEN);
                if (credentialKey.isPresent() && credentialToken.isPresent()) {
                    UserService userService = UserServiceImpl.getInstance();
                    Optional<User> user = userService.findIfExistsByCredentialsKeyAndToken(credentialKey.get(), credentialToken.get());
                    if (user.isPresent() && user.get().getStatus() == User.Status.ACTIVE) {
                        session.setAttribute(PRINCIPAL, user.get());
                    } else {
                        CookieHelper.eraseCookie(request, response, CREDENTIAL_KEY, CREDENTIAL_TOKEN);
                    }
                }
            }

            AccessRole currentRole = AccessRole.GUEST;
            if (principal != null) {
                currentRole = AccessRole.valueOf(principal.getRole().name());
            } else if(session.getAttribute(INACTIVE_PRINCIPAL) != null) {
                currentRole = AccessRole.INACTIVE_USER;
            }

            if (!RestrictedCommand.hasAccess(commandType.get(), currentRole)) {
                response.sendRedirect(MAIN_URL);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}