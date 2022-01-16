package by.latushko.anyqueries.util.tag;

import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

import static by.latushko.anyqueries.controller.command.identity.PageUrl.CONTROLLER_URL;
import static by.latushko.anyqueries.controller.command.identity.PageUrl.QUESTIONS_URL;
import static by.latushko.anyqueries.util.http.QueryParameterHelper.addParameter;
import static by.latushko.anyqueries.util.http.QueryParameterHelper.removeParameter;

public class QueryParameterChangerTag extends TagSupport {
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
            page = CONTROLLER_URL + QUERY_STRING_DELIMITER + request.getQueryString();
        } else {
            page = QUESTIONS_URL;
        }
        try {
            if(value != null && !value.isEmpty()) {
                page = addParameter(page, key, value);
            } else {
                page = removeParameter(page, key, value);
            }
            pageContext.getOut().write(page);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}