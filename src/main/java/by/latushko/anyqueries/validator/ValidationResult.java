package by.latushko.anyqueries.validator;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    private Map<String, Field> fields = new HashMap<>();

    public Field getField(String field) {
        return fields.get(field);
    }

    public void add(String field, String value, String message) {
        fields.put(field, new Field(value, message));
    }

    public void add(String field, String value) {
        fields.put(field, new Field(value));
    }


    public boolean getStatus() {
        return fields.values().stream().noneMatch(f -> f.getStatus().equals(Field.Status.INVALID));
    }

    public class Field {
        public enum Status {
            VALID, INVALID;
        }

        private String value;
        private Status status;
        private String message;

        public Field(String value) {
            this.value = value;
            this.status = Status.VALID;
        }

        public Field(String value, String message) {
            this.value = value;
            this.message = message;
            this.status = Status.INVALID;
        }

        public Status getStatus() {
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
