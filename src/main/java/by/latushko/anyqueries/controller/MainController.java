package by.latushko.anyqueries.controller;

import java.io.*;
import java.util.Optional;

import by.latushko.anyqueries.util.telegram.TelegramBot;
import by.latushko.anyqueries.command.*;
import by.latushko.anyqueries.util.http.RequestMethod;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static by.latushko.anyqueries.util.http.MimeType.APPLICATION_JSON;

@WebServlet(name = "mainController", value = "/controller")
public class MainController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void init() throws ServletException {
        try {
            initTelegramBot();
        } catch (TelegramApiException e) {
            logger.error("Unable to start telegram bot", e);
            throw new ExceptionInInitializerError("Unable to start telegram bot");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response);
    }

    private void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommandProvider commandProvider = CommandProvider.getInstance();

        String commandName = request.getParameter(RequestParameter.COMMAND);
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        Optional<Command> command = commandProvider.getCommand(commandName, method);

        if(command.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        ResponseParameter parameter = command.get().execute(request); //todo rename ResponseParameter class

        switch (parameter.getRoutingType()) {
            case FORWARD:
                request.getRequestDispatcher(parameter.getPage()).forward(request, response);
                break;
            case REDIRECT:
                response.sendRedirect(parameter.getPage());
                break;
            case RESPOND_WITH_JSON:
                response.setContentType(APPLICATION_JSON);
                PrintWriter writer = response.getWriter();
                writer.print(parameter.getPage());
                break;
            default:
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                break;
        }
    }

    private void initTelegramBot() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new TelegramBot());
    }
}