package by.latushko.anyqueries.validator;

public final class ValidationPattern {
    public static final String LOGIN_REGEXP = "[A-Za-z0-9]{1,20}";
    public static final String PASSWORD_REGEXP = "(?=.*[0-9])(?=.*[A-ZА-Я])\\S{6,25}";
    public static final String FIRST_NAME_REGEXP = "[A-Za-zА-Яа-я]{1,25}";
    public static final String LAST_NAME_REGEXP = "[A-Za-zА-Яа-я]{1,25}";
    public static final String MIDDLE_NAME_REGEXP = "[A-Za-zА-Яа-я]{1,25}";
    public static final String EMAIL_REGEXP = "(?=^.{1,100}$)([A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})";
    public static final String TELEGRAM_REGEXP = "[a-z0-9_]{5,32}";
    public static final String CATEGORY_NAME_REGEXP = "[a-zA-Zа-яА-Я\\s0-9]{1,25}";
    public static final String HEX_COLOR_REGEXP = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";
    public static final String HTML_TAGS_REGEXP = "(?s)<[^>]*>(\\s*<[^>]*>)*";

    private ValidationPattern() {
    }
}
