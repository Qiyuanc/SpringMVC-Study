package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/6 16:35
 * @Version 1.0
 **/
@Controller
@RequestMapping("/user")
public class LoginController {
    // 跳转到登陆页面
    @RequestMapping("/tologin")
    public String toLogin() {
        System.out.println("转向 login 页面");
        return "login";
    }

    // 跳转到登录成功页面
    @RequestMapping("/tosuccess")
    public String toSuccess() {
        return "success";
    }

    // 处理登陆请求
    @RequestMapping("/login")
    public String Login(HttpSession session, String username, String pwd) throws Exception {
        // 与数据库对比用户信息

        // 登录成功向 session 写入用户信息
        System.out.println("前端填写用户名: "+username);
        session.setAttribute("user", username);
        return "success";
    }

    // 退出登陆
    @RequestMapping("/logout")
    public String Logout(HttpSession session) throws Exception {
        // 使当前 session 过期
        session.invalidate();
        return "login";
    }
}
