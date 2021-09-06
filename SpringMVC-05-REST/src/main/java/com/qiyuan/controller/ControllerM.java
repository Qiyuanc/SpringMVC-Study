package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName ControllerM
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/31 22:59
 * @Version 1.0
 **/
@Controller
public class ControllerM {
    @RequestMapping("/testm")
    public String TestM(Model model, HttpServletRequest req, HttpServletResponse resp){
        // 获取本次会话的 Session
        HttpSession session = req.getSession();
        String id = session.getId();
        // 放信息
        model.addAttribute("msg",id);
        return "test";
    }
}
