package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.util.i18n.MessageKey.LABEL_WRONG_INPUT;

public class CreateQuestionFormValidator implements FormValidator {
    private static final int TITLE_MAX_LENGTH = 200;
    private static final int TEXT_MAX_LENGTH = 1500;
    private static FormValidator instance;

    private CreateQuestionFormValidator() {
    }

    public static FormValidator getInstance() {
        if(instance == null) {
            instance = new CreateQuestionFormValidator();
        }
        return instance;
    }

    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult(formData);
        if(result.getValue(CATEGORY).isEmpty()) {
            result.setError(CATEGORY, LABEL_WRONG_INPUT);
        }
        if(result.getValue(TITLE).isEmpty() || result.getValue(TITLE).length() > TITLE_MAX_LENGTH) {
            result.setError(TITLE, LABEL_WRONG_INPUT);
        }
        if(result.getValue(TEXT).isEmpty() || result.getValue(TEXT).length() > TEXT_MAX_LENGTH) {
            result.setError(TEXT, LABEL_WRONG_INPUT);
        }
        return result;
    }
}
