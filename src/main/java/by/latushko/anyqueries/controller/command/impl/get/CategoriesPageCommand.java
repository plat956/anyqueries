package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.service.CategoryService;
import by.latushko.anyqueries.service.impl.CategoryServiceImpl;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.PagePath.CATEGORIES_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.CATEGORIES;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.TOTAL_PAGES;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.QUERY;
import static by.latushko.anyqueries.controller.command.impl.get.LiveSearchCommand.limitQueryString;

public class CategoriesPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String name = limitQueryString(request.getParameter(QUERY));
        String pageParameter = request.getParameter(PAGE);
        RequestPage page = new RequestPage(pageParameter);
        CategoryService categoryService = CategoryServiceImpl.getInstance();
        Paginated<Category> categories = categoryService.findAllPaginatedByNameLikeOrderByNameAsc(page, name);
        request.setAttribute(TOTAL_PAGES, categories.getTotalPages());
        request.setAttribute(CATEGORIES, categories.getContent());
        return new CommandResult(CATEGORIES_PAGE, FORWARD);
    }
}
