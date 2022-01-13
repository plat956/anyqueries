package by.latushko.anyqueries.validator;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.Map;
import static by.latushko.anyqueries.validator.ValidationPattern.HTML_TAGS_REGEXP;

public interface FormValidator {
    ValidationResult validate(Map<String, String[]> formData);

    default String stripHtml(String data) {
        return StringEscapeUtils.unescapeHtml(data.replaceAll(HTML_TAGS_REGEXP, ""));
    }
}
