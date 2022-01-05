package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.PagePath.QUESTIONS_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.QUESTIONS;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.TOTAL_PAGES;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.PAGE;

public class QuestionsPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String pageParameter = request.getParameter(PAGE);
        Integer page = pageParameter != null && !pageParameter.isEmpty() ? Integer.valueOf(pageParameter) : 1; //todo check if > 0 and < last page
        Integer limit = 4;
        Integer offset = (page - 1) * limit;
        QuestionService questionService = QuestionServiceImpl.getInstance();

        List<Question> questions = questionService.findWithOffsetAndLimit(offset, limit);

        long totalPages = 0L;
        if(!questions.isEmpty()) {
            totalPages = (long) Math.ceil((double) questions.get(0).getTotal() / limit);
        }
        request.setAttribute(TOTAL_PAGES, totalPages);
        request.setAttribute(QUESTIONS, questions);
        return new CommandResult(QUESTIONS_PAGE, FORWARD);
    }
}
