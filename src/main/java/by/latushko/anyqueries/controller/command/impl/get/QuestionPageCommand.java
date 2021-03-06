package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.Answer;
import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.AnswerService;
import by.latushko.anyqueries.service.AttachmentService;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.AnswerServiceImpl;
import by.latushko.anyqueries.service.impl.AttachmentServiceImpl;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.PagePath.*;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.QUESTION;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.*;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;

public class QuestionPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        Long id = getLongParameter(request, ID);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(PRINCIPAL);
        Long userId = user != null ? user.getId() : null;
        Long edit = getLongParameter(request, EDIT);
        AnswerService answerService = AnswerServiceImpl.getInstance();
        if(edit != null && !answerService.checkEditAccess(edit, userId)) {
            return new CommandResult(ERROR_403_PAGE, FORWARD);
        }
        QuestionService questionService = QuestionServiceImpl.getInstance();
        Optional<Question> question = questionService.findById(id);
        if(question.isEmpty()) {
            return new CommandResult(ERROR_404_PAGE, FORWARD);
        }
        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
        request.setAttribute(ATTACHMENTS, attachmentService.findByQuestionId(id));
        request.setAttribute(QUESTION, question.get());
        String pageParameter = request.getParameter(PAGE);
        RequestPage page = new RequestPage(pageParameter);
        Paginated<Answer> answers = answerService.findPaginatedByQuestionIdOrderByCreationDateAsc(page, id, userId);
        request.setAttribute(TOTAL_PAGES, answers.getTotalPages());
        request.setAttribute(ANSWERS, answers.getContent());
        return new CommandResult(QUESTION_PAGE, FORWARD);
    }
}
