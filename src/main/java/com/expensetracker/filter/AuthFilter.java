package com.expensetracker.filter;

import com.expensetracker.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        
        String path = req.getRequestURI().substring(req.getContextPath().length());
        
        boolean isStatic = path.startsWith("/css/") || path.startsWith("/js/") || 
                           path.startsWith("/images/") || path.contains("webjars") || 
                           path.endsWith(".css") || path.endsWith(".js") || 
                           path.endsWith(".png") || path.endsWith(".jpg") || 
                           path.endsWith(".ico");
                           
        boolean isAuthPage = path.equals("/login") || path.equals("/register") || 
                             path.contains("login.jsp") || path.contains("register.jsp") ||
                             path.equals("/") || path.isEmpty();

        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (loggedIn || isStatic || isAuthPage) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
