package com.qiyuan.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName InterceptorController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/6 13:44
 * @Version 1.0
 **/
@RestController
public class InterceptorController {

    @RequestMapping("/test")
    public String Test(){
        System.out.println("Controller: 处理请求");
        return "项目运行";
    }
}
