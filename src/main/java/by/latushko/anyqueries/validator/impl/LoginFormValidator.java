package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.util.http.RequestParameterHelper;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;
import java.util.Optional;

public class LoginFormValidator implements FormValidator {
    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult();
        RequestParameterHelper helper = new RequestParameterHelper(formData);

        Optional<String> login = helper.getValue(RequestParameter.LOGIN);
        Optional<String> password = helper.getValue(RequestParameter.PASSWORD);

        if(login.isEmpty()) {
            result.add(RequestParameter.LOGIN, null, "Заполните поле корректно");
        } else {
            if(false) {//todo login additional checks
                result.add(RequestParameter.LOGIN, login.get(), "Логин должен содержать до 10 символов");
            } else {
                result.add(RequestParameter.LOGIN, login.get());
            }
        }
        //todo password field
        return result;
    }
}
