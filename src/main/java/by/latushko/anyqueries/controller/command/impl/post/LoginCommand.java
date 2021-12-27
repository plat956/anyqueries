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
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageKey;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.LoginFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.*;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Type.POPUP;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Type.TOAST;
import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_KEY;
import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_TOKEN;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.INACTIVE_PRINCIPAL;
import static by.latushko.anyqueries.util.AppProperty.APP_COOKIE_ALIVE_SECONDS;

public class LoginCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter(RequestParameter.LOGIN);
        String password = request.getParameter(RequestParameter.PASSWORD);
        String rememberMe = request.getParameter(RequestParameter.REMEMBER_ME);

        UserService userService = UserServiceImpl.getInstance();

        HttpSession session = request.getSession();
        FormValidator validator = new LoginFormValidator();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        String redirectUrl = PagePath.LOGIN_URL;
        if(validationResult.getStatus()) {
            String userLang = CookieHelper.readCookie(request, CookieName.LANG).orElse(null);
            MessageManager manager = MessageManager.getManager(userLang);
            Optional<User> user = userService.findIfExistsByLoginAndPassword(login, password);
            ResponseMessage message;
            if (user.isPresent()) {
                message = switch (user.get().getStatus()) {
                    case ACTIVE -> {
                        session.setAttribute(SessionAttribute.PRINCIPAL, user.get());
                        boolean isFirstLoginTime = user.get().getLastLoginDate() == null;
                        userService.updateLastLoginDate(user.get());

                        if(rememberMe != null) {
                            CookieHelper.addCookie(response, CREDENTIAL_KEY, user.get().getCredentialKey(), APP_COOKIE_ALIVE_SECONDS);
                            String token = userService.getCredentialToken(user.get());
                            CookieHelper.addCookie(response, CREDENTIAL_TOKEN, token, APP_COOKIE_ALIVE_SECONDS);
                        }

                        redirectUrl = PagePath.MAIN_URL;

                        if (isFirstLoginTime) {
                            yield new ResponseMessage(POPUP, SUCCESS, manager.getMessage(MessageKey.MESSAGE_REGISTRATION_SUCCESS_TITLE),
                                    manager.getMessage(MessageKey.MESSAGE_REGISTRATION_SUCCESS_NOTICE));
                        } else {
                            yield new ResponseMessage(TOAST, SUCCESS, manager.getMessage(MessageKey.MESSAGE_LOGIN_SUCCESS),
                                    manager.getMessage(MessageKey.MESSAGE_LOGIN_WELCOME, user.get().getFio()));
                        }
                    }
                    case INACTIVE -> {
                        session.setAttribute(INACTIVE_PRINCIPAL, user.get());
                        redirectUrl = PagePath.MAIN_URL;
                        yield null;
                    }
                    case BANNED -> {
                        session.setAttribute(SessionAttribute.VALIDATION_RESULT, validationResult);
                        yield new ResponseMessage(DANGER, manager.getMessage(MessageKey.MESSAGE_LOGIN_BANNED));
                    }
                    default -> {
                        session.setAttribute(SessionAttribute.VALIDATION_RESULT, validationResult);
                        yield new ResponseMessage(DANGER, manager.getMessage(MessageKey.MESSAGE_LOGIN_FAIL));
                    }
                };
            } else {
                message = new ResponseMessage(DANGER, manager.getMessage(MessageKey.MESSAGE_LOGIN_WRONG));
                session.setAttribute(SessionAttribute.VALIDATION_RESULT, validationResult);
            }

            session.setAttribute(SessionAttribute.MESSAGE, message);
        } else {
            session.setAttribute(SessionAttribute.VALIDATION_RESULT, validationResult);
        }
        return new CommandResult(redirectUrl, CommandResult.RoutingType.REDIRECT);
    }
}
