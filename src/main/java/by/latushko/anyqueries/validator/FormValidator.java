package by.latushko.anyqueries.validator;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.Map;

import static by.latushko.anyqueries.validator.ValidationPattern.HTML_TAGS_REGEXP;

/**
 * The Form validator interface.
 */
public interface FormValidator {
    /**
     * Validate submitted form data.
     *
     * @param formData the form data from http request
     * @return the object with information about validation result
     */
    ValidationResult validate(Map<String, String[]> formData);

    /**
     * Remove all html tags from string.
     *
     * @param data the string containing html
     * @return the stripped string
     */
    default String stripHtml(String data) {
        return StringEscapeUtils.unescapeHtml(data.replaceAll(HTML_TAGS_REGEXP, ""));
    }
}
