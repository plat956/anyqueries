package by.latushko.anyqueries.command.impl;

import by.latushko.anyqueries.command.Command;
import by.latushko.anyqueries.command.RequestParameter;
import by.latushko.anyqueries.command.Router;
import jakarta.servlet.http.HttpServletRequest;

public class LoginActionCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        String login = request.getParameter(RequestParameter.LOGIN);
        String password = request.getParameter(RequestParameter.PASSWORD);
        return null;
    }
}
