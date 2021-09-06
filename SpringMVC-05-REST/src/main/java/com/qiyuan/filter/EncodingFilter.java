package com.qiyuan.filter;


import javax.servlet.*;
import java.io.IOException;

/**
 * @ClassName EncodingFilter
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/1 17:58
 * @Version 1.0
 **/
public class EncodingFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 执行过滤行为
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        // 过滤完成，放行
        filterChain.doFilter(servletRequest,servletResponse);
    }

    public void destroy() {

    }
}
