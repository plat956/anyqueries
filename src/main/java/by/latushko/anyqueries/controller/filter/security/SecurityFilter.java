package by.latushko.anyqueries.controller.filter.security;

import by.latushko.anyqueries.controller.command.CommandType;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;
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
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;

@WebFilter(filterName = "securityFilter", urlPatterns = "/controller")
public class SecurityFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getParameter(RequestParameter.COMMAND) != null) {
            HttpSession session = request.getSession();

            User principal = null;
            if (session.getAttribute(PRINCIPAL) != null) {
                principal = (User) session.getAttribute(PRINCIPAL);
                if(!principal.getStatus().equals(User.Status.ACTIVE)) {
                    //todo: logout user! principal = null, session invalidate and maybe redirect with message as well
                }
            } else {
                Optional<String> credentialKey = CookieHelper.readCookie(request, CREDENTIAL_KEY);
                Optional<String> credentialToken = CookieHelper.readCookie(request, CREDENTIAL_TOKEN);
                if (credentialKey.isPresent() && credentialToken.isPresent()) {
                    UserService userService = UserServiceImpl.getInstance();
                    Optional<User> user = userService.findUserByCredentialKey(credentialKey.get());
                    if (user.isPresent() && user.get().getStatus().equals(User.Status.ACTIVE)) {
                        String tokenSource = userService.getCredentialTokenSource(user.get());
                        PasswordEncoder passwordEncoder = BCryptPasswordEncoder.getInstance();
                        if(passwordEncoder.check(tokenSource, credentialToken.get())) {
                            boolean authorized = userService.authorize(user.get(), request, response, false, false); //todo think about not to send req & resp to the service
                            if (authorized) {
                                principal = user.get();
                            }
                        }
                    } else {
                        //todo remove credential cookies, don't keep trash!
                    }
                }
            }

            User.Role currentRole = User.Role.GUEST;
            if (principal != null) {
                currentRole = principal.getRole();
            }

            String command = request.getParameter(RequestParameter.COMMAND);
            Optional<CommandType> commandType = CommandType.getByName(command);

            if (commandType.isPresent() && !RestrictedCommand.hasAccess(commandType.get(), currentRole)) {
                response.sendRedirect(PagePath.MAIN_URL);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}