package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.util.i18n.MessageKey;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

public class LoginFormValidator implements FormValidator {
    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult(formData);

        if(result.getValue(RequestParameter.LOGIN).isEmpty()) { //todo regexp
            result.setError(RequestParameter.LOGIN, MessageKey.LABEL_WRONG_INPUT);
        }
        if(result.getValue(RequestParameter.PASSWORD).isEmpty()) { //todo regexp
            result.setError(RequestParameter.PASSWORD, MessageKey.LABEL_WRONG_INPUT);
        }

        return result;
    }
}
