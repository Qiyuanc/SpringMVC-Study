package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/30 18:47
 * @Version 1.0
 **/
// 这样它就是一个控制器了！
@Controller
public class HelloController {

    // 访问地址 localhost:8080/项目路径/hello
    @RequestMapping("/hello")
    public String Hello(Model model){
        // 处理业务，结果放在 model 中！
        // 不过没返回，前面是怎么获取的呢？
        model.addAttribute("msg","hello,SpringMVC");

        // 返回的视图名称！
        return "hello";
    }
}
