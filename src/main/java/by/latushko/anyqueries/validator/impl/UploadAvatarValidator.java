package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.service.AttachmentService;
import by.latushko.anyqueries.service.impl.AttachmentServiceImpl;
import by.latushko.anyqueries.validator.AttachmentValidator;
import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Optional;

import static by.latushko.anyqueries.util.AppProperty.APP_ATTACHMENT_SIZE;
import static by.latushko.anyqueries.util.AppProperty.APP_UPLOAD_AVATAR_EXTENSIONS;

public class UploadAvatarValidator implements AttachmentValidator {
    private static AttachmentValidator instance;

    private UploadAvatarValidator() {
    }

    public static AttachmentValidator getInstance() {
        if(instance == null) {
            instance = new UploadAvatarValidator();
        }
        return instance;
    }

    public boolean validate(List<Part> attachments) {
        if(attachments == null || attachments.isEmpty()) {
            return false;
        }
        Part file = attachments.get(0);
        String fileName = file.getSubmittedFileName();
        AttachmentService attachmentService = AttachmentServiceImpl.getInstance();
        Optional<String> extension = attachmentService.getFileExtension(fileName);
        if(extension.isEmpty() || !APP_UPLOAD_AVATAR_EXTENSIONS.contains(extension.get())) {
            return false;
        }
        long fileSize = file.getSize() / (1024 * 1024);
        return APP_ATTACHMENT_SIZE >= fileSize;
    }
}
