package com.qiyuan.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName MyInterceptor
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/6 13:46
 * @Version 1.0
 **/
public class MyInterceptor implements HandlerInterceptor {
    // 在处理请求的方法执行前 执行 preHandle
    // 返回 true 放行
    // 返回 false 拦截！
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle: 处理请求前");
        return true;
    }

    // 在处理请求的方法执行后 执行 postHandle
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle: 处理请求后");
    }

    // 在 DispatcherServlet 处理后执行 afterCompletion 进行清理工作.
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion: 清理——！");
    }
}
