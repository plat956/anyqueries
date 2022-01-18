package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.ResponseMessage;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.model.entity.Answer;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.AnswerService;
import by.latushko.anyqueries.service.impl.AnswerServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.AttachmentValidator;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.AnswerFormValidator;
import by.latushko.anyqueries.validator.impl.AttachmentValidatorImpl;
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
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.HeaderName.REFERER;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.QUESTION_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.TEXT;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_ATTACHMENT_WRONG;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_ERROR_UNEXPECTED;

public class CreateAnswerCommand implements Command {
    private static final String URL_PAGE_PART = "&page=";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String referer = request.getHeader(REFERER);
        CommandResult commandResult = new CommandResult(referer, REDIRECT);
        session.setAttribute(CREATE_RECORD, true);
        FormValidator validator = AnswerFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
        ResponseMessage message;
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
        String text = request.getParameter(TEXT);
        Long question = getLongParameter(request, RequestParameter.QUESTION);
        AnswerService answerService = AnswerServiceImpl.getInstance();
        Optional<Answer> result = answerService.create(question, text, user, fileParts);
        if(result.isPresent()) {
            Integer page = answerService.calculateLastPageByQuestionId(question);
            String url = QUESTION_URL + question;
            if(page > 1) {
                url += URL_PAGE_PART + page;
            }
            session.setAttribute(ANSWER_OBJECT, result.get());
            return new CommandResult(url, REDIRECT);
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
            session.setAttribute(MESSAGE, message);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
    }
}
