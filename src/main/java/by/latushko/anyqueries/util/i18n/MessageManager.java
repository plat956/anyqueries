package by.latushko.anyqueries.util.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import static by.latushko.anyqueries.util.i18n.MessageKey.*;
import static java.lang.Math.abs;

public enum MessageManager {
    RU(ResourceBundle.getBundle(MESSAGES_FILE_NAME)),
    EN(ResourceBundle.getBundle(MESSAGES_FILE_NAME, new Locale(LOCALE_EN))),
    BE(ResourceBundle.getBundle(MESSAGES_FILE_NAME, new Locale(LOCALE_BE)));

    private static final String MESSAGE_1_POSTFIX = "1";
    private static final String MESSAGE_2_POSTFIX = "2";
    private static final String MESSAGE_5_POSTFIX = "5";
    private ResourceBundle bundle;
    public static final String SPACE_CHARACTER = " ";

    MessageManager(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public static MessageManager getManager(String lang) {
        if(lang == null) {
            return RU;
        }
        try {
            return MessageManager.valueOf(lang.toUpperCase());
        } catch (IllegalArgumentException e) {
            return RU;
        }
    }

    public String getMessage(String key) {
        return bundle.getString(key);
    }

    public String getMessage(String key, Object... variables) {
        String result = bundle.getString(key);
        return String.format(result, variables);
    }

    public String getMessageInPlural(String key, long count) {
        key = buildPluralMessageKey(count, key);
        String result = bundle.getString(key);
        if(count == 0) {
            result = bundle.getString(LABEL_NO) + SPACE_CHARACTER + result;
        } else {
            result = count + SPACE_CHARACTER + result;
        }
        return result;
    }

    private String buildPluralMessageKey(long count, String key) {
        count = abs(count) % 100;
        count = count % 10;
        if (count > 10 && count < 20) {
            return key + MESSAGE_5_POSTFIX;
        }
        if (count > 1 && count < 5) {
            return key + MESSAGE_2_POSTFIX;
        }
        if (count == 1) {
            return key + MESSAGE_1_POSTFIX;
        }
        return key + MESSAGE_5_POSTFIX;
    }
}
