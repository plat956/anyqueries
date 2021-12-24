package by.latushko.anyqueries.validator;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    private Map<String, Field> fields = new HashMap<>();

    public Field getField(String field) {
        return fields.get(field);
    }

    public void add(String field, String value, boolean status, String message) {
        fields.put(field, new Field(status, value, message));
    }

    public boolean getStatus() {
        return fields.values().stream().noneMatch(f -> !f.getStatus());
    }

    public class Field {
        private Boolean status;
        private String value;
        private String message;

        public Field(boolean status, String value, String message) {
            this.status = status;
            this.value = value;
            this.message = message;
        }

        public Boolean getStatus() {
            return status;
        }

        public String getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }
    }
}
