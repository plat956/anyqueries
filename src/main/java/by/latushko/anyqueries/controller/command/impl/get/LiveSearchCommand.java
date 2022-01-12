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
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonString;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.DATA;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;
import static by.latushko.anyqueries.service.QuestionService.QUESTION_SEARCH_QUERY_MAX_LENGTH;
import static by.latushko.anyqueries.util.http.MimeType.APPLICATION_JSON;

public class LiveSearchCommand implements Command {
    private static final int LIVE_SEARCH_PREDICTIONS_LIMIT = 7;
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(PRINCIPAL);
        String queryString = request.getParameter(QUERY_STRING);
        if(queryString != null && queryString.length() > QUESTION_SEARCH_QUERY_MAX_LENGTH) {
            queryString = queryString.substring(0, QUESTION_SEARCH_QUERY_MAX_LENGTH);
        }
        boolean users = request.getParameter(USERS) != null;
        boolean categories = request.getParameter(CATEGORIES) != null;

        List<String> result;
        if(users) {
            UserService userService = UserServiceImpl.getInstance();
            result = userService.findLoginByLoginLikeOrderedAndLimited(queryString, LIVE_SEARCH_PREDICTIONS_LIMIT);
        } else if(categories) {
            CategoryService categoryService = CategoryServiceImpl.getInstance();
            result = categoryService.findNameByNameLikeOrderedAndLimited(queryString, LIVE_SEARCH_PREDICTIONS_LIMIT);
        } else {
            String categoryParameter = request.getParameter(CATEGORY);
            String currentUserParameter = request.getParameter(CURRENT);
            Long categoryId = categoryParameter != null ? Long.valueOf(categoryParameter) : null;
            Long authorId = currentUserParameter != null ? user.getId() : null;
            QuestionService questionService = QuestionServiceImpl.getInstance();
            result = questionService.findTitleByTitleLikeAndCategoryIdAndAuthorIdOrderedAndLimited(queryString, categoryId,
                    authorId, LIVE_SEARCH_PREDICTIONS_LIMIT);
        }

        JsonArray array = new JsonArray();
        for(String item: result) {
            array.addValue(new JsonString().setValue(item));
        }
        return new CommandResult(array.toString(), DATA);
    }
}