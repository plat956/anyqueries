package by.latushko.anyqueries.command.impl.postCommand;

import by.latushko.anyqueries.command.Command;
import by.latushko.anyqueries.command.RequestParameter;
import by.latushko.anyqueries.command.ResponseParameter;
import jakarta.servlet.http.HttpServletRequest;

public class LoginCommand implements Command {
    @Override
    public ResponseParameter execute(HttpServletRequest request) {
        String login = request.getParameter(RequestParameter.LOGIN);
        String password = request.getParameter(RequestParameter.PASSWORD);
        return null;
    }
}
