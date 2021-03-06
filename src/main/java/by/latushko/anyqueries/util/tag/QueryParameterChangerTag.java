package by.latushko.anyqueries.util.tag;

import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static by.latushko.anyqueries.controller.command.identity.PageUrl.CONTROLLER_URL;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.QUESTIONS_URL;
import static by.latushko.anyqueries.util.http.QueryParameterHelper.addParameter;
import static by.latushko.anyqueries.util.http.QueryParameterHelper.removeParameter;

/**
 * The class represents a jstl tag to change a query parameter in the url.
 */
public class QueryParameterChangerTag extends TagSupport {
    private static final Logger logger = LogManager.getLogger();
    private static final String QUERY_STRING_DELIMITER = "?";
    private String url;
    private String key;
    private String value;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String page;
        if(url != null) {
            page = url;
        } else if(request.getParameter(RequestParameter.COMMAND) != null) {
            page = request.getContextPath() + CONTROLLER_URL + QUERY_STRING_DELIMITER + request.getQueryString();
        } else {
            page = request.getContextPath() + QUESTIONS_URL;
        }
        try {
            if(value != null && !value.isEmpty()) {
                page = addParameter(page, key, value);
            } else {
                page = removeParameter(page, key, value);
            }
            pageContext.getOut().write(page);
        } catch (IOException e) {
            logger.error("Failed to write tag response", e);
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}