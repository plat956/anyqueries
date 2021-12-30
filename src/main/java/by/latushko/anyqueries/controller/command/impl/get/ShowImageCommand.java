package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;

import static by.latushko.anyqueries.controller.command.CommandResult.RoutingType.IMAGE;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.FILE;
import static by.latushko.anyqueries.util.AppProperty.APP_UPLOAD_DIR;

public class ShowImageCommand implements Command {
    private static final String IMAGE_UPLOAD_DIRECTORY = "images";
    public static final String IMAGE_DIRECTORY_PATH = APP_UPLOAD_DIR + File.separator + IMAGE_UPLOAD_DIRECTORY + File.separator;

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String file = request.getParameter(FILE);
        return new CommandResult(IMAGE_DIRECTORY_PATH + file, IMAGE);
    }
}
