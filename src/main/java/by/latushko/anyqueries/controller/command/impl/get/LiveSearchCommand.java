package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.CategoryService;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.CategoryServiceImpl;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;

import java.util.List;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.DATA;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;
import static by.latushko.anyqueries.util.AppProperty.APP_SEARCH_PREDICTIONS_LIMIT;
import static by.latushko.anyqueries.util.AppProperty.APP_SEARCH_QUERY_MAXLENGTH;
import static by.latushko.anyqueries.util.http.MimeType.APPLICATION_JSON;

public class LiveSearchCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON);
        HttpSession session = request.getSession();
        JSONArray resultArray = new JSONArray();
        String queryString = limitQueryString(request.getParameter(QUERY_STRING));
        if(queryString == null || queryString.isEmpty()) {
            return new CommandResult(resultArray.toString(), DATA);
        }
        User user = (User) session.getAttribute(PRINCIPAL);
        boolean users = request.getParameter(USERS) != null;
        boolean categories = request.getParameter(CATEGORIES) != null;
        List<String> result = List.of();
        if(users) {
            if (user != null && user.getRole() == User.Role.ADMIN) {
                UserService userService = UserServiceImpl.getInstance();
                result = userService.findLoginByLoginContainsOrderByLoginAscLimitedTo(queryString, APP_SEARCH_PREDICTIONS_LIMIT);
            }
        } else if(categories) {
            CategoryService categoryService = CategoryServiceImpl.getInstance();
            result = categoryService.findNameByNameContainsOrderByNameAscLimitedTo(queryString, APP_SEARCH_PREDICTIONS_LIMIT);
        } else {
            Long categoryId = getLongParameter(request, CATEGORY);
            Long authorId = request.getParameter(CURRENT) != null && user != null ? user.getId() : null;
            QuestionService questionService = QuestionServiceImpl.getInstance();
            result = questionService.findTitleByTitleContainsAndCategoryIdAndAuthorIdOrderByTitleAscLimitedTo(queryString, categoryId,
                    authorId, APP_SEARCH_PREDICTIONS_LIMIT);
        }
        result.forEach(i -> resultArray.put(i));
        return new CommandResult(resultArray.toString(), DATA);
    }

    static String limitQueryString(String queryString) {
        if(queryString != null && queryString.length() > APP_SEARCH_QUERY_MAXLENGTH) {
            queryString = queryString.substring(0, APP_SEARCH_QUERY_MAXLENGTH);
        }
        return queryString;
    }
}