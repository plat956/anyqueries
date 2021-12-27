package by.latushko.anyqueries.controller.filter;

import by.latushko.anyqueries.controller.command.identity.RequestParameter;
import by.latushko.anyqueries.util.http.RequestMethod;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static by.latushko.anyqueries.controller.command.identity.PagePath.CONTROLLER_URL;
import static by.latushko.anyqueries.controller.command.identity.SessionAttribute.CURRENT_PAGE;

@WebFilter(filterName = "currentPageFilter", urlPatterns = "/controller")
public class CurrentPageFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getMethod().equals(RequestMethod.GET.name()) &&
                request.getParameter(RequestParameter.AJAX) == null &&
                request.getParameter(RequestParameter.COMMAND) != null) {
            String currentPage = CONTROLLER_URL + "?" + request.getQueryString();
            HttpSession session = request.getSession();
            session.setAttribute(CURRENT_PAGE, currentPage);
        }
        chain.doFilter(request, servletResponse);
    }
}