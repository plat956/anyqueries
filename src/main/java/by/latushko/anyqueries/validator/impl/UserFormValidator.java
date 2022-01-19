package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.model.entity.User;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.service.impl.UserServiceImpl.FIRST_ADMIN_ACCOUNT_ID;
import static by.latushko.anyqueries.util.i18n.MessageKey.LABEL_WRONG_INPUT;
import static by.latushko.anyqueries.validator.ValidationPattern.*;

public class UserFormValidator implements FormValidator {
    private static FormValidator instance;

    private UserFormValidator() {
    }

    public static FormValidator getInstance() {
        if(instance == null) {
            instance = new UserFormValidator();
        }
        return instance;
    }

    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult(formData);
        if(!result.getValue(FIRST_NAME).matches(FIRST_NAME_REGEXP)) {
            result.setError(FIRST_NAME, LABEL_WRONG_INPUT);
        }
        if(!result.getValue(LAST_NAME).matches(LAST_NAME_REGEXP)) {
            result.setError(LAST_NAME, LABEL_WRONG_INPUT);
        }
        if(!result.getValue(MIDDLE_NAME).matches(MIDDLE_NAME_REGEXP)) {
            result.setError(MIDDLE_NAME, LABEL_WRONG_INPUT);
        }
        String email = result.getValue(EMAIL);
        if(!email.isEmpty() && !email.matches(EMAIL_REGEXP)) {
            result.setError(EMAIL, LABEL_WRONG_INPUT);
        }
        String telegram = result.getValue(TELEGRAM);
        if(!telegram.isEmpty() && !telegram.matches(TELEGRAM_REGEXP)) {
            result.setError(TELEGRAM, LABEL_WRONG_INPUT);
        }
        if(!result.getValue(LOGIN).matches(LOGIN_REGEXP)) {
            result.setError(LOGIN, LABEL_WRONG_INPUT);
        }
        User.Status status = null;
        User.Role role = null;
        try {
            status = User.Status.valueOf(result.getValue(STATUS));
        } catch (IllegalArgumentException ex) {
            result.setError(STATUS, LABEL_WRONG_INPUT);
        }
        try {
            role = User.Role.valueOf(result.getValue(ROLE));
        } catch (IllegalArgumentException ex) {
            result.setError(ROLE, LABEL_WRONG_INPUT);
        }
        if(result.containsField(ID)) {
            if (FIRST_ADMIN_ACCOUNT_ID.toString().equals(result.getValue(ID))) {
                if (status != User.Status.ACTIVE) {
                    result.setError(STATUS, LABEL_WRONG_INPUT);
                }
                if (role != User.Role.ADMIN) {
                    result.setError(ROLE, LABEL_WRONG_INPUT);
                }
            }
        }
        return result;
    }
}
