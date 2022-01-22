package by.latushko.anyqueries.validator;

import java.util.HashMap;
import java.util.Map;

/**
 * The Validation result class.
 * Stores full information about the completed form validation
 */
public final class ValidationResult {
    /**
     * The list with all form fields
     */
    private final Map<String, Field> fields = new HashMap<>();

    /**
     * Instantiates a new validation result.
     *
     * @param formData the data from submitted form
     */
    public ValidationResult(Map<String, String[]> formData) {
        for(Map.Entry<String, String[]> field: formData.entrySet()) {
            String value = getFormattedValue(field.getValue());
            fields.put(field.getKey(), new Field(value));
        }
    }

    /**
     * Gets the form field by key.
     *
     * @param key the field key
     * @return the field
     */
    public Field getField(String key) {
        return fields.get(key);
    }

    /**
     * Gets field value.
     *
     * @param key the field key
     * @return the value if the field was found or empty string
     */
    public String getValue(String key) {
        if(fields.containsKey(key)) {
            return fields.get(key).getValue();
        } else {
            return "";
        }
    }

    /**
     * Contains the field by key.
     *
     * @param key the field key
     * @return the boolean, true if the field exists, otherwise false
     */
    public boolean containsField(String key) {
        return fields.containsKey(key);
    }

    /**
     * Gets the field validation message.
     *
     * @param key the field key
     * @return the validation message
     */
    public String getMessage(String key) {
        if(fields.containsKey(key)) {
            return fields.get(key).getMessage();
        } else {
            return null;
        }
    }

    /**
     * Sets field error message.
     *
     * @param key   the field key
     * @param message the validation message
     */
    public void setError(String key, String message) {
        if(fields.containsKey(key)) {
            Field f = fields.get(key);
            f.setMessage(message);
        } else {
            Field f = new Field();
            f.setMessage(message);
            fields.put(key, f);
        }
    }

    /**
     * Gets form global validation status.
     *
     * @return the status of form validation
     */
    public boolean getStatus() {
        return fields.values().stream().noneMatch(f -> f.getMessage() != null && !f.getMessage().isEmpty());
    }

    /**
     * Transfer submitted value from array to single string
     * @param values request parameter value as an array
     * @return converted value
     */
    private String getFormattedValue(String[] values) {
        if(values != null && values.length > 0) {
            String value = values[0];
            if(!value.isEmpty()) {
                return value;
            }
        }
        return null;
    }

    /**
     * The Field class.
     * Represents a form field with its submitted value and validation result message
     * @see by.latushko.anyqueries.util.tag.FieldClassDetectorTag
     */
    public class Field {
        private String value;
        private String message;

        /**
         * Instantiates a new Field.
         */
        public Field() {
        }

        /**
         * Instantiates a new Field.
         *
         * @param value the value
         */
        public Field(String value) {
            this.value = value;
            this.message = "";
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * Gets message.
         *
         * @return the message
         */
        public String getMessage() {
            return message;
        }

        /**
         * Sets message.
         *
         * @param message the message
         */
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
