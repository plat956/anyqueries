package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.UserFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.EDIT_USER_URL;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.USERS_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.VALIDATION_RESULT;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class EditUserCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Long id = getLongParameter(request, ID);
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        String previousPage = request.getParameter(RequestParameter.PREVIOUS_PAGE);
        if(previousPage == null || previousPage.isEmpty()) {
            previousPage = USERS_URL;
        }
        session.setAttribute(SessionAttribute.PREVIOUS_PAGE, previousPage);
        ResponseMessage message;
        if(id == null) {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
            session.setAttribute(MESSAGE, message);
            return new CommandResult(USERS_URL, REDIRECT);
        }
        CommandResult commandResult = new CommandResult(EDIT_USER_URL + id, REDIRECT);
        FormValidator validator = UserFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        UserService userService = UserServiceImpl.getInstance();
        String email = request.getParameter(EMAIL);
        if(email != null && !email.isEmpty() && userService.existsByEmailExceptUserId(email, id)) {
            validationResult.setError(EMAIL, LABEL_EMAIL_EXISTS);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        String telegram = request.getParameter(TELEGRAM);
        if(telegram != null && !telegram.isEmpty() && userService.existsByTelegramExceptUserId(telegram, id)) {
            validationResult.setError(TELEGRAM, LABEL_TELEGRAM_EXISTS);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        String login = request.getParameter(LOGIN);
        if(login != null && !login.isEmpty() && userService.existsByLoginExceptUserId(login, id)) {
            validationResult.setError(LOGIN, LABEL_LOGIN_EXISTS);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        String firstName = request.getParameter(FIRST_NAME);
        String lastName = request.getParameter(LAST_NAME);
        String middleName = request.getParameter(MIDDLE_NAME);
        User.Status status = User.Status.valueOf(request.getParameter(STATUS));
        User.Role role = User.Role.valueOf(request.getParameter(ROLE));
        boolean result = userService.update(id, firstName, lastName, middleName, email, telegram, login, status, role);
        if (result) {
            message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_SAVE_SUCCESSFUL));
            session.setAttribute(MESSAGE, message);
            return new CommandResult(previousPage, REDIRECT);
        } else {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_SAVE_FAILED));
            session.setAttribute(MESSAGE, message);
            return commandResult;
        }
    }
}
