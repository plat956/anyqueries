package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.AttachmentValidator;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.AttachmentValidatorImpl;
import by.latushko.anyqueries.validator.impl.CreateQuestionFormValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.SUCCESS;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.CREATE_QUESTION_URL;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.QUESTION_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;
import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public class CreateQuestionCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        CommandResult commandResult = new CommandResult(CREATE_QUESTION_URL, REDIRECT);
        ResponseMessage message;

        FormValidator validator = CreateQuestionFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }

        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
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

        User user = (User) session.getAttribute(PRINCIPAL);
        Long category = Long.valueOf(request.getParameter(CATEGORY));
        String title = request.getParameter(TITLE);
        String text = request.getParameter(TEXT);
        QuestionService questionService = QuestionServiceImpl.getInstance();
        Optional<Question> result = questionService.create(category, title, text, user, fileParts);
        if(result.isPresent()) {
            message = new ResponseMessage(SUCCESS, manager.getMessage(MESSAGE_QUESTION_CREATED));
            session.setAttribute(MESSAGE, message);
            return new CommandResult(QUESTION_URL + result.get().getId(), REDIRECT);
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
            session.setAttribute(MESSAGE, message);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
    }
}
