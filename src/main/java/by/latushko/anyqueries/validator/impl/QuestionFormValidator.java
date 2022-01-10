package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.util.AppProperty.APP_TEXTAREA_MAXLENGTH;
import static by.latushko.anyqueries.util.i18n.MessageKey.LABEL_WRONG_INPUT;

public class QuestionFormValidator implements FormValidator {
    private static final int TITLE_MAX_LENGTH = 200;
    private static FormValidator instance;

    private QuestionFormValidator() {
    }

    public static FormValidator getInstance() {
        if(instance == null) {
            instance = new QuestionFormValidator();
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
        if(result.getValue(TEXT).isEmpty() || result.getValue(TEXT).length() > APP_TEXTAREA_MAXLENGTH) {
            result.setError(TEXT, LABEL_WRONG_INPUT);
        }
        return result;
    }
}
