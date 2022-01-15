package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.Category;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.CategoryService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.CategoryServiceImpl;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.PagePath.*;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.*;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.CATEGORY;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.ID;

public class EditUserPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.valueOf(request.getParameter(ID));
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
