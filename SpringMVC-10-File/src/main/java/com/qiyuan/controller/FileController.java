package com.qiyuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @ClassName FileController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/6 21:24
 * @Version 1.0
 **/
@Controller
public class FileController {

    @RequestMapping("/upload")
    // @RequestParam("file") 将 name=file 控件得到的文件封装成 CommonsMultipartFile 对象
    // 批量上传文件时 CommonsMultipartFile 为数组即可
    public String fileUpload(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {

        // 获取文件名 file.getOriginalFilename()
        String uploadFileName = file.getOriginalFilename();

        // 如果文件名为空，直接回到首页！
        if ("".equals(uploadFileName)) {
            return "redirect:/index.jsp";
        }
        System.out.println("上传文件名: " + uploadFileName);

        // 获取当前项目路径，设置上传文件保存路径
        String path = request.getServletContext().getRealPath("/upload");
        System.out.println("path: " + path);
        // 如果保存路径不存在，创建一个
        File realPath = new File(path);
        if (!realPath.exists()) {
            realPath.mkdir();
        }
        System.out.println("上传文件保存地址：" + realPath);

        // 文件输入流
        InputStream is = file.getInputStream();
        // 文件输出流
        OutputStream os = new FileOutputStream(new File(realPath, uploadFileName));

        // IO操作
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
            os.flush();
        }
        os.close();
        is.close();
        return "redirect:/index.jsp";
    }

    // 使用 transferTo 方法，更简单
    @RequestMapping("/upload2")
    public String fileUpload2(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {

        // 获取当前项目路径，设置上传文件保存路径
        String path = request.getServletContext().getRealPath("/upload");
        File realPath = new File(path);
        if (!realPath.exists()) {
            realPath.mkdir();
        }
        System.out.println("上传文件保存地址：" + realPath);

        // 使用 CommonsMultipartFile 的 transferTo 方法直接写文件！
        file.transferTo(new File(realPath + "/" + file.getOriginalFilename()));

        return "redirect:/index.jsp";
    }

    @RequestMapping(value = "/download")
    public String downloads(HttpServletResponse response, HttpServletRequest request) throws Exception {
        // 1. 设置要下载的文件的路径
        String path = request.getServletContext().getRealPath("/upload");
        String fileName = "Irror.jpg";

        // 2. 设置 response 响应头
        // 设置页面不缓存，清空buffer
        response.reset();
        // 设置字符编码
        response.setCharacterEncoding("UTF-8");
        // 设置二进制传输数据
        response.setContentType("multipart/form-data");
        // 设置响应头
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));

        // 3. 获取要下载的文件对象
        File file = new File(path, fileName);
        // 4. 获取要下载文件的输入流
        InputStream is = new FileInputStream(file);
        // 5. 获取向客户端传输的输出流（从 response 中获取）
        OutputStream os = response.getOutputStream();

        // 6. 进行 IO 操作
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            os.write(buff, 0, len);
            os.flush();
        }
        os.close();
        is.close();
        return "redirect:/index.jsp";
    }
}
