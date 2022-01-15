package by.latushko.anyqueries.controller.command.impl.get;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.FileNameMap;
import java.net.URLConnection;

import static by.latushko.anyqueries.controller.command.identity.HeaderName.CONTENT_DISPOSITION;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.FILE;
import static by.latushko.anyqueries.service.AttachmentService.FILE_DIRECTORY_PATH;

public class DownloadCommand implements Command {
    private static final String CONTENT_DISPOSITION_ATTACHMENT_VALUE = "attachment; filename=";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String file = request.getParameter(FILE);
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(file);
        response.setContentType(mimeType);
        response.setHeader(CONTENT_DISPOSITION, CONTENT_DISPOSITION_ATTACHMENT_VALUE + file);
        return new CommandResult(FILE_DIRECTORY_PATH + file, CommandResult.RoutingType.FILE);
    }
}
