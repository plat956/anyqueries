package by.latushko.anyqueries.util.tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

import static by.latushko.anyqueries.controller.command.identity.PageUrl.QUESTIONS_URL;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.CURRENT_PAGE;
import static by.latushko.anyqueries.util.http.QueryParameterHelper.addParameter;

public class QueryParameterChangerTag extends TagSupport {
    private String key;
    private String value;

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpSession session = request.getSession();
        String page;
        if(session.getAttribute(CURRENT_PAGE) != null) {
            page = session.getAttribute(CURRENT_PAGE).toString();
        } else {
            page = QUESTIONS_URL;
        }
        try {
            page = addParameter(page, key, value);
            pageContext.getOut().write(page);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}