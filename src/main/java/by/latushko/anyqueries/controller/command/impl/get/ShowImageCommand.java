package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.FILE;
import static by.latushko.anyqueries.service.AttachmentService.IMAGE_DIRECTORY_PATH;
import static by.latushko.anyqueries.util.http.MimeType.IMAGE_JPEG;

public class ShowImageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String file = request.getParameter(FILE);
        response.setContentType(IMAGE_JPEG);
        return new CommandResult(IMAGE_DIRECTORY_PATH + file, CommandResult.RoutingType.FILE);
    }
}
