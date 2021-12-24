package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.LoginFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.*;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Type.TOAST;

public class LoginCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter(RequestParameter.LOGIN);
        String password = request.getParameter(RequestParameter.PASSWORD);

        UserService userService = UserServiceImpl.getInstance();
        PasswordEncoder passwordEncoder = BCryptPasswordEncoder.getInstance();

        FormValidator validator = new LoginFormValidator();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(validationResult.getStatus()) {
            Optional<User> user = userService.findUserByLogin(login);
            ResponseMessage message = new ResponseMessage(INFO, "Неверные данные аутентификации");
            if (user.isPresent()) {
                if (passwordEncoder.check(password, user.get().getPassword())) {
                    message = switch (user.get().getStatus()) {
                        case ACTIVE -> {
                            boolean remember = true; //todo - read "remember me" checkbox value from form parameters
                            boolean result = userService.authorize(user.get(), request, response, remember, true);
                            if (result) {
                                String fio = userService.getUserFio(user.get());
                                yield new ResponseMessage(TOAST, SUCCESS, "Вход выполнен", "Добро пожаловать, " + fio);
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
            return new CommandResult(PagePath.MAIN_URL, CommandResult.RoutingType.REDIRECT);
        } else {
            request.getSession().setAttribute(SessionAttribute.VALIDATION_RESULT, validationResult);
            return new CommandResult(PagePath.LOGIN_URL, CommandResult.RoutingType.REDIRECT);
        }
    }
}
