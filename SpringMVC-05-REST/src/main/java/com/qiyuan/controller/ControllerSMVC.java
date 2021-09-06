package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName ControllerSMVC
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/31 23:28
 * @Version 1.0
 **/
@Controller
public class ControllerSMVC {

    @RequestMapping("/testsmvc/forward")
    public String TestF(Model model){
        model.addAttribute("msg","Qiyuan转发！");
        // 转发
        // 没有视图解析器当然要手动拼接辽
        return "forward:/WEB-INF/jsp/test.jsp";
    }

    @RequestMapping("/testsmvc/redirect")
    public String TestM(Model model){
        model.addAttribute("msg","Qiyuan重定向！");
        // 重定向
        // 这里和直接用 Servlet 不一样！SpringMVC 会自动补上项目路径！
        return "redirect:/index.jsp";
    }
}
