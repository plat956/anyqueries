package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.AnswerService;
import by.latushko.anyqueries.service.impl.AnswerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.DATA;
import static by.latushko.anyqueries.controller.command.identity.RequestAttribute.RESULT;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.CHECK;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.ID;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.PRINCIPAL;
import static by.latushko.anyqueries.util.http.MimeType.APPLICATION_JSON;

public class MarkSolutionCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON);

        HttpSession session = request.getSession();
        Long id = Long.valueOf(request.getParameter(ID));
        Boolean check = Boolean.valueOf(request.getParameter(CHECK));
        User user = (User) session.getAttribute(PRINCIPAL);
        AnswerService answerService = AnswerServiceImpl.getInstance();
        boolean result = answerService.setSolution(id, check, user.getId());
        JSONObject responseResult = new JSONObject();
        responseResult.put(RESULT, result);
        return new CommandResult(responseResult.toString(), DATA);
    }
}
