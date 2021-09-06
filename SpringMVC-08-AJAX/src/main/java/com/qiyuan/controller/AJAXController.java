package com.qiyuan.controller;

import com.qiyuan.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AJAXController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/5 13:53
 * @Version 1.0
 **/
@RestController
public class AJAXController {

    @RequestMapping("/test")
    public String Test(){
        return "Request Success!";
    }

    @RequestMapping("/ajax")
    // 传过来的参数叫 name，用 response 响应数据
    public void AJAXTest(String name, HttpServletResponse response) throws IOException {
        if ("qiyuan".equals(name)){
            response.getWriter().print("true");
        }else{
            response.getWriter().print("false");
        }
    }

    @RequestMapping("/ajax2")
    // 传过来的参数叫 name，用 response 响应数据
    public List<User> AJAXTest2() {
        List<User> userList = new ArrayList<User>();
        userList.add(new User("祈鸢",18,"男"));
        userList.add(new User("小祈",18,"女"));
        userList.add(new User("Qiyuan",18,"男"));
        // 直接返回 List，前端接收的是什么类型的呢？
        return userList;
    }

    @RequestMapping("/ajax3")
    // 传过来的参数叫 name，用 response 响应数据
    public String AJAXTest3(String username) {
        String msg = "";
        // 模拟数据库中存在数据
        if (username!=null){
            if ("qiyuan".equals(username)){
                msg = "OK";
            }else {
                msg = "用户名不存在";
            }
        }
        // 由于 @RestController 注解和 jackson 包
        // 返回结果会被转换为 JSON
        return msg;
    }
}
