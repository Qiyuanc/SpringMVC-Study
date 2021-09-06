package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName ControllerSAPI
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/31 23:08
 * @Version 1.0
 **/
@Controller
public class ControllerSAPI {
    @RequestMapping("/testsapi/forward")
    public void TestF(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 用原生 API 当然需要这两个参数了
        req.setAttribute("msg","转发啦");
        req.getRequestDispatcher("/WEB-INF/jsp/test.jsp").forward(req,resp);
    }

    @RequestMapping("/testsapi/redirect")
    public void TestR(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 用原生 API 当然需要这两个参数了
        req.setAttribute("msg","重定向啦");
        // 重定向就访问不了被保护的 WEB-INF 目录了！！！
        // 由客户端发起二次请求，要带上项目路径！！！
        resp.sendRedirect(req.getServletContext().getContextPath()+"/index.jsp");
    }
}
