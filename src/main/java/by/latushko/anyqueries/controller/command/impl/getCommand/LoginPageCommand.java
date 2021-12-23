package by.latushko.anyqueries.controller.command.impl.getCommand;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.PreparedResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginPageCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request, HttpServletResponse response) {
        return new PreparedResponse(PagePath.LOGIN_PAGE, PreparedResponse.RoutingType.FORWARD);
    }
}
