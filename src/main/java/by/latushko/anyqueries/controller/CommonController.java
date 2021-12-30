package by.latushko.anyqueries.controller;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandProvider;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.util.http.RequestMethod;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Optional;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.COMMAND;
import static by.latushko.anyqueries.util.http.MimeType.APPLICATION_JSON;
import static by.latushko.anyqueries.util.http.MimeType.IMAGE_JPEG;

@WebServlet(name = "commonController", value = "/controller")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class CommonController extends HttpServlet {
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

        String commandName = request.getParameter(COMMAND);
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        Optional<Command> command = commandProvider.getCommand(commandName, method);

        if(command.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        CommandResult result = command.get().execute(request, response);

        switch (result.routingType()) {
            case FORWARD -> request.getRequestDispatcher(result.page()).forward(request, response);
            case REDIRECT -> response.sendRedirect(result.page());
            case JSON -> {
                response.setContentType(APPLICATION_JSON);
                PrintWriter writer = response.getWriter();
                writer.print(result.page());
            }
            case IMAGE -> {
                response.setContentType(IMAGE_JPEG);
                try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(result.page()));
                     BufferedOutputStream bout = new BufferedOutputStream(response.getOutputStream())) {
                    int ch;
                    while ((ch = bin.read()) != -1) {
                        bout.write(ch);
                    }
                }
            }
            default -> response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}