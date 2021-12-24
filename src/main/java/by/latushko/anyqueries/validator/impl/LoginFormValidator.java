package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

public class LoginFormValidator implements FormValidator {
    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult();
        //if(!formData.containsKey(RequestParameter.LOGIN)) {
            String[] fieldValue = formData.get(RequestParameter.LOGIN);
            String value = null;
            if(fieldValue != null && fieldValue.length > 0) {
                value = fieldValue[0];
            }

            result.add(RequestParameter.LOGIN, value, false, "Заполните поле корректно");
        //}
        return result;
    }
}
