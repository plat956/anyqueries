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
    private final UserService userService = UserServiceImpl.getInstance();

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
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        boolean rememberMe = request.getParameter(REMEMBER_ME) != null;
        String userLang = CookieHelper.readCookie(request, CookieName.LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        Optional<User> userOptional = userService.findByLoginAndPassword(login, password);
        ResponseMessage message;
        if (userOptional.isEmpty()) {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_LOGIN_WRONG));
            session.setAttribute(MESSAGE, message);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return new CommandResult(redirectUrl, REDIRECT);
        }
        User user = userOptional.get();
        message = buildResponseMessageAndAuthorize(session, response, manager, validationResult, user, rememberMe);
        session.setAttribute(MESSAGE, message);
        if(user.getStatus() != User.Status.BANNED) {
            redirectUrl = QUESTIONS_URL;
        }
        return new CommandResult(redirectUrl, REDIRECT);
    }

    private ResponseMessage buildResponseMessageAndAuthorize(HttpSession session, HttpServletResponse response, MessageManager manager,
                                                 ValidationResult validationResult, User user, boolean rememberMe) {
        ResponseMessage message = switch (user.getStatus()) {
            case ACTIVE -> {
                authorize(session, response, user, rememberMe);
                if (user.getLastLoginDate() == null) {
                    yield new ResponseMessage(POPUP, SUCCESS, manager.getMessage(MESSAGE_REGISTRATION_SUCCESS_TITLE),
                            manager.getMessage(MESSAGE_REGISTRATION_SUCCESS_NOTICE));
                } else {
                    yield new ResponseMessage(TOAST, SUCCESS, manager.getMessage(MESSAGE_LOGIN_SUCCESS),
                            manager.getMessage(MESSAGE_LOGIN_WELCOME, user.getFio()));
                }
            }
            case INACTIVE -> {
                session.setAttribute(INACTIVE_PRINCIPAL, user);
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
        return message;
    }

    private void authorize(HttpSession session, HttpServletResponse response, User user, boolean rememberMe) {
        session.setAttribute(PRINCIPAL, user);
        userService.updateLastLoginDate(user);
        if (rememberMe) {
            CookieHelper.addCookie(response, CREDENTIAL_KEY, user.getCredentialKey(), APP_COOKIE_ALIVE_SECONDS);
            String token = userService.generateCredentialToken(user);
            CookieHelper.addCookie(response, CREDENTIAL_TOKEN, token, APP_COOKIE_ALIVE_SECONDS);
        }
    }
}
