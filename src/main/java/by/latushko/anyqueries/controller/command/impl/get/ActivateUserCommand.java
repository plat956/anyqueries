package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.*;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageKey;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Type.POPUP;

public class ActivateUserCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String hash = request.getParameter(RequestParameter.HASH);
        RegistrationService registrationService = RegistrationServiceImpl.getInstance();
        boolean result = registrationService.activateUserByHash(hash);

        String userLang = CookieHelper.readCookie(request, CookieName.LANG).orElse(null);
        MessageManager manager = MessageManager.getManager(userLang);
        ResponseMessage message;
        if(result) {
            message = new ResponseMessage(POPUP, SUCCESS, manager.getMessage(MessageKey.MESSAGE_REGISTRATION_SUCCESS_TITLE),
                    manager.getMessage(MessageKey.MESSAGE_REGISTRATION_SUCCESS_NOTICE));
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MessageKey.MESSAGE_ACTIVATION_FAIL));
        }
        //todo: log-in automatically if success & redirect to the main page must be here
        request.getSession().setAttribute(SessionAttribute.MESSAGE, message);
        return new CommandResult(PagePath.MAIN_URL, CommandResult.RoutingType.REDIRECT);
    }
}