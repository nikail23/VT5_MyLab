package horseraceapp.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import horseraceapp.util.dao.entity.User;
import horseraceapp.util.dao.entity.UserType;

@WebFilter(filterName = "AdminLoginFilter", urlPatterns = {"/app/admin/*"})
public class AdminLoginFilter extends AbstractLoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        HttpSession session = httpReq.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute(USER);
            if (user != null) {
                if (user.getType() == UserType.ADMIN) {
                    chain.doFilter(request, response);
                } else {
                    httpResp.sendRedirect(httpReq.getContextPath() + USER_DEFAULT_PAGE);
                }
            } else {
                httpResp.sendRedirect(httpReq.getContextPath() + DEFAULT_PAGE);
            }
        } else {
            httpResp.sendRedirect(httpReq.getContextPath() + DEFAULT_PAGE);
        }
    }
}
