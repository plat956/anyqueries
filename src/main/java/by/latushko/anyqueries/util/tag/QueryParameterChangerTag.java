package by.latushko.anyqueries.util.tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import static by.latushko.anyqueries.controller.command.identity.PageUrl.QUESTIONS_URL;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.CURRENT_PAGE;

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
        } catch (IOException | URISyntaxException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    private String addParameter(String url, String key, String value) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        List<NameValuePair> queryParameters = uriBuilder.getQueryParams();
        for (Iterator<NameValuePair> queryParameterItr = queryParameters.iterator(); queryParameterItr.hasNext();) {
            NameValuePair queryParameter = queryParameterItr.next();
            if (queryParameter.getName().equals(key)) {
                queryParameterItr.remove();
            }
        }
        uriBuilder.setParameters(queryParameters);
        uriBuilder.addParameter(key, value);
        return uriBuilder.build().toString();
    }
}