package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName ControllerTest3
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/31 10:46
 * @Version 1.0
 **/
@Controller
@RequestMapping("/qiyuan")
public class ControllerTest3 {
    @RequestMapping("/test3")
    public String Test3(Model model){
        model.addAttribute("msg","Test3Controller");

        return "test";
    }
}
