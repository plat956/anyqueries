package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.controller.command.identity.SessionAttribute;
import by.latushko.anyqueries.model.entity.User;
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

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.HeaderName.REFERER;
import static by.latushko.anyqueries.controller.command.identity.PagePath.ERROR_403_PAGE;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.EDIT_QUESTION_URL;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.QUESTIONS_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class EditQuestionCommand implements Command {
    private QuestionService questionService = QuestionServiceImpl.getInstance();

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Long id = getLongParameter(request, ID);
        User user = (User) session.getAttribute(PRINCIPAL);
        String closeParameter = request.getParameter(CLOSE);
        if(!questionService.checkEditAccess(id, user.getId(), closeParameter == null)) {
            return new CommandResult(ERROR_403_PAGE, FORWARD);
        }
        String previousPage = request.getParameter(RequestParameter.PREVIOUS_PAGE);
        if(previousPage == null || previousPage.isEmpty()) {
            previousPage = QUESTIONS_URL;
        }
        session.setAttribute(SessionAttribute.PREVIOUS_PAGE, previousPage);
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        if(closeParameter != null) {
            return changeClosedStatus(request, session, id, closeParameter, manager);
        }
        CommandResult commandResult = new CommandResult(EDIT_QUESTION_URL + id, REDIRECT);
        FormValidator validator = QuestionFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        ResponseMessage message;
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
        AttachmentValidator attachmentValidator = AttachmentValidatorImpl.getInstance();
        boolean attachmentValidationResult = attachmentValidator.validate(fileParts);
        if(!attachmentValidationResult) {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ATTACHMENT_WRONG));
            session.setAttribute(MESSAGE, message);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        Long category = getLongParameter(request, CATEGORY);
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

    private CommandResult changeClosedStatus(HttpServletRequest request, HttpSession session, Long id,
                                             String closeParameter, MessageManager manager) {
        String referer = request.getHeader(REFERER);
        boolean close = Boolean.valueOf(closeParameter);
        boolean result = questionService.changeStatus(id, close);
        ResponseMessage message;
        if(result) {
            message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_SUCCESS));
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
        }
        session.setAttribute(MESSAGE, message);
        return new CommandResult(referer, REDIRECT);
    }
}
