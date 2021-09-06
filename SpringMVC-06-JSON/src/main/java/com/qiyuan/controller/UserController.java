package com.qiyuan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.qiyuan.entity.User;
import com.qiyuan.utils.JsonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/2 16:16
 * @Version 1.0
 **/
@RestController
//@Controller
public class UserController {

    @RequestMapping(value = "/json1", produces = "application/json;charset=utf-8")
    // 使用这个注解，返回的结果就不会去视图解析器，还是一个字符串
    //@ResponseBody
    public String JSONTest1() throws JsonProcessingException {
        // 创建对象
        User user = new User("祈鸢", 18, "男");
        // 因为 @ResponseBody，返回的就是一个普通字符串
        return user.toString();
    }

    @RequestMapping(value = "/json2")
    // 使用这个注解，返回的结果就不会去视图解析器，还是一个字符串
    //@ResponseBody
    public String JSONTest2() throws JsonProcessingException {
        //创建 jackson 的对象映射器以解析数据
        ObjectMapper mapper = new ObjectMapper();
        // 创建对象
        User user = new User("祈鸢", 18, "男");
        // 把对象转换为 json 字符串
        String str = mapper.writeValueAsString(user);
        // 因为 @ResponseBody，返回的就是一个普通字符串
        return str;
    }

    @RequestMapping("/json3")
    public String JSONTest3() throws JsonProcessingException {

        //创建 jackson 的对象映射器以解析数据
        ObjectMapper mapper = new ObjectMapper();
        // 创建好多对象
        User user1 = new User("祈鸢A", 18, "男");
        User user2 = new User("祈鸢B", 18, "男");
        User user3 = new User("祈鸢C", 18, "男");
        User user4 = new User("祈鸢D", 18, "男");
        List<User> list = new ArrayList<User>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        list.add(user4);

        // 将对象解析成为 JSON 字符串
        String str = mapper.writeValueAsString(list);
        return str;
    }

    @RequestMapping("/json4")
    public String JSONTest4() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        //创建时间对象 注意是 java.util.Date
        Date date = new Date();
        // 将对象解析成为 JSON 字符串
        String str = mapper.writeValueAsString(date);
        return str;
    }

    @RequestMapping("/json5")
    public String JSONTest5() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        // 不使用时间戳
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 自定义日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 指定日期格式
        mapper.setDateFormat(sdf);

        Date date = new Date();
        String str = mapper.writeValueAsString(date);

        return str;
    }

    @RequestMapping("/json6")
    public String JSONTest6() throws JsonProcessingException {

        //创建时间对象 注意是 java.util.Date
        Date date = new Date();
        // 将对象解析成为 JSON 字符串
        String str = JsonUtil.getJson(date);
        return str;
    }
}
