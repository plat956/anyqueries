package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.TEXT;
import static by.latushko.anyqueries.util.AppProperty.APP_ANSWER_MAXLENGTH;
import static by.latushko.anyqueries.util.i18n.MessageKey.LABEL_WRONG_INPUT;

public class AnswerFormValidator implements FormValidator {
    private static FormValidator instance;

    private AnswerFormValidator() {
    }

    public static FormValidator getInstance() {
        if(instance == null) {
            instance = new AnswerFormValidator();
        }
        return instance;
    }

    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult(formData);
        String text = stripHtml(result.getValue(TEXT));
        if(text.isEmpty() || text.length() > APP_ANSWER_MAXLENGTH) {
            result.setError(TEXT, LABEL_WRONG_INPUT);
        }
        return result;
    }
}
