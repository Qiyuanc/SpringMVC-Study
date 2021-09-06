package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName ControllerTest2
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/31 10:02
 * @Version 1.0
 **/
@Controller
public class ControllerTest2 {
    @RequestMapping("/test2")
    public String Test2(Model model){
        model.addAttribute("msg","Test2Controller");

        return "test";
    }
}
