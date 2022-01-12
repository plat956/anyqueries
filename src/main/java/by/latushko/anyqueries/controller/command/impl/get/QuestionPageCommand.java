package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.Attachment;
import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.service.AttachmentService;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.AttachmentServiceImpl;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.PagePath.ERROR_404_PAGE;
import static by.latushko.anyqueries.controller.command.identity.PagePath.QUESTION_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.ATTACHMENTS;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.QUESTION;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.ID;

public class QuestionPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.valueOf(request.getParameter(ID));
        QuestionService questionService = QuestionServiceImpl.getInstance();
        Optional<Question> question = questionService.findById(id);
        if(question.isEmpty()) {
            return new CommandResult(ERROR_404_PAGE, FORWARD);
        }
        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
        List<Attachment> attachments = attachmentService.findByQuestionId(id);
        request.setAttribute(ATTACHMENTS, attachments);
        request.setAttribute(QUESTION, question.get());
        return new CommandResult(QUESTION_PAGE, FORWARD);
    }
}
