package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.service.CategoryService;
import by.latushko.anyqueries.service.impl.CategoryServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.CategoryFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.CATEGORIES_URL;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.EDIT_CATEGORY_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.VALIDATION_RESULT;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class EditCategoryCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Long id = getLongParameter(request, ID);
        String previousPage = request.getParameter(RequestParameter.PREVIOUS_PAGE);
        if(previousPage == null || previousPage.isEmpty()) {
            previousPage = CATEGORIES_URL;
        }
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        ResponseMessage message;
        if(id == null) {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
            session.setAttribute(MESSAGE, message);
            return new CommandResult(CATEGORIES_URL, REDIRECT);
        }
        CommandResult commandResult = new CommandResult(EDIT_CATEGORY_URL + id, REDIRECT);
        FormValidator validator = CategoryFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        String name = request.getParameter(NAME);
        String color = request.getParameter(COLOR);
        CategoryService categoryService = CategoryServiceImpl.getInstance();
        boolean exists = categoryService.existsByNameAndIdNot(name, id);
        if(exists) {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_CATEGORY_WRONG));
            session.setAttribute(MESSAGE, message);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        boolean result = categoryService.update(id, name, color);
        if(result) {
            message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_CATEGORY_UPDATED));
            session.setAttribute(MESSAGE, message);
            return new CommandResult(previousPage, REDIRECT);
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
            session.setAttribute(MESSAGE, message);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
    }
}
