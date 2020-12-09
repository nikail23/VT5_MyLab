package horseraceapp.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public abstract class AbstractLoginFilter implements Filter {
    static final String USER = "user";
    static final String DEFAULT_PAGE = "/index.jsp";   
    static final String USER_DEFAULT_PAGE = "/app/main.jsp";

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
}
