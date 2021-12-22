package by.latushko.anyqueries.controller.command.impl.postCommand;

import by.latushko.anyqueries.controller.command.*;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;
import by.latushko.anyqueries.util.telegram.TelegramBot;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.INFO;

public class LoginCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter(RequestParameter.LOGIN);
        String password = request.getParameter(RequestParameter.PASSWORD);

        UserService userService = new UserServiceImpl();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        ResponseMessage message = new ResponseMessage(INFO, "Неверные данные аутентификации");
        Optional<User> userOptional = userService.findUserByLogin(login);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if(passwordEncoder.check(password, user.getPassword())) {
                if(user.getStatus().equals(User.Status.ACTIVE)) {
                    userService.authorize(user, request, response);
                    message = new ResponseMessage(INFO, "Вы успешно вошли в учетную запись");
                }
                //todo handle other user statuses
            }
        }

        request.getSession().setAttribute(RequestAttribute.MESSAGE, message);
        return new PreparedResponse(PagePath.LOGIN_URL, PreparedResponse.RoutingType.REDIRECT);
    }
}
