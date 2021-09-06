package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName EncodingController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/1 17:44
 * @Version 1.0
 **/
@Controller
public class EncodingController {
    @RequestMapping(value = "/encoding",method = RequestMethod.POST)
    public String Test(Model model, String name){
        // 前端传的就是 name，可以直接用
        System.out.println(name);
        model.addAttribute("msg",name);
        return "test";
    }
}
