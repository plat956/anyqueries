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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static by.latushko.anyqueries.controller.command.identity.HeaderName.*;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.CONTROLLER_URL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.COMMAND;

@WebServlet(name = "commonController", value = "/controller")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 15,
        maxRequestSize = 1024 * 1024 * 15 * 10)
public class CommonController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    private static final String CONTENT_DISPOSITION_VALUE = "inline";
    private static final String CACHE_CONTROL_VALUE = "no-cache, no-store, must-revalidate";
    private static final String PRAGMA_VALUE = "no-cache";
    private static final String EXPIRES_VALUE = "0";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response);
    }

    private void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader(CACHE_CONTROL, CACHE_CONTROL_VALUE);
        response.setHeader(PRAGMA, PRAGMA_VALUE);
        response.setHeader(EXPIRES, EXPIRES_VALUE);

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
            case REDIRECT -> response.sendRedirect(addContextPath(request, result.page()));
            case DATA -> renderData(response, result);
            case FILE -> sendFile(response, result);
            default -> response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void renderData(HttpServletResponse response, CommandResult result) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.print(result.page());
    }

    private void sendFile(HttpServletResponse response, CommandResult result) throws IOException {
        Path path = Paths.get(result.page());
        if(!Files.exists(path) || !Files.isRegularFile(path)) {
            logger.error("Attempt to take the unknown file {}", result.page());
            response.setContentType(null);
            response.setHeader(CONTENT_DISPOSITION, CONTENT_DISPOSITION_VALUE);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(result.page()));
             BufferedOutputStream bout = new BufferedOutputStream(response.getOutputStream())) {
            int ch;
            while ((ch = bin.read()) != -1) {
                bout.write(ch);
            }
        } finally {
            logger.info("File {} has been given to user successfully", result.page());
        }
    }

    private String addContextPath(HttpServletRequest request, String url) {
        String context = request.getContextPath();
        return context + url.substring(url.indexOf(CONTROLLER_URL));
    }
}