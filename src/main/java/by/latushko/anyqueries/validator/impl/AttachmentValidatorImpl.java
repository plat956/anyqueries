package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.validator.AttachmentValidator;
import jakarta.servlet.http.Part;

import java.util.List;

import static by.latushko.anyqueries.util.AppProperty.APP_ATTACHMENT_COUNT;
import static by.latushko.anyqueries.util.AppProperty.APP_ATTACHMENT_SIZE;

public class AttachmentValidatorImpl implements AttachmentValidator {
    private static AttachmentValidator instance;

    private AttachmentValidatorImpl() {
    }

    public static AttachmentValidator getInstance() {
        if(instance == null) {
            instance = new AttachmentValidatorImpl();
        }
        return instance;
    }

    @Override
    public boolean validate(List<Part> attachments) {
        if(attachments == null || attachments.size() > APP_ATTACHMENT_COUNT) {
            return false;
        }
        return attachments.stream().noneMatch(p -> (p.getSize() / (1024 * 1024)) > APP_ATTACHMENT_SIZE);
    }
}
