package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.service.CategoryService;
import by.latushko.anyqueries.service.impl.CategoryServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.PagePath.EDIT_CATEGORY_PAGE;
import static by.latushko.anyqueries.controller.command.identity.PagePath.ERROR_404_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.VALIDATION_RESULT;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.CATEGORY;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.ID;

public class EditCategoryPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.valueOf(request.getParameter(ID));
        HttpSession session = request.getSession();
        CategoryService categoryService = CategoryServiceImpl.getInstance();
        Optional<Category> category = categoryService.findById(id);
        if(category.isEmpty()) {
            return new CommandResult(ERROR_404_PAGE, FORWARD);
        }
        if(request.getAttribute(VALIDATION_RESULT) == null) {
            request.setAttribute(CATEGORY, category.get());
        }
        return new CommandResult(EDIT_CATEGORY_PAGE, FORWARD);
    }
}
