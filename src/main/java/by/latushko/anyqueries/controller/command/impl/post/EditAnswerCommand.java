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
import by.latushko.anyqueries.util.http.QueryParameterHelper;
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

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.REDIRECT;
import static by.latushko.anyqueries.controller.command.ResponseMessage.Level.DANGER;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.HeaderName.REFERER;
import static by.latushko.anyqueries.controller.command.identity.PagePath.ERROR_403_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.*;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_ATTACHMENT_WRONG;
import static by.latushko.anyqueries.util.i18n.MessageKey.MESSAGE_ERROR_UNEXPECTED;

public class EditAnswerCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Long id = getLongParameter(request, ID);
        User user = (User) session.getAttribute(PRINCIPAL);
        AnswerService answerService = AnswerServiceImpl.getInstance();
        if(!answerService.checkEditAccess(id, user.getId())) {
            return new CommandResult(ERROR_403_PAGE, FORWARD);
        }
        String referer = request.getHeader(REFERER);
        CommandResult commandResult = new CommandResult(referer, REDIRECT);
        ResponseMessage message;
        FormValidator validator = AnswerFormValidator.getInstance();
        ValidationResult validationResult = validator.validate(request.getParameterMap());
        if(!validationResult.getStatus()) {
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }

        String text = request.getParameter(TEXT);
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
        Optional<Answer> result = answerService.update(id, text, fileParts);
        if(result.isPresent()) {
            session.setAttribute(ANSWER_OBJECT, result.get());
            referer = QueryParameterHelper.removeParameter(referer, EDIT);
            return new CommandResult(referer, REDIRECT);
        } else {
            message = new ResponseMessage(DANGER, manager.getMessage(MESSAGE_ERROR_UNEXPECTED));
            session.setAttribute(MESSAGE, message);
            session.setAttribute(VALIDATION_RESULT, validationResult);
            return commandResult;
        }
    }
}
