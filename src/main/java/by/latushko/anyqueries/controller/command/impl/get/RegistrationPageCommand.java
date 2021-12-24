package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.util.telegram.command.BotCommandType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.util.telegram.TelegramBot.BOT_NAME;

public class RegistrationPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("telegramBot", BOT_NAME);
        request.setAttribute("activationCommand", BotCommandType.ACTIVATE_ACCOUNT);
        return new CommandResult(PagePath.REGISTRATION_PAGE, CommandResult.RoutingType.FORWARD);
    }
}
