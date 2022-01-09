package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonString;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.JSON;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;
import static by.latushko.anyqueries.service.QuestionService.QUESTION_SEARCH_QUERY_MAX_LENGTH;

public class LiveSearchCommand implements Command {
    private static final int LIVE_SEARCH_PREDICTIONS_LIMIT = 10;
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(PRINCIPAL);
        String queryString = request.getParameter(QUERY_STRING);
        if(queryString != null && queryString.length() > QUESTION_SEARCH_QUERY_MAX_LENGTH) {
            queryString = queryString.substring(0, QUESTION_SEARCH_QUERY_MAX_LENGTH);
        }
        String categoryParameter = request.getParameter(CATEGORY);
        String currentUserParameter = request.getParameter(CURRENT);
        Long categoryId = categoryParameter != null ? Long.valueOf(categoryParameter) : null;
        Long authorId = currentUserParameter != null ? user.getId() : null;

        QuestionService questionService = QuestionServiceImpl.getInstance();
        List<String> result = questionService.findTitleByTitleLikeAndCategoryIdAndAuthorIdOrderedAndLimited(queryString, categoryId,
                authorId, LIVE_SEARCH_PREDICTIONS_LIMIT);
        JsonArray array = new JsonArray();
        for(String item: result) {
            array.addValue(new JsonString().setValue(item));
        }
        return new CommandResult(array.toString(), JSON);
    }
}