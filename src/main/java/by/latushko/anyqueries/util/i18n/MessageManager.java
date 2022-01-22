package by.latushko.anyqueries.util.i18n;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ResourceBundle;

import static by.latushko.anyqueries.util.i18n.MessageKey.*;

/**
 * The Message manager enum.
 * Manages generation of localized messages
 */
public enum MessageManager {
    /**
     * Russian message manager.
     */
    RU(ResourceBundle.getBundle(MESSAGES_FILE_NAME_PREFIX + LOCALE_RU)),
    /**
     * English message manager.
     */
    EN(ResourceBundle.getBundle(MESSAGES_FILE_NAME_PREFIX + LOCALE_EN)),
    /**
     * Belarusian message manager.
     */
    BE(ResourceBundle.getBundle(MESSAGES_FILE_NAME_PREFIX + LOCALE_BE));

    private static final Logger logger = LogManager.getLogger();
    /**
     * The constant SPACE_CHARACTER represents the white space.
     */
    public static final String SPACE_CHARACTER = " ";

    private final ResourceBundle bundle;

    MessageManager(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    /**
     * Gets manager by language.
     *
     * @param lang the language
     * @return the manager
     */
    public static MessageManager getManager(String lang) {
        if(lang == null) {
            return RU;
        }
        try {
            return MessageManager.valueOf(lang.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Wrong language request: {}", lang, e);
            return RU;
        }
    }

    /**
     * Gets message by key.
     *
     * @param key the message key
     * @return the message value
     */
    public String getMessage(String key) {
        return bundle.getString(key);
    }

    /**
     * Gets message with injected variables.
     *
     * @param key       the message key
     * @param variables the variables to inject
     * @return the result message
     */
    public String getMessage(String key, Object... variables) {
        String result = bundle.getString(key);
        return String.format(result, variables);
    }

    /**
     * Gets message in plural by number.
     *
     * @param key   the message key prefix
     * @param count the number to build a message
     * @return the result message
     */
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
        if(this == EN) {
            if(count == 1) {
                return key + PLURAL_MESSAGE_1_POSTFIX;
            } else {
                return key + PLURAL_MESSAGE_2_POSTFIX;
            }
        } else {
            count = Math.abs(count) % 100;
            if (count > 10 && count < 20) {
                return key + PLURAL_MESSAGE_5_POSTFIX;
            }
            count %= 10;
            if (count > 1 && count < 5) {
                return key + PLURAL_MESSAGE_2_POSTFIX;
            }
            if (count == 1) {
                return key + PLURAL_MESSAGE_1_POSTFIX;
            }
            return key + PLURAL_MESSAGE_5_POSTFIX;
        }
    }
}
