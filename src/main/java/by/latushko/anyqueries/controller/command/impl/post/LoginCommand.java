package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.encryption.PasswordEncoder;
import by.latushko.anyqueries.util.encryption.impl.BCryptPasswordEncoder;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageKey;
import by.latushko.anyqueries.util.i18n.MessageManager;
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
            String userLang = CookieHelper.readCookie(request, CookieName.LANG).orElse(null);
            MessageManager manager = MessageManager.getManager(userLang);
            Optional<User> user = userService.findUserByLogin(login);
            ResponseMessage message = new ResponseMessage(DANGER, manager.getMessage(MessageKey.MESSAGE_LOGIN_WRONG));
            if (user.isPresent()) {
                if (passwordEncoder.check(password, user.get().getPassword())) {
                    message = switch (user.get().getStatus()) {
                        case ACTIVE -> {
                            boolean remember = true; //todo - read "remember me" checkbox value from form parameters
                            boolean result = userService.authorize(user.get(), request, response, remember, true);
                            if (result) {
                                yield new ResponseMessage(TOAST, SUCCESS, manager.getMessage(MessageKey.MESSAGE_LOGIN_SUCCESS),
                                        manager.getMessage(MessageKey.MESSAGE_LOGIN_WELCOME) + user.get().getFio());
                            } else {
                                yield new ResponseMessage(DANGER, manager.getMessage(MessageKey.MESSAGE_LOGIN_FAIL));
                            }
                        }
                        case INACTIVE -> new ResponseMessage(WARNING, manager.getMessage(MessageKey.MESSAGE_LOGIN_INACTIVE));
                        case BANNED -> new ResponseMessage(DANGER, manager.getMessage(MessageKey.MESSAGE_LOGIN_BANNED));
                        default -> new ResponseMessage(DANGER, manager.getMessage(MessageKey.MESSAGE_LOGIN_FAIL));
                    };
                }
            }

            request.getSession().setAttribute(SessionAttribute.MESSAGE, message);
            return new CommandResult(PagePath.MAIN_URL, CommandResult.RoutingType.REDIRECT); //todo при ошибке на логин а не на мэйн редирект
        } else {
            request.getSession().setAttribute(SessionAttribute.VALIDATION_RESULT, validationResult);
            return new CommandResult(PagePath.LOGIN_URL, CommandResult.RoutingType.REDIRECT);
        }
    }
}
