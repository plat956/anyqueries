package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonString;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.RESPOND_WITH_JSON;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.QUERY_STRING;

public class LiveSearchCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String queryString = request.getParameter(QUERY_STRING);

        //todo select data from db and put results in array below

        JsonArray array = new JsonArray();
        array.addValue(new JsonString().setValue("Andorra"));
        array.addValue(new JsonString().setValue("United Arab Emirates"));
        array.addValue(new JsonString().setValue("Afghanistan"));

        return new CommandResult(array.toString(), RESPOND_WITH_JSON);
    }
}
