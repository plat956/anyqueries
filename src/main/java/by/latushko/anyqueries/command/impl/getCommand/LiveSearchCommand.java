package by.latushko.anyqueries.command.impl.getCommand;

import by.latushko.anyqueries.command.Command;
import by.latushko.anyqueries.command.RequestParameter;
import by.latushko.anyqueries.command.ResponseParameter;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonString;
import jakarta.servlet.http.HttpServletRequest;

import static by.latushko.anyqueries.command.ResponseParameter.RoutingType.RESPOND_WITH_JSON;

public class LiveSearchCommand implements Command {
    @Override
    public ResponseParameter execute(HttpServletRequest request) {
        String queryString = request.getParameter(RequestParameter.QUERY_STRING);

        //todo search in db
        JsonArray array = new JsonArray();
        array.addValue(new JsonString().setValue("Andorra"));
        array.addValue(new JsonString().setValue("United Arab Emirates"));
        array.addValue(new JsonString().setValue("Afghanistan"));

        return new ResponseParameter(array.toString(), RESPOND_WITH_JSON);
    }
}
