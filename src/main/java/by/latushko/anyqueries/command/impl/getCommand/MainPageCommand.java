package by.latushko.anyqueries.command.impl.getCommand;

import by.latushko.anyqueries.command.Command;
import by.latushko.anyqueries.command.PagePath;
import by.latushko.anyqueries.command.ResponseParameter;
import jakarta.servlet.http.HttpServletRequest;

public class MainPageCommand implements Command {
    @Override
    public ResponseParameter execute(HttpServletRequest request) {
        return new ResponseParameter(PagePath.MAIN_PAGE, ResponseParameter.RoutingType.FORWARD);
    }
}