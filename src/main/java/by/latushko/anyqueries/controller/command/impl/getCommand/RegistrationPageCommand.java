package by.latushko.anyqueries.controller.command.impl.getCommand;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.PagePath;
import by.latushko.anyqueries.controller.command.PreparedResponse;
import by.latushko.anyqueries.util.telegram.command.BotCommandType;
import jakarta.servlet.http.HttpServletRequest;

import static by.latushko.anyqueries.util.telegram.TelegramBot.BOT_NAME;

public class RegistrationPageCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request) {
        request.setAttribute("telegramBot", BOT_NAME);
        request.setAttribute("activationCommand", BotCommandType.ACTIVATE_ACCOUNT);
        return new PreparedResponse(PagePath.REGISTRATION_PAGE, PreparedResponse.RoutingType.FORWARD);
    }
}
