package by.latushko.anyqueries.controller.command.impl.postCommand;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.PagePath;
import by.latushko.anyqueries.controller.command.PreparedResponse;
import by.latushko.anyqueries.controller.command.RequestParameter;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ChangeLocaleCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request, HttpServletResponse response) {
        String lang = request.getParameter(RequestParameter.LANG);
        UserService userService = new UserServiceImpl();
        userService.changeLocale(lang, response);
        return new PreparedResponse(PagePath.MAIN_PAGE, PreparedResponse.RoutingType.REDIRECT); //refreshPage
    }
}
