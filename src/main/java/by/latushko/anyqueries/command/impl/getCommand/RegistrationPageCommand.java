package by.latushko.anyqueries.command.impl.getCommand;

import by.latushko.anyqueries.command.Command;
import by.latushko.anyqueries.command.PagePath;
import by.latushko.anyqueries.command.ResponseParameter;
import by.latushko.anyqueries.util.telegram.command.BotCommandType;
import jakarta.servlet.http.HttpServletRequest;

import static by.latushko.anyqueries.util.telegram.TelegramBot.BOT_NAME;

public class RegistrationPageCommand implements Command {
    @Override
    public ResponseParameter execute(HttpServletRequest request) {
        request.setAttribute("telegramBot", BOT_NAME);
        request.setAttribute("activationCommand", BotCommandType.ACTIVATE_ACCOUNT);
        return new ResponseParameter(PagePath.REGISTRATION_PAGE, ResponseParameter.RoutingType.FORWARD);
    }
}
