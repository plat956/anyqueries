package by.latushko.anyqueries.validator;

import java.util.Map;

public interface FormValidator {
    ValidationResult validate(Map<String, String[]> formData);
}
