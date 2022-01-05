package by.latushko.anyqueries.util.tag;

import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;

public class PluralFormatterTag extends TagSupport {
    private long count;
    private String key;

    public void setCount(long count) {
        this.count = count;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        String result = manager.getMessageInPlural(key, count);
        try {
            pageContext.getOut().write(result);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}