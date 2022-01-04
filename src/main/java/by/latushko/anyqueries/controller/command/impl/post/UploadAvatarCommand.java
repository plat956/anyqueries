package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.file.FileHelper;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.AttachmentValidator;
import by.latushko.anyqueries.validator.impl.UploadAvatarValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.*;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PagePath.EDIT_PROFILE_URL;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.MESSAGE;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;
import static by.latushko.anyqueries.service.AttachmentService.IMAGE_DIRECTORY_PATH;
import static by.latushko.anyqueries.service.UserService.AVATAR_PREFIX;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class UploadAvatarCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        File uploadDir = new File(IMAGE_DIRECTORY_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String userLang = CookieHelper.readCookie(request, LANG).orElse(null);
        MessageManager manager = MessageManager.getManager(userLang);
        HttpSession session = request.getSession();
        ResponseMessage message;
        try {
            List<Part> parts = request.getParts().stream().toList();
            if(parts != null && !parts.isEmpty()) {
                AttachmentValidator validator = UploadAvatarValidator.getInstance();
                boolean validationResult = validator.validate(parts);
                if(validationResult) {
                    Part part = parts.get(0);
                    String fileName = part.getSubmittedFileName();
                    String ext = FileHelper.getExtension(fileName).get();
                    fileName = AVATAR_PREFIX + Calendar.getInstance().getTimeInMillis() + ext;
                    part.write(IMAGE_DIRECTORY_PATH + fileName);

                    UserService userService = UserServiceImpl.getInstance();
                    userService.resizeAvatar(fileName);

                    User user = (User) session.getAttribute(PRINCIPAL);
                    boolean result = userService.updateAvatar(user, fileName);
                    if(result) {
                        message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_AVATAR_UPLOADED));
                    } else {
                        message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_FILE_UPLOAD_ERROR));
                    }
                } else {
                    message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_AVATAR_WRONG));
                }
            } else {
                message = new ResponseMessage(INFO, manager.getMessage(MESSAGE_NO_FILE_CHOSEN));
            }
        } catch (IOException | ServletException e) {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_FILE_UPLOAD_ERROR));
        }

        session.setAttribute(MESSAGE, message);
        return new CommandResult(EDIT_PROFILE_URL, REDIRECT);
    }
}
