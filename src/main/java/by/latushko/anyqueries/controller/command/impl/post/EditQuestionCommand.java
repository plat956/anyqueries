package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.AttachmentValidator;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.AttachmentValidatorImpl;
import by.latushko.anyqueries.validator.impl.QuestionFormValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.EDIT_QUESTION_URL;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.QUESTIONS_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class EditQuestionCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Long id = Long.valueOf(request.getParameter(ID));
        String closeParameter = request.getParameter(CLOSE);
        String previousPage = QUESTIONS_URL;
        if(session.getAttribute(PREVIOUS_PAGE) != null) {
            previousPage = session.getAttribute(PREVIOUS_PAGE).toString();
        }
        QuestionService questionService = QuestionServiceImpl.getInstance();

        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        ResponseMessage message;

        if(closeParameter != null) {
            String currentPage = QUESTIONS_URL;
            if(session.getAttribute(CURRENT_PAGE) != null) {
                currentPage = session.getAttribute(CURRENT_PAGE).toString();
            }
            boolean close = Boolean.valueOf(closeParameter);
            boolean result = questionService.changeStatus(id, close);
            if(result) {
                message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_SUCCESS));
            } else {
                message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
            }
            session.setAttribute(MESSAGE, message);
            return new CommandResult(currentPage, REDIRECT);
        }

        CommandResult commandResult = new CommandResult(EDIT_QUESTION_URL + id, REDIRECT);
        FormValidator validator = QuestionFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }

        List<Part> fileParts;
        try {
            fileParts = request.getParts().stream().
                    filter(part -> RequestParameter.FILE.equals(part.getName()) && part.getSize() > 0).toList();
        } catch (IOException | ServletException e) {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
            session.setAttribute(MESSAGE, message);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        AttachmentValidator attachmentValidatorImpl = AttachmentValidatorImpl.getInstance();
        boolean attachmentValidationResult = attachmentValidatorImpl.validate(fileParts);
        if(!attachmentValidationResult) {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ATTACHMENT_WRONG));
            session.setAttribute(MESSAGE, message);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        //todo гдето тут удалять аттачменты прі валідаціі
        Long category = Long.valueOf(request.getParameter(CATEGORY));
        String title = request.getParameter(TITLE);
        String text = request.getParameter(TEXT);
        boolean result = questionService.update(id, category, title, text, fileParts);
        if(result) {
            message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_QUESTION_UPDATED));
            session.setAttribute(MESSAGE, message);
            return new CommandResult(previousPage, REDIRECT);
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
            session.setAttribute(MESSAGE, message);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
    }
}
