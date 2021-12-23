package by.latushko.anyqueries.controller.filter.security;

import by.latushko.anyqueries.controller.command.identity.PagePath;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "pageProtectionFilter", urlPatterns = "/jsp/*")
public class PageProtectionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.sendRedirect(PagePath.MAIN_URL);
        chain.doFilter(request, response);
    }
}
