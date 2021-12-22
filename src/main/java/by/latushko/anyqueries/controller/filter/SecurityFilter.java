package by.latushko.anyqueries.controller.filter;

import by.latushko.anyqueries.controller.command.CommandType;
import by.latushko.anyqueries.controller.command.PagePath;
import by.latushko.anyqueries.controller.command.RequestParameter;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.checkerframework.checker.nullness.Opt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebFilter("/controller")
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(); //todo: false parameter??? think about it

        User principal = null;
        if(session != null && session.getAttribute("principal") != null) {
            principal = (User) session.getAttribute("principal");
        } else {
            String credentialKey = readCookie(request, "CREDENTIAL_KEY");
            String credentialToken = readCookie(request, "CREDENTIAL_TOKEN");
            if(credentialKey != null && credentialToken != null) {
                UserService userService = new UserServiceImpl();
                Optional<User> userOptional = userService.findUserByCredentialKey(credentialKey);
                if(userOptional.isPresent()) {
                    User user = userOptional.get();
                    //if(passwordEncoder.check(generateTokenByAlgprithm(user), credentialToken)) {
                        userService.authorize(user, request, response);
                        //principal = user;
                    //}
                }
            }
        }

        User.Role currentRole = User.Role.ROLE_USER; //todo - get current userRole from session, залочить все jsp и прочее что не проходит по контроллерам
        //User principal = (User)session.getAttribute("principal"); //сделать role guest?

        String command = request.getParameter(RequestParameter.COMMAND);
        CommandType commandType;
        try {
            commandType = CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            commandType = null;
        }


        if(RestrictedCommand.commands.containsKey(commandType)) {
            List<User.Role> roles = RestrictedCommand.commands.get(commandType);
            if(roles != null && !roles.isEmpty() && !roles.contains(currentRole)) {
                response.sendRedirect(PagePath.MAIN_URL);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String readCookie(HttpServletRequest request, String key) {
        if(request != null && request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(c -> key.equals(c.getName()))
                    .map(Cookie::getValue)
                    .findAny().orElse(null);
        } else {
            return null;
        }
    }
}