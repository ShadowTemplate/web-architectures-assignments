package bookshop.servlet.filter;

import bookshop.web.Pages;
import bookshop.web.SessionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/UserAuthenticationFilter")
public class UserAuthenticationFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        SessionManager sessionManager = new SessionManager(((HttpServletRequest) request).getSession());
        if(sessionManager.getUser() == null || sessionManager.getCart() == null) {
            ((HttpServletResponse) response).sendRedirect(Pages.LOGIN);
        } else {
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }


    public void destroy() {
    }

}