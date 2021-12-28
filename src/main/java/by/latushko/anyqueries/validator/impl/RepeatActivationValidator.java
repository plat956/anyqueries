package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;

import java.util.Map;

import static by.latushko.anyqueries.controller.command.identity.RequestParameter.EMAIL;
import static by.latushko.anyqueries.controller.command.identity.RequestParameter.TELEGRAM;
import static by.latushko.anyqueries.util.i18n.MessageKey.LABEL_WRONG_INPUT;
import static by.latushko.anyqueries.validator.ValidationPattern.EMAIL_REGEXP;
import static by.latushko.anyqueries.validator.ValidationPattern.TELEGRAM_REGEXP;

public class RepeatActivationValidator implements FormValidator {
    private static FormValidator instance;

    private RepeatActivationValidator() {
    }

    public static FormValidator getInstance() {
        if(instance == null) {
            instance = new RepeatActivationValidator();
        }
        return instance;
    }

    @Override
    public ValidationResult validate(Map<String, String[]> formData) {
        ValidationResult result = new ValidationResult(formData);
        boolean sendLink = result.containsField(RequestParameter.SEND_LINK);
        String email = result.getValue(RequestParameter.EMAIL);
        String telegram = result.getValue(RequestParameter.TELEGRAM);
        if((sendLink || !email.isEmpty()) && !result.getValue(EMAIL).matches(EMAIL_REGEXP)) {
            result.setError(EMAIL, LABEL_WRONG_INPUT);
        }
        if((!sendLink || !telegram.isEmpty()) && !result.getValue(TELEGRAM).matches(TELEGRAM_REGEXP)) {
            result.setError(TELEGRAM, LABEL_WRONG_INPUT);
        }
        return result;
    }
}
