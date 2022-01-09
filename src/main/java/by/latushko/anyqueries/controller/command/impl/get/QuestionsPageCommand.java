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
import static by.latushko.anyqueries.service.QuestionService.QUESTION_SEARCH_QUERY_MAX_LENGTH;

public class QuestionsPageCommand implements Command {
    private static final String NEWEST_SORT_VALUE = "new";
    private static final String MODE_MY_VALUE = "my";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(PRINCIPAL);

        String pageParameter = request.getParameter(PAGE);
        String resolvedParameter = request.getParameter(RESOLVED);
        String sortParameter = request.getParameter(SORT);
        String modeParameter = request.getParameter(MODE);
        String categoryParameter = request.getParameter(CATEGORY);

        boolean resolved = resolvedParameter != null ? Boolean.valueOf(resolvedParameter) : false;
        boolean newestFirst = sortParameter == null || sortParameter.equalsIgnoreCase(NEWEST_SORT_VALUE);
        Long authorId = modeParameter != null && modeParameter.equalsIgnoreCase(MODE_MY_VALUE) ? user.getId() : null;
        Long category = categoryParameter != null ? Long.valueOf(categoryParameter) : null;
        String title = request.getParameter(TITLE);
        if(title != null && title.length() > QUESTION_SEARCH_QUERY_MAX_LENGTH) {
            title = title.substring(0, QUESTION_SEARCH_QUERY_MAX_LENGTH);
        }
        RequestPage page = new RequestPage(pageParameter);
        QuestionService questionService = QuestionServiceImpl.getInstance();
        Paginated<Question> questions = questionService.findByQueryParametersOrderByNewest(page, resolved, newestFirst, authorId, category, title);
        request.setAttribute(TOTAL_PAGES, questions.getTotalPages());
        request.setAttribute(QUESTIONS, questions.getContent());
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
