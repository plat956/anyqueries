package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonString;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.JSON;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.QUERY_STRING;

public class LiveSearchCommand implements Command {
    private static final int LIVE_SEARCH_PREDICTIONS_LIMIT = 10;
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String queryString = request.getParameter(QUERY_STRING);
        QuestionService questionService = QuestionServiceImpl.getInstance();
        List<String> result = questionService.findTitleLikeOrderedAndLimited(queryString, LIVE_SEARCH_PREDICTIONS_LIMIT);
        JsonArray array = new JsonArray();
        for(String item: result) {
            array.addValue(new JsonString().setValue(item));
        }
        return new CommandResult(array.toString(), JSON);
    }
}