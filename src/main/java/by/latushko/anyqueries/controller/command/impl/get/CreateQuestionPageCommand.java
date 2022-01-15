package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.service.CategoryService;
import by.latushko.anyqueries.service.impl.CategoryServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.PagePath.CREATE_QUESTION_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.CATEGORIES;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.CATEGORY;

public class CreateQuestionPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        CategoryService categoryService = CategoryServiceImpl.getInstance();
        List<Category> categories = categoryService.findAllOrderByNameAsc();
        request.setAttribute(CATEGORIES, categories);
        Long category = getLongParameter(request, CATEGORY);
        request.setAttribute(CATEGORY, category);
        return new CommandResult(CREATE_QUESTION_PAGE, FORWARD);
    }
}
