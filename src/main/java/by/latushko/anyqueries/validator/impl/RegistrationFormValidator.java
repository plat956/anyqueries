package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.util.i18n.MessageKey;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationPattern;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

public class RegistrationFormValidator implements FormValidator {
    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult(formData);

        boolean sendLink = result.containsField(RequestParameter.SEND_LINK);
        String email = result.getValue(RequestParameter.EMAIL);
        String telegram = result.getValue(RequestParameter.TELEGRAM);

        if(!result.getValue(RequestParameter.FIRST_NAME).matches(ValidationPattern.FIRST_NAME_REGEXP)) {
            result.setError(RequestParameter.FIRST_NAME, MessageKey.LABEL_WRONG_INPUT);
        }
        if(!result.getValue(RequestParameter.LAST_NAME).matches(ValidationPattern.LAST_NAME_REGEXP)) {
            result.setError(RequestParameter.LAST_NAME, MessageKey.LABEL_WRONG_INPUT);
        }
        if(!result.getValue(RequestParameter.MIDDLE_NAME).matches(ValidationPattern.MIDDLE_NAME_REGEXP)) {
            result.setError(RequestParameter.MIDDLE_NAME, MessageKey.LABEL_WRONG_INPUT);
        }
        if((sendLink || !email.isEmpty()) && !result.getValue(RequestParameter.EMAIL).matches(ValidationPattern.EMAIL_REGEXP)) {
            result.setError(RequestParameter.EMAIL, MessageKey.LABEL_WRONG_INPUT);
        }
        if((!sendLink || !telegram.isEmpty()) && !result.getValue(RequestParameter.TELEGRAM).matches(ValidationPattern.TELEGRAM_REGEXP)) {
            result.setError(RequestParameter.TELEGRAM, MessageKey.LABEL_WRONG_INPUT);
        }
        if(!result.getValue(RequestParameter.LOGIN).matches(ValidationPattern.LOGIN_REGEXP)) {
            result.setError(RequestParameter.LOGIN, MessageKey.LABEL_WRONG_INPUT);
        }
        if(!result.getValue(RequestParameter.PASSWORD).matches(ValidationPattern.PASSWORD_REGEXP)) {
            result.setError(RequestParameter.PASSWORD, MessageKey.LABEL_WRONG_INPUT);
        }
        if(!result.getValue(RequestParameter.PASSWORD_CONFIRMED).matches(ValidationPattern.PASSWORD_REGEXP)) {
            result.setError(RequestParameter.PASSWORD_CONFIRMED, MessageKey.LABEL_WRONG_INPUT);
        }
        if(!result.getValue(RequestParameter.PASSWORD).equals(result.getValue(RequestParameter.PASSWORD_CONFIRMED))) {
            result.setError(RequestParameter.PASSWORD_CONFIRMED, MessageKey.LABEL_WRONG_INPUT);
        }

        return result;
    }
}
