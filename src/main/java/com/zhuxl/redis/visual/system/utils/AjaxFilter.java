//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.system.utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxFilter implements Filter {
    public AjaxFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequestt, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequestt;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String ajaxSubmit = request.getHeader("X-Requested-With");
        if (ajaxSubmit != null && ajaxSubmit.equals("XMLHttpRequest") && request.getSession(false) == null) {
            response.setHeader("sessionstatus", "timeout");
            response.getWriter().print("sessionstatus");
        } else {
            chain.doFilter(servletRequestt, servletResponse);
        }
    }

    public void destroy() {
    }
}
