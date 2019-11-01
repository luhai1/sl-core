package com.sl.common.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecuritySessionFilter implements Filter  {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            throw new ServletException("Can only process HttpServletRequest");
        }

        if (!(response instanceof HttpServletResponse)) {
            throw new ServletException("Can only process HttpServletResponse");
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession httpSession = null;
        try {
            httpSession = httpRequest.getSession(false);
        } catch (IllegalStateException ignored) {
        }

        Cookie cookies[] =httpRequest.getCookies();
        chain.doFilter(request, response);
    }
}
