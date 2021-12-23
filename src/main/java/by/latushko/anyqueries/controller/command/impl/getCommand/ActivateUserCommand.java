package by.latushko.anyqueries.controller.command.impl.getCommand;

import by.latushko.anyqueries.controller.command.*;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Type.POPUP;

public class ActivateUserCommand implements Command {
    @Override
    public PreparedResponse execute(HttpServletRequest request, HttpServletResponse response) {
        String hash = request.getParameter(RequestParameter.HASH);
        RegistrationService registrationService = RegistrationServiceImpl.getInstance();
        boolean result = registrationService.activateUserByHash(hash);

        ResponseMessage message;
        if(result) {
            message = new ResponseMessage(POPUP, SUCCESS, "Регистрация завершена", "Поздравляем! Теперь вы можете воспользоваться полным функционалом системы.");
        } else {
            message = new ResponseMessage(DANGER, "Неверная ссылка активации либо истек срок ее действия, повторите процедуру регистрации с тем же логином.");
        }
        //todo: log-in automatically if success & redirect to the main page must be here
        request.getSession().setAttribute(SessionAttribute.MESSAGE, message);
        return new PreparedResponse(PagePath.MAIN_URL, PreparedResponse.RoutingType.REDIRECT);
    }
}