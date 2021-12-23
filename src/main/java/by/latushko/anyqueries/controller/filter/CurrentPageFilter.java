package by.latushko.anyqueries.controller.filter;

import by.latushko.anyqueries.controller.command.RequestAttribute;
import by.latushko.anyqueries.controller.command.RequestParameter;
import by.latushko.anyqueries.util.http.RequestMethod;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(filterName = "currentPageFilter", urlPatterns = "/controller")
public class CurrentPageFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getMethod().equals(RequestMethod.GET.name()) &&
                request.getParameter(RequestParameter.AJAX) == null &&
                request.getParameter(RequestParameter.COMMAND) != null) {
            String currentPage = "/controller?" + request.getQueryString();
            request.getSession().setAttribute(RequestAttribute.CURRENT_PAGE, currentPage);
        }
        chain.doFilter(request, servletResponse);
    }
}