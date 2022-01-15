package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.model.entity.Category;
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

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.CATEGORIES_URL;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.CREATE_CATEGORY_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.COLOR;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.NAME;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.VALIDATION_RESULT;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class CreateCategoryCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        CommandResult commandResult = new CommandResult(CREATE_CATEGORY_URL, REDIRECT);
        ResponseMessage message;

        FormValidator validator = CategoryFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }

        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);

        String name = request.getParameter(NAME);
        String color = request.getParameter(COLOR);
        CategoryService categoryService = CategoryServiceImpl.getInstance();
        boolean exists = categoryService.checkIfExistsByName(name);
        if(exists) {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_CATEGORY_WRONG));
            session.setAttribute(MESSAGE, message);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        Optional<Category> result = categoryService.create(name, color);
        if(result.isPresent()) {
            message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_CATEGORY_CREATED));
            session.setAttribute(MESSAGE, message);
            return new CommandResult(CATEGORIES_URL, REDIRECT);
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
            session.setAttribute(MESSAGE, message);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
    }
}
