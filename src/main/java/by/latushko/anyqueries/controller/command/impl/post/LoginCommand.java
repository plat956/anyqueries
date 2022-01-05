package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.LoginFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Type.POPUP;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Type.TOAST;
import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_KEY;
import static by.latushko.anyqueries.controller.command.identity.CookieName.CREDENTIAL_TOKEN;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.LOGIN_URL;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.QUESTIONS_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;
import static by.latushko.anyqueries.util.AppProperty.APP_COOKIE_ALIVE_SECONDS;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class LoginCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String redirectUrl = LOGIN_URL;
        HttpSession session = request.getSession();
        FormValidator validator = LoginFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if (!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return new CommandResult(redirectUrl, REDIRECT);
        }
        UserService userService = UserServiceImpl.getInstance();
        String userLang = CookieHelper.readCookie(request, CookieName.LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        String rememberMe = request.getParameter(REMEMBER_ME);
        Optional<User> user = userService.findByLoginAndPassword(login, password);
        if (user.isEmpty()) {
            session.setAttribute(MESSAGE, new ResponseMessage(DANGER, manager.getMessage(MESSAGE_LOGIN_WRONG)));
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return new CommandResult(redirectUrl, REDIRECT);
        }
        ResponseMessage message = switch (user.get().getStatus()) {
            case ACTIVE -> {
                redirectUrl = QUESTIONS_URL;
                session.setAttribute(PRINCIPAL, user.get());
                boolean isFirstLoginTime = user.get().getLastLoginDate() == null;
                userService.updateLastLoginDate(user.get());
                if (rememberMe != null) {
                    CookieHelper.addCookie(response, CREDENTIAL_KEY, user.get().getCredentialKey(), APP_COOKIE_ALIVE_SECONDS);
                    String token = userService.generateCredentialToken(user.get());
                    CookieHelper.addCookie(response, CREDENTIAL_TOKEN, token, APP_COOKIE_ALIVE_SECONDS);
                }
                if (isFirstLoginTime) {
                    yield new ResponseMessage(POPUP, SUCCESS, manager.getMessage(MESSAGE_REGISTRATION_SUCCESS_TITLE),
                            manager.getMessage(MESSAGE_REGISTRATION_SUCCESS_NOTICE));
                } else {
                    yield new ResponseMessage(TOAST, SUCCESS, manager.getMessage(MESSAGE_LOGIN_SUCCESS),
                            manager.getMessage(MESSAGE_LOGIN_WELCOME, user.get().getFio()));
                }
            }
            case INACTIVE -> {
                redirectUrl = QUESTIONS_URL;
                session.setAttribute(INACTIVE_PRINCIPAL, user.get());
                yield null;
            }
            case BANNED -> {
                session.setAttribute(VALIDATION_RESULT, validationResult);
                yield new ResponseMessage(DANGER, manager.getMessage(MESSAGE_LOGIN_BANNED));
            }
            default -> {
                session.setAttribute(VALIDATION_RESULT, validationResult);
                yield new ResponseMessage(DANGER, manager.getMessage(MESSAGE_LOGIN_FAIL));
            }
        };
        session.setAttribute(MESSAGE, message);
        return new CommandResult(redirectUrl, REDIRECT);
    }
}
