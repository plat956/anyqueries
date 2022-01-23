package by.latushko.anyqueries.validator.impl;

import by.latushko.anyqueries.validator.FormValidator;
import by.latushko.anyqueries.validator.ValidationResult;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class CategoryFormValidatorTest {

    private FormValidator validator = CategoryFormValidator.getInstance();

    @Test(dataProvider = "categoryCorrect", dataProviderClass = FormValidatorDataProvider.class)
    public void testValidate(Map<String, String[]> formData) {
        ValidationResult result = validator.validate(formData);
        boolean actual = result.getStatus();
        assertTrue(actual);
    }

    @Test(dataProvider = "categoryWrong", dataProviderClass = FormValidatorDataProvider.class)
    public void testValidateFalse(Map<String, String[]> formData) {
        ValidationResult result = validator.validate(formData);
        boolean actual = result.getStatus();
        assertFalse(actual);
    }
}