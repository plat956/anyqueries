package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.LANG;
import static by.latushko.anyqueries.util.i18n.MessageKey.LABEL_WRONG_INPUT;

public class ChangeLocaleFormValidator implements FormValidator{
    private static FormValidator instance;

    private ChangeLocaleFormValidator() {
    }

    public static FormValidator getInstance() {
        if(instance == null) {
            instance = new ChangeLocaleFormValidator();
        }
        return instance;
    }

    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult(formData);
        try {
            MessageManager.valueOf(result.getValue(LANG).toUpperCase());
        } catch (IllegalArgumentException ex) {
            result.setError(LANG, LABEL_WRONG_INPUT);
        }
        return result;
    }
}
