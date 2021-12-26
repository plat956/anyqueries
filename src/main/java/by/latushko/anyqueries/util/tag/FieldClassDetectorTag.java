package by.latushko.anyqueries.util.tag;

import by.latushko.anyqueries.validator.ValidationResult;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

public class FieldClassDetectorTag extends TagSupport {
    private static final String VALID_CLASS = " is-valid-field";
    private static final String INVALID_CLASS = " is-invalid-field";
    private static final String NO_CLASS = "";
    private ValidationResult.Field field;

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
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}
