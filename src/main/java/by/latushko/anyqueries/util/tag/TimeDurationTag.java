package by.latushko.anyqueries.util.tag;

import by.latushko.anyqueries.util.http.CookieHelper;
import by.latushko.anyqueries.util.i18n.MessageManager;
import by.latushko.anyqueries.util.i18n.TimeDuration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.time.LocalDateTime;

import static by.latushko.anyqueries.controller.command.identity.CookieName.LANG;

public class TimeDurationTag extends TagSupport {
    private LocalDateTime date;

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String userLang = CookieHelper.readCookie(request, LANG);
        MessageManager manager = MessageManager.getManager(userLang);
        String result = TimeDuration.format(date, manager);
        try {
            pageContext.getOut().write(result);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}
