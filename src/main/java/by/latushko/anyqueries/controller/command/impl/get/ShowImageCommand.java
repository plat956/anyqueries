package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.service.AttachmentService;
import by.latushko.anyqueries.service.impl.AttachmentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.FILE;
import static by.latushko.anyqueries.util.http.MimeType.IMAGE_JPEG;

public class ShowImageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(IMAGE_JPEG);
        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
        String path = attachmentService.getImagePath(request.getParameter(FILE));
        return new CommandResult(path, CommandResult.RoutingType.FILE);
    }
}
