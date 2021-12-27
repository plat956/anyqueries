package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.LOGIN;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.PASSWORD;
import static by.latushko.anyqueries.util.i18n.MessageKey.LABEL_WRONG_INPUT;
import static by.latushko.anyqueries.validator.ValidationPattern.LOGIN_REGEXP;
import static by.latushko.anyqueries.validator.ValidationPattern.PASSWORD_REGEXP;

public class LoginFormValidator implements FormValidator {
    private static FormValidator instance;

    private LoginFormValidator() {
    }

    public static FormValidator getInstance() {
        if(instance == null) {
            instance = new LoginFormValidator();
        }
        return instance;
    }

    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult(formData);
        if(!result.getValue(LOGIN).matches(LOGIN_REGEXP)) {
            result.setError(LOGIN, LABEL_WRONG_INPUT);
        }
        if(!result.getValue(PASSWORD).matches(PASSWORD_REGEXP)) {
            result.setError(PASSWORD, LABEL_WRONG_INPUT);
        }
        return result;
    }
}
