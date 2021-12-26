package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.util.telegram.TelegramBot.BOT_NAME;

public class RegistrationPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        if(request.getAttribute(MESSAGE) == null) {
            String userLang = CookieHelper.readCookie(request, CookieName.LANG).orElse(null);
            MessageManager manager = MessageManager.getManager(userLang);
            ResponseMessage message = new ResponseMessage(ResponseMessage.Level.WARNING,
                    manager.getMessage("message.registration.warning", "https://t.me/" + BOT_NAME, BOT_NAME)); //todo lnk
            request.setAttribute(MESSAGE, message);
        }
        return new CommandResult(PagePath.REGISTRATION_PAGE, CommandResult.RoutingType.FORWARD);
    }
}
