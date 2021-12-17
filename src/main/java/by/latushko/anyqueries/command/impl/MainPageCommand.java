package by.latushko.anyqueries.command.impl;

import by.latushko.anyqueries.command.Command;
import by.latushko.anyqueries.command.PagePath;
import by.latushko.anyqueries.command.Router;
import jakarta.servlet.http.HttpServletRequest;

public class MainPageCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) {
        return new Router(PagePath.MAIN_PAGE, Router.RouterType.FORWARDING);
    }
}