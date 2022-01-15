package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.PagePath.EDIT_USER_PAGE;
import static by.latushko.anyqueries.controller.command.identity.PagePath.ERROR_404_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.*;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.ID;

public class EditUserPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        Long id = getLongParameter(request, ID);
        UserService userService = UserServiceImpl.getInstance();
        Optional<User> user = userService.findById(id);
        if(user.isEmpty()) {
            return new CommandResult(ERROR_404_PAGE, FORWARD);
        }
        request.setAttribute(USER, user.get());
        request.setAttribute(ROLES, User.Role.values());
        request.setAttribute(STATUSES, User.Status.values());
        return new CommandResult(EDIT_USER_PAGE, FORWARD);
    }
}
