package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.AttachmentService;
import by.latushko.anyqueries.service.CategoryService;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.AttachmentServiceImpl;
import by.latushko.anyqueries.service.impl.CategoryServiceImpl;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.PagePath.*;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.*;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.ID;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;

public class EditQuestionPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        Long id = getLongParameter(request, ID);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(PRINCIPAL);
        QuestionService questionService = QuestionServiceImpl.getInstance();
        if(!questionService.checkEditAccess(id, user.getId(), true)) {
            return new CommandResult(ERROR_403_PAGE, FORWARD);
        }
        Optional<Question> question = questionService.findById(id);
        if(question.isEmpty()) {
            return new CommandResult(ERROR_404_PAGE, FORWARD);
        }
        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
        CategoryService categoryService = CategoryServiceImpl.getInstance();
        request.setAttribute(ATTACHMENTS, attachmentService.findByQuestionId(id));
        request.setAttribute(CATEGORIES, categoryService.findAllOrderByNameAsc());
        request.setAttribute(QUESTION, question.get());
        return new CommandResult(EDIT_QUESTION_PAGE, FORWARD);
    }
}
