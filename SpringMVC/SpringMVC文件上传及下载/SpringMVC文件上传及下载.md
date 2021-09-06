## SpringMVC文件上传及下载

文件上传及下载是 Web 项目中经常使用的功能，SpringMVC 可以很好地支持文件上传及下载；本节新建 SpringMVC-09-File 项目学习在 SpringMVC 中进行文件上传及下载。

### 1. 文件上传

#### 1.1 前置知识

文件上传即从客户端向服务器传输数据，所以在客户端也需要有文件传输的支持！

前端页面的**表单中的 `enctype` 属性**就是用于设置传输格式的

- `application/x-www=form-urlencoded`：默认方式，只处理表单域中的 value 属性值，采用这种编码方式的表单会将表单域中的值处理成 URL 编码方式。
- `multipart/form-data`：这种编码方式会以二进制流的方式来处理表单数据，这种编码方式会把文件域指定文件的内容也封装到请求参数中，不会对字符编码（就是要用它了）。
- `text/plain`：除了把空格转换为 "+" 号外，其他字符都不做编码处理，这种方式可用于直接通过表单发送邮件。

```html
<form action="" enctype="multipart/form-data" method="post">
   <input type="file" name="file"/>
   <input type="submit">
</form>
```

设置 `enctype` 为 `multipart/form-data` 后，浏览器就会采用二进制流的方式来处理表单数据，实现文件的传输。

在后端，使用 Servlet 也可以做到文件上传的接收（之前跳过了😓），不过 SpringMVC 提供了更简单的封装，即使用 `MultipartResolver` 实现；SpringMVC 使用 Apache Commons FileUpload 技术实现了一个 `MultipartResolver` 实现类，即 `CommonsMultipartResolver`，所以使用 SpringMVC 进行文件上传还需要导入 Apache Commons FileUpload 的包。

```xml
    <!--文件上传-->
    <dependency>
       <groupId>commons-fileupload</groupId>
       <artifactId>commons-fileupload</artifactId>
       <version>1.3.3</version>
    </dependency>
```

了解完以上内容后，就可以尝试一下文件上传了！

#### 1.2 基本操作

新建 SpringMVC 项目后，依旧需要配置 Web、配置 SpringMVC、配置 Tomcat 等，此处也省略。

在 SpringMVC 的配置文件中**配置 `multipartResolver`的 bean**，使用 SpringMVC 的 `CommonsMultipartResolver` 实现！

```xml
    <!--文件上传配置-->
    <bean id="multipartResolver"  class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <!-- 请求的编码格式，必须和jSP的pageEncoding属性一致，以便正确读取表单的内容，默认为ISO-8859-1 -->
        <property name="defaultEncoding" value="utf-8"/>
        <!-- 上传文件大小上限，单位为字节（10485760=10M） -->
        <property name="maxUploadSize" value="10485760"/>
        <property name="maxInMemorySize" value="40960"/>
    </bean>
```

上传文件的**前端页面**，设置了 `enctype` 属性为 `multipart/form-data`

```html
<form action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data" method="post">
    <input type="file" name="file"/>
    <input type="submit" value="上传文件">
</form>
```

**处理上传文件请求的控制器方法**，获取文件输入流后用输出流输出到保存路径下，最基本的 IO 操作

```java
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
}
```

其中用到了 `CommonsMultipartFile` 即文件对象的两个方法

- **String getOriginalFilename()：获取上传文件的原名**
- **InputStream getInputStream()：获取文件流**

通过这两个方法，获取了文件的名字和数据，再用输出流保存到服务器中就完成了！测试上传文件，保存到了out 文件夹下对应项目的 upload 文件夹中！

#### 1.3 简化操作

上面虽然成功实现了文件上传，但代码中还有可以抽象的部分，如获取文件名、获取输入流、保存文件的操作对任何文件应该都是相同的；**所以 `CommonsMultipartFile` 提供了 `transferTo` 方法直接保存文件！**

在控制器中再添加一个处理文件上传的方法，对应请求 `/upload2`

```java
@Controller
public class FileController {
    ...

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
}
```

通过 `CommonsMultipartFile` 的 `transferTo ` 方法，省去了判断文件是否为空和进行 IO 操作的步骤，只要有文件名和保存路径，直接一步到位！

### 2. 文件下载

文件下载相比上传就简单多了，因为客户端只要像平时一样接收数据就行了，只不过接收的是二进制数据！

对应的前端页面，只要和平时一样发起一个请求就行了！

```html
<a href="${pageContext.request.contextPath}/download">下载文件</a>
```

在控制器中添加处理下载文件请求的方法

```java
@Controller
public class FileController {
    ...

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
```

其中可总结为六步操作

1. 设置要下载的文件的路径
2. 设置 response 响应头
3. 获取要下载的文件对象
4. 获取要下载文件的输入流
5. 获取向客户端传输的输出流（从 response 中获取）
6. 进行 IO 操作

运行测试，下载文件成功！简单到没什么好说的了！

### 3. 总结

本节其实没什么好总结的，都是一些常用的操作。要总结的是 SpringMVC 的学习到此就告一段落了，在出发前结束不免有些感慨。一时间也不知道要说什么了，只能希望以后越来越好吧😭！

> ***我们就这样，生活在此地并不断离别。***
>
> ***so leben wir und nehmen immer Abschied.***

