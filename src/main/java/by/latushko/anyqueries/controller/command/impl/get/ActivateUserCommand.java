package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Type.POPUP;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.LOGIN_URL;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.QUESTIONS_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.HASH;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class ActivateUserCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String hash = request.getParameter(HASH);
        RegistrationService registrationService = RegistrationServiceImpl.getInstance();
        Optional<User> activatedUser = registrationService.activateUserByHash(hash);
        HttpSession session = request.getSession();
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        ResponseMessage message;
        String redirectUrl = QUESTIONS_URL;
        if(activatedUser.isPresent()) {
            User user = activatedUser.get();
            message = new ResponseMessage(POPUP, SUCCESS, manager.getMessage(MESSAGE_REGISTRATION_SUCCESS_TITLE),
                    manager.getMessage(MESSAGE_REGISTRATION_SUCCESS_NOTICE));
            session.removeAttribute(INACTIVE_PRINCIPAL);
            session.setAttribute(PRINCIPAL, user);
            UserService userService = UserServiceImpl.getInstance();
            userService.updateLastLoginDate(user);
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ACTIVATION_FAIL));
            if(session.getAttribute(INACTIVE_PRINCIPAL) == null) {
                redirectUrl = LOGIN_URL;
            }
        }
        session.setAttribute(MESSAGE, message);
        return new CommandResult(redirectUrl, REDIRECT);
    }
}