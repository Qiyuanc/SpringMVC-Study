package com.qiyuan.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/29 21:51
 * @Version 1.0
 **/
// 这里先使用 Controller 接口实现
public class HelloController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ModelAndView 模型和视图
        ModelAndView mv = new ModelAndView();

        // 封装对象，放在ModelAndView中。Model
        mv.addObject("msg","HelloSpringMVC!");
        // 封装要跳转的视图，放在ModelAndView中
        // 由于设置了前缀和后缀，相当于 /WEB-INF/jsp/hello.jsp
        mv.setViewName("hello");
        return mv;
    }
}
