package by.latushko.anyqueries.util.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import static by.latushko.anyqueries.util.i18n.MessageKey.*;

public enum MessageManager {
    RU(ResourceBundle.getBundle(MESSAGES_FILE_NAME)),
    EN(ResourceBundle.getBundle(MESSAGES_FILE_NAME, new Locale(LOCALE_EN))),
    BE(ResourceBundle.getBundle(MESSAGES_FILE_NAME, new Locale(LOCALE_BE)));

    private ResourceBundle bundle;

    MessageManager(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getMessage(String key) {
        return bundle.getString(key);
    }

    public String getMessage(String key, Object... variables) {
        String result = bundle.getString(key);
        return String.format(result, variables);
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
}
