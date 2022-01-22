package by.latushko.anyqueries.validator;

import jakarta.servlet.http.Part;

import java.util.List;

/**
 * The Attachment validator interface.
 */
public interface AttachmentValidator {
    /**
     * Validate attached form files.
     *
     * @param attachments the attachments list
     * @return the boolean, true if validation was passed successfully, otherwise false
     */
    boolean validate(List<Part> attachments);
}
