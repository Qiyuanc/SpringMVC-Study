## SpringMVC乱码问题

乱码问题一直是一个经常出现又烦人的问题，本节继续用 Spring-05-REST 项目（内容不多没必要新建）研究和复习一下乱码问题及其解决方法！

### 1. 发现问题

首先编写前端页面 index.jsp，通过 POST 方式提交一个 name（ 捏吗的这个 jsp 的路径问题真烦人）

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>提交名字</title>
  </head>
  <body>
    <form action="${pageContext.request.contextPath}/encoding" method="post">
      <input type="text" name="name">
      <input type="submit">
    </form>
  </body>
</html>
```

然后写一个控制器处理请求，直接返回提交的 name

```java
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
```

通过视图解析器找到 .../WEB-INF/jsp/test.jsp，输出 msg 信息即名字（省略了，和之前一样的）。

最后运行测试一下，提交 name 为 `祈鸢`，返回页面结果为 `ç¥é¸¢`，**乱码了**；通过在控制器中输出 name 客可知，**name 并不是返回后才乱码的，而是在到达控制器时就乱码了。**

### 2. Filter过滤器

在之前的 Servlet 阶段，使用了过滤器来解决乱码问题，在 SpringMVC 中当然也可以！

编写一个过滤器类 EncodingFilter，实现 Filter 接口，它就是一个过滤器了；实现过滤器方法

```java
public class EncodingFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 执行过滤行为
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        // 过滤完成，放行
        filterChain.doFilter(servletRequest,servletResponse);
    }

    public void destroy() {

    }
}
```

非常简单，设置完编码放行即可，个中道理不懂的回去复习 Filter！

在 web.xml 中配置过滤器，设置过滤的请求

```xml
<filter>
    <filter-name>Encoding</filter-name>
    <filter-class>com.qiyuan.filter.EncodingFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>Encoding</filter-name>
    <!-- /* 包括 JSP ！ / 不包括 ！-->
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

注意 url-pattern 为 `/*`，过滤所有请求（包括 jsp ）；如果设置为 `/`，访问 jsp 就不会经过过滤器了！

运行测试，提交 name 为 `祈鸢`，返回页面结果为 `祈鸢`，成功啦！

### 3. SpringMVC过滤器

上面直接使用自定义的过滤器当然只是为了复习一下，都使用框架了这种事情肯定让框架干了！

直接在 web.xml 中配置 SpringMVC 提供的过滤器（把上面的注释掉了）！

```xml
<filter>
    <filter-name>EncodingMVC</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>utf-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>EncodingMVC</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

当然也没有问题啦，就是 SpringMVC 帮我们写好了过滤器，直接用就行了！

### 4. 总结

这节也没什么实质性内容···乱码问题，就用过滤器解决，SpringMVC 就提供了这种过滤器！

这节就当复习过滤器了🤒。