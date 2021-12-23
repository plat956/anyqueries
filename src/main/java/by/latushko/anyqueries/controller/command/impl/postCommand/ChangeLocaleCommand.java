package by.latushko.anyqueries.controller.command.impl.postCommand;

import by.latushko.anyqueries.controller.command.*;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ChangeLocaleCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request, HttpServletResponse response) {
        String lang = request.getParameter(RequestParameter.LANG);
        UserService userService = UserServiceImpl.getInstance();
        userService.changeLocale(lang, response);
        String currentPage = String.valueOf(request.getSession().getAttribute(RequestAttribute.CURRENT_PAGE));
        return new PreparedResponse(currentPage, PreparedResponse.RoutingType.REDIRECT); //refreshPage
    }
}
