package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.*;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ChangeLocaleCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String lang = request.getParameter(RequestParameter.LANG);
        UserService userService = UserServiceImpl.getInstance();
        ResponseMessage message;
        boolean result = userService.changeLocale(lang, response);
        if(result) {
            message = new ResponseMessage(ResponseMessage.Level.SUCCESS, "Язык успешно изменен");
        } else {
            message = new ResponseMessage(ResponseMessage.Level.DANGER, "При изменении языка произошла ошибка");
        }
        HttpSession session = request.getSession();
        String currentPage;
        if(session.getAttribute(SessionAttribute.CURRENT_PAGE) != null) {
            currentPage = request.getSession().getAttribute(SessionAttribute.CURRENT_PAGE).toString();
        } else {
            currentPage = PagePath.MAIN_URL;
        }
        session.setAttribute(SessionAttribute.MESSAGE, message);
        return new CommandResult(currentPage, CommandResult.RoutingType.REDIRECT);
    }
}
