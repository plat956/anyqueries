package by.latushko.anyqueries.controller;

import by.latushko.anyqueries.controller.command.*;
import by.latushko.anyqueries.util.http.RequestMethod;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static by.latushko.anyqueries.util.http.MimeType.APPLICATION_JSON;

@WebServlet(name = "mainController", value = "/controller")
public class MainController extends HttpServlet {
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

        PreparedResponse preparedResponse = command.get().execute(request, response);

        switch (preparedResponse.getRoutingType()) {
            case FORWARD -> request.getRequestDispatcher(preparedResponse.getPage()).forward(request, response);
            case REDIRECT -> response.sendRedirect(preparedResponse.getPage());
            case RESPOND_WITH_JSON -> {
                response.setContentType(APPLICATION_JSON);
                PrintWriter writer = response.getWriter();
                writer.print(preparedResponse.getPage());
            }
            default -> response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}