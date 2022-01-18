package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PagePath.REGISTRATION_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.ACTIVATION_MESSAGE;
import static by.latushko.anyqueries.util.AppProperty.APP_TELEGRAM_LINK_HOST;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_REGISTRATION_WARNING;
import static by.latushko.anyqueries.util.telegram.TelegramBot.BOT_NAME;

public class RegistrationPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        String message = manager.getMessage(MESSAGE_REGISTRATION_WARNING, APP_TELEGRAM_LINK_HOST + BOT_NAME, BOT_NAME);
        request.setAttribute(ACTIVATION_MESSAGE, message);
        return new CommandResult(REGISTRATION_PAGE, FORWARD);
    }
}
