package by.latushko.anyqueries.util.tag;

import by.latushko.anyqueries.validator.ValidationResult;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * The class represents a jstl tag to detect a form field's CSS class after its validation completed.
 */
public class FieldClassDetectorTag extends TagSupport {
    private static final Logger logger = LogManager.getLogger();
    /**
     * The CSS class indicates that the field value is correct
     */
    private static final String VALID_CLASS = " is-valid-field";
    /**
     * The CSS class indicates that the field value is incorrect
     */
    private static final String INVALID_CLASS = " is-invalid-field";
    /**
     * The empty CSS class indicates that the form with the field still wasn't submitted and validated.
     */
    private static final String NO_CLASS = "";
    /**
     * The form field object
     * @see by.latushko.anyqueries.validator.ValidationResult.Field
     */
    private ValidationResult.Field field;

    /**
     * Sets the field.
     *
     * @param field the field object
     */
    public void setField(ValidationResult.Field field) {
        this.field = field;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            String cl;
            if(field == null || field.getMessage() == null) {
                cl = NO_CLASS;
            } else if(field.getMessage().isEmpty()) {
                cl = VALID_CLASS;
            } else {
                cl = INVALID_CLASS;
            }
            pageContext.getOut().write(cl);
        } catch (IOException e) {
            logger.error("Failed to write tag response", e);
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}
