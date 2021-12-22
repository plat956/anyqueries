package by.latushko.anyqueries.controller.command.impl.getCommand;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.PagePath;
import by.latushko.anyqueries.controller.command.PreparedResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MainPageCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request, HttpServletResponse response) {
        return new PreparedResponse(PagePath.MAIN_PAGE, PreparedResponse.RoutingType.FORWARD);
    }
}