package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.util.i18n.MessageKey.LABEL_WRONG_INPUT;
import static by.latushko.anyqueries.validator.ValidationPattern.*;

public class CategoryFormValidator implements FormValidator {
    private static FormValidator instance;

    private CategoryFormValidator() {
    }

    public static FormValidator getInstance() {
        if(instance == null) {
            instance = new CategoryFormValidator();
        }
        return instance;
    }

    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult(formData);
        if(!result.getValue(NAME).matches(CATEGORY_NAME_REGEXP)) {
            result.setError(NAME, LABEL_WRONG_INPUT);
        }
        if(result.getValue(COLOR).isEmpty() || !result.getValue(COLOR).matches(HEX_COLOR_REGEXP)) {
            result.setError(COLOR, LABEL_WRONG_INPUT);
        }
        return result;
    }
}
