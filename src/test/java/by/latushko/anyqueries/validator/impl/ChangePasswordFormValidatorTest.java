package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ChangePasswordFormValidatorTest {

    private FormValidator validator = ChangePasswordFormValidator.getInstance();

    @Test(dataProvider = "changePasswordCorrect", dataProviderClass = FormValidatorDataProvider.class)
    public void testValidate(Map<String, String[]> formData) {
        ValidationResult result = validator.validate(formData);
        boolean actual = result.getStatus();
        assertTrue(actual);
    }

    @Test(dataProvider = "changePasswordWrong", dataProviderClass = FormValidatorDataProvider.class)
    public void testValidateFalse(Map<String, String[]> formData) {
        ValidationResult result = validator.validate(formData);
        boolean actual = result.getStatus();
        assertFalse(actual);
    }
}