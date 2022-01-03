package by.latushko.anyqueries.validator;

import jakarta.servlet.http.Part;

import java.util.List;

public interface AttachmentValidator {
    boolean validate(List<Part> attachments);
}
