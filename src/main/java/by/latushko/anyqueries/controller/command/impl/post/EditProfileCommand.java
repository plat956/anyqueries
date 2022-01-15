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
import by.latushko.anyqueries.validator.impl.ProfileFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.EDIT_PROFILE_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class EditProfileCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        CommandResult commandResult = new CommandResult(EDIT_PROFILE_URL, REDIRECT);
        FormValidator validator = ProfileFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        UserService userService = UserServiceImpl.getInstance();
        User currentUser = (User) session.getAttribute(PRINCIPAL);
        String email = request.getParameter(EMAIL);
        if(email != null && !email.isEmpty() && userService.existsByEmailExceptUserId(email, currentUser.getId())) {
            validationResult.setError(EMAIL, LABEL_EMAIL_EXISTS);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        String telegram = request.getParameter(TELEGRAM);
        if(telegram != null && !telegram.isEmpty() && userService.existsByTelegramExceptUserId(telegram, currentUser.getId())) {
            validationResult.setError(TELEGRAM, LABEL_TELEGRAM_EXISTS);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        String login = request.getParameter(LOGIN);
        if(login != null && !login.isEmpty() && userService.existsByLoginExceptUserId(login, currentUser.getId())) {
            validationResult.setError(LOGIN, LABEL_LOGIN_EXISTS);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);

        String firstName = request.getParameter(FIRST_NAME);
        String lastName = request.getParameter(LAST_NAME);
        String middleName = request.getParameter(MIDDLE_NAME);
        boolean result = userService.update(currentUser, firstName, lastName, middleName, email, telegram, login);
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
