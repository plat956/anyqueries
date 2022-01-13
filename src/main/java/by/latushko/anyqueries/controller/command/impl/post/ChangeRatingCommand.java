package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.AnswerService;
import by.latushko.anyqueries.service.impl.AnswerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.DATA;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.GRADE;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.ID;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;
import static by.latushko.anyqueries.util.http.MimeType.APPLICATION_JSON;

public class ChangeRatingCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON);

        HttpSession session = request.getSession();
        Long id = Long.valueOf(request.getParameter(ID));
        Boolean grade = Boolean.valueOf(request.getParameter(GRADE));
        User currentUser = (User) session.getAttribute(PRINCIPAL);
        AnswerService answerService = AnswerServiceImpl.getInstance();
        boolean result = answerService.changeRating(id, grade, currentUser.getId());
        JSONObject gradeResponse = new JSONObject();
        if(result) {
            Integer rating = answerService.calculateRatingByAnswerId(id);
            gradeResponse.put(GRADE, rating);
        }
        return new CommandResult(gradeResponse.toString(), DATA);
    }
}
