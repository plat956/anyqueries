package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.PagePath.USERS_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.*;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.PAGE;

public class UsersPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String pageParameter = request.getParameter(PAGE);
        RequestPage page = new RequestPage(pageParameter);
        UserService userService = UserServiceImpl.getInstance();
        Paginated<User> users = userService.findAllPaginatedOrderByRoleAsc(page);
        request.setAttribute(TOTAL_PAGES, users.getTotalPages());
        request.setAttribute(USERS, users.getContent());
        return new CommandResult(USERS_PAGE, FORWARD);
    }
}
