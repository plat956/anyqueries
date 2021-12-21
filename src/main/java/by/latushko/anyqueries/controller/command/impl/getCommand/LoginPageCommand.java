package by.latushko.anyqueries.controller.command.impl.getCommand;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.PagePath;
import by.latushko.anyqueries.controller.command.PreparedResponse;
import jakarta.servlet.http.HttpServletRequest;

public class LoginPageCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request) {
        return new PreparedResponse(PagePath.LOGIN_PAGE, PreparedResponse.RoutingType.FORWARD);
    }
}
