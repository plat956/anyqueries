package by.latushko.anyqueries.controller;

import java.io.*;
import java.util.Optional;

import by.latushko.anyqueries.command.*;
import by.latushko.anyqueries.command.RequestMethod;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import static by.latushko.anyqueries.util.MimeType.APPLICATION_JSON;

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

        Router router = command.get().execute(request);

        switch (router.getRouterType()) {
            case FORWARDING:
                request.getRequestDispatcher(router.getPage()).forward(request, response);
                break;
            case REDIRECTION:
                response.sendRedirect(router.getPage());
                break;
            case AJAX_RESPONSE_BODY:
                response.setContentType(APPLICATION_JSON);
                PrintWriter writer = response.getWriter();
                writer.print(router.getPage());
                break;
            default:
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                break;
        }
    }
}