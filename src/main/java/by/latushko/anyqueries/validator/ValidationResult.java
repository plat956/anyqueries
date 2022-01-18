package by.latushko.anyqueries.validator;

import java.util.HashMap;
import java.util.Map;

public final class ValidationResult {
    private final Map<String, Field> fields = new HashMap<>();

    public ValidationResult(Map<String, String[]> formData) {
        for(Map.Entry<String, String[]> field: formData.entrySet()) {
            String value = getFormattedValue(field.getValue());
            fields.put(field.getKey(), new Field(value));
        }
    }

    public Field getField(String key) {
        return fields.get(key);
    }

    public String getValue(String field) {
        String value = null;
        if(fields.containsKey(field)) {
            value = fields.get(field).getValue();
        }
        return value != null ? value : "";
    }

    public boolean containsField(String field) {
        return fields.containsKey(field);
    }

    public String getMessage(String field) {
        if(fields.containsKey(field)) {
            return fields.get(field).getMessage();
        } else {
            return null;
        }
    }

    public void setError(String field, String message) {
        if(fields.containsKey(field)) {
            Field f = fields.get(field);
            f.setMessage(message);
        } else {
            Field f = new Field();
            f.setMessage(message);
            fields.put(field, f);
        }
    }

    public boolean getStatus() {
        return fields.values().stream().noneMatch(f -> f.getMessage() != null && !f.getMessage().isEmpty());
    }

    private String getFormattedValue(String[] values) {
        if(values != null && values.length > 0) {
            String value = values[0];
            if(!value.isEmpty()) {
                return value;
            }
        }
        return null;
    }

    public class Field {
        private String value;
        private String message;

        public Field() {
        }

        public Field(String value) {
            this.value = value;
            this.message = "";
        }

        public String getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
