package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.AttachmentService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.AttachmentServiceImpl;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.AttachmentValidator;
import by.latushko.anyqueries.validator.impl.UploadAvatarValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.EDIT_PROFILE_URL;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class UploadAvatarCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        HttpSession session = request.getSession();
        CommandResult commandResult = new CommandResult(EDIT_PROFILE_URL, REDIRECT);
        ResponseMessage message;
        List<Part> parts;
        try {
            parts = request.getParts().stream().toList();
        } catch (IOException | ServletException e) {
            logger.error("Failed to receive attachment files", e);
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_FILE_UPLOAD_ERROR));
            session.setAttribute(MESSAGE, message);
            return commandResult;
        }
        AttachmentValidator validator = UploadAvatarValidator.getInstance();
        boolean validationResult = validator.validate(parts);
        if(!validationResult) {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_AVATAR_WRONG));
            session.setAttribute(MESSAGE, message);
            return commandResult;
        }
        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
        Optional<String> avatar = attachmentService.uploadAvatar(parts);
        if(avatar.isEmpty()) {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_FILE_UPLOAD_ERROR));
            session.setAttribute(MESSAGE, message);
            return commandResult;
        }
        User user = (User) session.getAttribute(PRINCIPAL);
        UserService userService = UserServiceImpl.getInstance();
        boolean result = userService.updateAvatar(user, avatar.get());
        if(result) {
            message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_AVATAR_UPLOADED));
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_FILE_UPLOAD_ERROR));
        }
        session.setAttribute(MESSAGE, message);
        return commandResult;
    }
}
