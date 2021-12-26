package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageKey;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Type.POPUP;

public class ActivateUserCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String hash = request.getParameter(RequestParameter.HASH);
        RegistrationService registrationService = RegistrationServiceImpl.getInstance();
        Optional<User> activatedUser = registrationService.activateUserByHash(hash);
        HttpSession session = request.getSession();
        String userLang = CookieHelper.readCookie(request, CookieName.LANG).orElse(null);
        MessageManager manager = MessageManager.getManager(userLang);
        ResponseMessage message;
        String redirectUrl;
        if(activatedUser.isPresent()) {
            message = new ResponseMessage(POPUP, SUCCESS, manager.getMessage(MessageKey.MESSAGE_REGISTRATION_SUCCESS_TITLE),
                    manager.getMessage(MessageKey.MESSAGE_REGISTRATION_SUCCESS_NOTICE));

            UserService userService = UserServiceImpl.getInstance();
            session.setAttribute(SessionAttribute.PRINCIPAL, activatedUser.get());
            userService.updateLastLoginDate(activatedUser.get());

            redirectUrl = PagePath.MAIN_URL;
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MessageKey.MESSAGE_ACTIVATION_FAIL));
            redirectUrl = PagePath.LOGIN_URL;
        }

        session.setAttribute(SessionAttribute.MESSAGE, message);
        return new CommandResult(redirectUrl, CommandResult.RoutingType.REDIRECT);
    }
}