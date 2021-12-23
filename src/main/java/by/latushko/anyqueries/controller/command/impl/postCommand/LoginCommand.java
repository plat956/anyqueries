package by.latushko.anyqueries.controller.command.impl.postCommand;

import by.latushko.anyqueries.controller.command.*;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.*;

public class LoginCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter(RequestParameter.LOGIN);
        String password = request.getParameter(RequestParameter.PASSWORD);

        UserService userService = UserServiceImpl.getInstance();
        PasswordEncoder passwordEncoder = BCryptPasswordEncoder.getInstance();

        Optional<User> user = userService.findUserByLogin(login);
        ResponseMessage message = new ResponseMessage(INFO, "Неверные данные аутентификации");
        if(user.isPresent()) {
            if(passwordEncoder.check(password, user.get().getPassword())) {
                message = switch (user.get().getStatus()) {
                    case ACTIVE -> {
                        boolean remember = true; //todo - read "remember me" checkbox value from form parameters
                        boolean result = userService.authorize(user.get(), request, response, remember);
                        if(result) {
                            yield new ResponseMessage(INFO, "Вы успешно вошли в учетную запись");
                        } else {
                            yield new ResponseMessage(DANGER, "При входе возникла непредвиденная ошибка");
                        }
                    }
                    case INACTIVE -> new ResponseMessage(WARNING, "Учетная запись не активирована");
                    case BANNED -> new ResponseMessage(DANGER, "Учетная запись заблокирована");
                    //todo default -> ???
                };
            }
        }

        request.getSession().setAttribute(SessionAttribute.MESSAGE, message);
        return new PreparedResponse(PagePath.LOGIN_URL, PreparedResponse.RoutingType.REDIRECT);
    }
}
