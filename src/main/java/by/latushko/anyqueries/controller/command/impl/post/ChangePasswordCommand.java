package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.ChangePasswordFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.CHANGE_PASSWORD_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.PASSWORD_NEW;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.PASSWORD_OLD;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class ChangePasswordCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        CommandResult commandResult = new CommandResult(CHANGE_PASSWORD_URL, REDIRECT);
        FormValidator validator = ChangePasswordFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if (!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }

        String oldPassword = request.getParameter(PASSWORD_OLD);

        User user = (User) session.getAttribute(PRINCIPAL);
        UserService userService = UserServiceImpl.getInstance();
        if(!userService.checkPassword(user, oldPassword)) {
            validationResult.setError(PASSWORD_OLD, MESSAGE_PASSWORD_OLD_INCORRECT);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }

        String password = request.getParameter(PASSWORD_NEW);
        boolean result = userService.changePassword(user, password);

        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        ResponseMessage message;
        if (result) {
            message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_SAVE_SUCCESSFUL));
        } else {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_SAVE_FAILED));
        }

        session.setAttribute(MESSAGE, message);
        return commandResult;
    }
}
