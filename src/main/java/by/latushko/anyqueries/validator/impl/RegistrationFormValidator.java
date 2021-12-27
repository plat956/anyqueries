package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.*;
import static by.latushko.anyqueries.util.i18n.MessageKey.LABEL_WRONG_INPUT;
import static by.latushko.anyqueries.validator.ValidationPattern.*;

public class RegistrationFormValidator implements FormValidator {
    private static FormValidator instance;

    private RegistrationFormValidator() {
    }

    public static FormValidator getInstance() {
        if(instance == null) {
            instance = new RegistrationFormValidator();
        }
        return instance;
    }

    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult(formData);
        boolean sendLink = result.containsField(RequestParameter.SEND_LINK);
        String email = result.getValue(RequestParameter.EMAIL);
        String telegram = result.getValue(RequestParameter.TELEGRAM);
        String passwordConfirm = result.getValue(PASSWORD_CONFIRMED);

        if(!result.getValue(FIRST_NAME).matches(FIRST_NAME_REGEXP)) {
            result.setError(FIRST_NAME, LABEL_WRONG_INPUT);
        }
        if(!result.getValue(LAST_NAME).matches(LAST_NAME_REGEXP)) {
            result.setError(LAST_NAME, LABEL_WRONG_INPUT);
        }
        if(!result.getValue(MIDDLE_NAME).matches(MIDDLE_NAME_REGEXP)) {
            result.setError(MIDDLE_NAME, LABEL_WRONG_INPUT);
        }
        if((sendLink || !email.isEmpty()) && !result.getValue(EMAIL).matches(EMAIL_REGEXP)) {
            result.setError(EMAIL, LABEL_WRONG_INPUT);
        }
        if((!sendLink || !telegram.isEmpty()) && !result.getValue(TELEGRAM).matches(TELEGRAM_REGEXP)) {
            result.setError(TELEGRAM, LABEL_WRONG_INPUT);
        }
        if(!result.getValue(LOGIN).matches(LOGIN_REGEXP)) {
            result.setError(LOGIN, LABEL_WRONG_INPUT);
        }
        if(!result.getValue(PASSWORD).matches(PASSWORD_REGEXP)) {
            result.setError(PASSWORD, LABEL_WRONG_INPUT);
        }
        if(!result.getValue(PASSWORD_CONFIRMED).matches(PASSWORD_REGEXP)) {
            result.setError(PASSWORD_CONFIRMED, LABEL_WRONG_INPUT);
        }
        if(!result.getValue(PASSWORD).equals(passwordConfirm)) {
            result.setError(PASSWORD_CONFIRMED, LABEL_WRONG_INPUT);
        }
        return result;
    }
}
