package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.service.AnswerService;
import by.latushko.anyqueries.service.QuestionService;
import by.latushko.anyqueries.service.UserService;
import by.latushko.anyqueries.service.impl.AnswerServiceImpl;
import by.latushko.anyqueries.service.impl.QuestionServiceImpl;
import by.latushko.anyqueries.service.impl.UserServiceImpl;
import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.util.Optional;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.DATA;
import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;
import static by.latushko.anyqueries.controller.command.identity.JsonName.*;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.ID;
import static by.latushko.anyqueries.util.AppProperty.APP_TELEGRAM_LINK_HOST;
import static by.latushko.anyqueries.util.http.MimeType.APPLICATION_JSON;
import static by.latushko.anyqueries.util.i18n.MessageKey.LABEL_ROLE_PREFIX;

public class ProfilePageCommand implements Command {

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON);

        String id = request.getParameter(ID);
        if(id == null || id.isEmpty()) {
            return new CommandResult(new JSONObject().toString(), DATA);
        }
        UserService userService = UserServiceImpl.getInstance();
        Optional<User> userOptional = userService.findById(Long.valueOf(id));
        if(userOptional.isEmpty()) {
            return new CommandResult(new JSONObject().toString(), DATA);
        }
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        JSONObject result = new JSONObject();
        User user = userOptional.get();
        result.put(FIO, user.getFio());
        result.put(AVATAR, user.getAvatar());
        result.put(ROLE_COLOR, user.getRole().getColor());
        result.put(ROLE_NAME, manager.getMessage(LABEL_ROLE_PREFIX + user.getRole().name().toLowerCase()));
        result.put(FIRST_NAME, user.getFirstName());
        result.put(LAST_NAME, user.getLastName());
        result.put(MIDDLE_NAME, user.getMiddleName());
        result.put(EMAIL, user.getEmail());
        result.put(TELEGRAM, user.getTelegram());
        result.put(TELEGRAM_LNK, APP_TELEGRAM_LINK_HOST + user.getTelegram());
        QuestionService questionService = QuestionServiceImpl.getInstance();
        AnswerService answerService = AnswerServiceImpl.getInstance();
        Long totalQuestions = questionService.countByAuthorId(user.getId());
        Long totalAnswers = answerService.countByUserId(user.getId());
        result.put(QUESTIONS_COUNT, totalQuestions);
        result.put(ANSWERS_COUNT, totalAnswers);
        return new CommandResult(result.toString(), DATA);
    }
}
