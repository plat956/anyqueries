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
    private static final String EXPIRES_HEADER_NAME = "Expires";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
        response.setContentType(IMAGE_JPEG);
        response.setDateHeader(EXPIRES_HEADER_NAME, attachmentService.getImageCachingTerm());
        String path = attachmentService.getImagePath(request.getParameter(FILE));
        return new CommandResult(path, CommandResult.RoutingType.FILE);
    }
}
