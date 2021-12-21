package by.latushko.anyqueries.controller.command.impl.postCommand;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.RequestParameter;
import by.latushko.anyqueries.controller.command.PreparedResponse;
import jakarta.servlet.http.HttpServletRequest;

public class LoginCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request) {
        return null;
    }
}
