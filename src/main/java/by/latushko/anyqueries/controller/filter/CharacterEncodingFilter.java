package by.latushko.anyqueries.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;

import java.io.IOException;

@WebFilter(filterName = "characterEncodingFilter", urlPatterns = "/*",
        initParams = @WebInitParam(name = "encoding", value = "UTF-8"))
public class CharacterEncodingFilter implements Filter {
    private static final String ENCODING_PARAMETER = "encoding";
    private String correctEncoding;

    @Override
    public void init(FilterConfig config) throws ServletException {
        correctEncoding = config.getInitParameter(ENCODING_PARAMETER);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String requestEncoding = request.getCharacterEncoding();
        if (correctEncoding != null && !correctEncoding.equalsIgnoreCase(requestEncoding)) {
            request.setCharacterEncoding(correctEncoding);
            response.setCharacterEncoding(correctEncoding);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        correctEncoding = null;
    }
}
