package com.qiyuan.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName LoginInterceptor
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/6 16:56
 * @Version 1.0
 **/
public class LoginInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getRequestURI());
        if(request.getRequestURI().contains("login")){
            // 如果请求包含 login ，放行！
            return true;
        }
        // 获取当前会话 session
        HttpSession session = request.getSession();
        if(session.getAttribute("user")!=null){
            // session 中存在登录信息，放行！
            return true;
        }
        // 用户为登录访问其他页面，直接赶到登录页面去！
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request,response);
        // 拦截！！！
        return false;
    }
}
