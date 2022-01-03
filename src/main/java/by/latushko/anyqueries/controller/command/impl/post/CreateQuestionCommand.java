package by.latushko.anyqueries.controller.command.impl.post;

import by.latushko.anyqueries.controller.command.Command;
import by.latushko.anyqueries.controller.command.CommandResult;
import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.validator.AttachmentValidator;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import by.latushko.anyqueries.validator.impl.AttachmentValidatorImpl;
import by.latushko.anyqueries.validator.impl.CreateQuestionFormValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;

public class CreateQuestionCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            FormValidator validator = CreateQuestionFormValidator.getInstance();
            ValidationResult validationResult = validator.validate(request.getParameterMap());
            if(!validationResult.getStatus()) {
                //todo redirect
            }

            List<Part> fileParts = request.getParts().stream().
                    filter(part -> RequestParameter.FILE.equals(part.getName()) && part.getSize() > 0).toList();
            AttachmentValidator attachmentValidatorImpl = AttachmentValidatorImpl.getInstance();
            boolean attachmentValidationResult = attachmentValidatorImpl.validate(fileParts);
            if(!attachmentValidationResult) {
                //todo redirect
            }

            //passw form data, save files & create record
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }

        return null;
    }
}
