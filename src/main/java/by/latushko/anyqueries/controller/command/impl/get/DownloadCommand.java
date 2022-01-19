package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.service.AttachmentService;
import by.latushko.anyqueries.service.impl.AttachmentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.latushko.anyqueries.controller.command.identity.HeaderName.CONTENT_DISPOSITION;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.FILE;

public class DownloadCommand implements Command {
    private static final String CONTENT_DISPOSITION_ATTACHMENT_VALUE_TEMPLATE = "attachment; filename=\"%s\"";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String file = request.getParameter(FILE);
        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
        String mimeType = attachmentService.detectMimeType(file);
        response.setContentType(mimeType);
        String encodedFileName = attachmentService.encodeFileName(file);
        response.setHeader(CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_ATTACHMENT_VALUE_TEMPLATE, encodedFileName));
        String path = attachmentService.getFilePath(file);
        return new CommandResult(path, CommandResult.RoutingType.FILE);
    }
}
