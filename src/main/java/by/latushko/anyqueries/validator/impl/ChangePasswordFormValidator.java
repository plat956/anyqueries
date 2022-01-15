package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.util.i18n.MessageKey.LABEL_WRONG_INPUT;
import static by.latushko.anyqueries.validator.ValidationPattern.PASSWORD_REGEXP;

public class ChangePasswordFormValidator implements FormValidator {
    private static FormValidator instance;

    private ChangePasswordFormValidator() {
    }

    public static FormValidator getInstance() {
        if(instance == null) {
            instance = new ChangePasswordFormValidator();
        }
        return instance;
    }

    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult(formData);
        String passwordConfirm = result.getValue(PASSWORD_NEW_REPEAT);
        if(!result.getValue(PASSWORD_OLD).matches(PASSWORD_REGEXP)) {
            result.setError(PASSWORD_OLD, LABEL_WRONG_INPUT);
        }
        if(!result.getValue(PASSWORD_NEW).matches(PASSWORD_REGEXP)) {
            result.setError(PASSWORD_NEW, LABEL_WRONG_INPUT);
        }
        if(!result.getValue(PASSWORD_NEW).equals(passwordConfirm)) {
            result.setError(PASSWORD_NEW_REPEAT, LABEL_WRONG_INPUT);
        }
        return result;
    }
}
