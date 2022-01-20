package by.latushko.anyqueries.util.tag;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ListFormatterTag extends TagSupport {
    private static final Logger logger = LogManager.getLogger();
    private static final String ELEMENT_DELIMITER = ",";
    private List list;

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            Object result = list.stream().map(Object::toString).collect(Collectors.joining(ELEMENT_DELIMITER));
            pageContext.getOut().write(result.toString());
        } catch (IOException e) {
            logger.error("Failed to write tag response", e);
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}
