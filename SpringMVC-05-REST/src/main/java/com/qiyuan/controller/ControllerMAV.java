package com.qiyuan.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;

/**
 * @ClassName ControllerMV
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/31 22:41
 * @Version 1.0
 **/
public class ControllerMAV implements Controller {

    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        // 获取本次会话的 Session
        HttpSession session = httpServletRequest.getSession();
        String id = session.getId();
        // 放信息，设视图
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg",id);
        mv.setViewName("test");

        return mv;
    }
}
