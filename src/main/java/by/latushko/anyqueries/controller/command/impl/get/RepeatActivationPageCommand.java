package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RepeatActivationPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        return new CommandResult(PagePath.REPEAT_ACTIVATION_PAGE, CommandResult.RoutingType.FORWARD);
    }
}
