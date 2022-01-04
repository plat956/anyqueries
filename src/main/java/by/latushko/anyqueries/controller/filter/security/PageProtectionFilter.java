package by.latushko.anyqueries.controller.filter.security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static by.latushko.anyqueries.controller.command.identity.PageUrl.MAIN_URL;

@WebFilter(filterName = "pageProtectionFilter", urlPatterns = "/jsp/*")
public class PageProtectionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.sendRedirect(MAIN_URL);
        chain.doFilter(request, response);
    }
}
