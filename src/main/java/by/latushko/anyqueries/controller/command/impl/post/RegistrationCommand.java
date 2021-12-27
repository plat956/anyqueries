package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.CookieName;
import by.latushko.anyqueries.controller.command.identity.PagePath;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.service.RegistrationService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.RegistrationServiceImpl;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageKey;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.RegistrationFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.util.AppProperty.APP_ACTIVATION_LINK_ALIVE_HOURS;
import static by.latushko.anyqueries.util.telegram.TelegramBot.BOT_NAME;

public class RegistrationCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        ResponseMessage message;
        FormValidator validator = new RegistrationFormValidator();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(validationResult.getStatus()) {
            String login = request.getParameter(RequestParameter.LOGIN);
            String email = request.getParameter(RequestParameter.EMAIL);
            String telegram = request.getParameter(RequestParameter.TELEGRAM);
            UserService userService = UserServiceImpl.getInstance();
            //if(!userService.chechIfExistsByLogin(login)) {
                //if((email == null || !email.isEmpty()) && userService.chechIfExistsByEmail(email)) {
                    //if((telegram == null || !telegram.isEmpty()) && userService.chechIfExistsByTelegram(telegram)) {
                        String firstName = request.getParameter(RequestParameter.FIRST_NAME);
                        String lastName = request.getParameter(RequestParameter.LAST_NAME);
                        String middleName = request.getParameter(RequestParameter.MIDDLE_NAME);
                        String password = request.getParameter(RequestParameter.PASSWORD);
                        boolean sendLink = request.getParameter(RequestParameter.SEND_LINK) != null;

                        RegistrationService registrationService = RegistrationServiceImpl.getInstance();
                        boolean result = registrationService.registerUser(firstName, lastName, middleName, sendLink, email, telegram, login, password);

                        String userLang = CookieHelper.readCookie(request, CookieName.LANG).orElse(null);
                        MessageManager manager = MessageManager.getManager(userLang);

                        if (result) {
                            String text, notice;
                            if (sendLink) {
                                text = manager.getMessage(MessageKey.MESSAGE_ACTIVATION_EMAIL_TITLE, email);
                                notice = manager.getMessage(MessageKey.MESSAGE_ACTIVATION_EMAIL_NOTICE, APP_ACTIVATION_LINK_ALIVE_HOURS, "https://t.me/" + BOT_NAME, BOT_NAME);
                                notice += manager.getMessage(MessageKey.MESSAGE_ACTIVATION_GLOBAL_NOTICE);
                            } else {
                                text = manager.getMessage(MessageKey.MESSAGE_ACTIVATION_TELEGRAM, "https://t.me/" + BOT_NAME, BOT_NAME);
                                notice = manager.getMessage(MessageKey.MESSAGE_ACTIVATION_GLOBAL_NOTICE);
                            }
                            message = new ResponseMessage(SUCCESS, text, notice);
                        } else {
                            session.setAttribute(SessionAttribute.VALIDATION_RESULT, validationResult);
                            message = new ResponseMessage(DANGER, manager.getMessage(MessageKey.MESSAGE_REGISTRATION_FAIL));
                        }

                        session.setAttribute(SessionAttribute.MESSAGE, message);
                    //} else {
                        //exists telegram
                    //}
                //} else {
                    //exists email
                //}
            //} else {
                //exists login
            //}
        } else {
            session.setAttribute(SessionAttribute.VALIDATION_RESULT, validationResult);
        }

        return new CommandResult(PagePath.REGISTRATION_URL, CommandResult.RoutingType.REDIRECT);
    }
}