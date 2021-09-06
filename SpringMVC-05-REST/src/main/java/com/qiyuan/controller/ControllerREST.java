package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName ControllerREST
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/31 15:06
 * @Version 1.0
 **/
@Controller
public class ControllerREST {

    // 请求路径: .../add?a=1&b=1
    @RequestMapping("/add")
    public String Add(int a, int b, Model model) {
        // 业务处理
        int res = a + b;
        model.addAttribute("msg","a + b = "+res);
        // 返回视图
        return "test";
    }

    // 请求路径: .../add/1/2
    @RequestMapping("/add/{a}/{b}")
    public String AddREST(@PathVariable int a,@PathVariable int b, Model model) {
        // 业务处理
        int res = a + b;
        model.addAttribute("msg","a + b = "+res);
        // 返回视图
        return "test";
    }

    // 请求路径: .../add/1/2，且请求方法必须为 GET
    @RequestMapping(value = "/add/{a}/{b}",method = RequestMethod.GET)
    // @GetMapping("/add/{a}/{b}")
    public String AddRESTGET(@PathVariable int a,@PathVariable int b, Model model) {
        // 业务处理
        int res = a + b;
        model.addAttribute("msg","a + b = "+res);
        // 返回视图
        return "test";
    }


}
