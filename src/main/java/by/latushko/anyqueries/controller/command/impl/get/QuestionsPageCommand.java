package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.Question;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.CategoryService;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.impl.CategoryServiceImpl;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import by.latushko.anyqueries.util.pagination.Paginated;
import by.latushko.anyqueries.util.pagination.RequestPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.FORWARD;
import static by.latushko.anyqueries.controller.command.identity.PagePath.QUESTIONS_PAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.*;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;
import static by.latushko.anyqueries.controller.command.impl.get.LiveSearchCommand.limitSearchQueryString;

public class QuestionsPageCommand implements Command {
    private static final String DISCUSSED_SORT_VALUE = "discussed";
    private static final String MODE_MY_VALUE = "my";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(PRINCIPAL);
        String pageParameter = request.getParameter(PAGE);
        boolean resolved = Boolean.valueOf(request.getParameter(RESOLVED));
        boolean newestFirst = !DISCUSSED_SORT_VALUE.equalsIgnoreCase(request.getParameter(SORT));
        Long authorId = MODE_MY_VALUE.equalsIgnoreCase(request.getParameter(MODE)) ? user.getId() : null;
        Long category = getLongParameter(request, CATEGORY);
        String title = limitSearchQueryString(request.getParameter(QUERY));
        RequestPage page = new RequestPage(pageParameter);
        QuestionService questionService = QuestionServiceImpl.getInstance();
        Paginated<Question> questions = questionService.findPaginatedByQueryParametersOrderByNewest(page, resolved, authorId, category, title, newestFirst);
        request.setAttribute(TOTAL_PAGES, questions.getTotalPages());
        request.setAttribute(QUESTIONS, questions.getContent());
        request.setAttribute(CATEGORY, category);
        if (category != null) {
            CategoryService categoryService = CategoryServiceImpl.getInstance();
            Optional<String> name = categoryService.findNameById(category);
            if(name.isPresent()) {
                request.setAttribute(CATEGORY_NAME, name.get());
            }
        }
        return new CommandResult(QUESTIONS_PAGE, FORWARD);
    }
}
