## SpringMVC拦截器

本节新建 SpringMVC-09-Interceptor 项目学习 SpringMVC 的拦截器。

### 1. 拦截器简介

SpringMVC 中的拦截器（ Interceptor ）类似于 Servlet 中的过滤器（ Filter ），主要用于拦截用户请求并进行相应的处理。例如通过拦截器可以进行权限验证、记录请求信息的日志、判断用户是否登录等。

过滤器与拦截器的区别：**拦截器是 AOP 思想的具体应用。**

**过滤器**

- Servlet 规范中的一部分，任何 JavaWeb 工程都可以使用
- 在 url-pattern 中配置了 `/*` 之后，可以对所有要访问的资源进行拦截

**拦截器** 

- 拦截器是 SpringMVC 框架的内容，只有使用了 SpringMVC 框架的工程才能使用
- 拦截器只会拦截访问的控制器方法， 如果访问的是 jsp/html/css/image/js 则不会进行拦截（自带静态资源过滤）

### 2. 自定义拦截器

创建完项目后的配置 Web、配置 SpringMVC、配置 Tomcat 等步骤省略！配置完后在控制器中写一个简单的请求方法测试项目能正常运行即可！

在 com.qiyuan.interceptor 包下创建 `MyInterceptor` 类，实现 `HandleInterceptor` 接口，实现了这个接口的类，就是一个拦截器（和 Servlet、Filter 类似）

```java
package com.qiyuan.interceptor;
public class MyInterceptor implements HandlerInterceptor {
    
}
```

不过与过滤器 Filter 接口强制要求实现其中的方法不同，`HandleInterceptor` 接口在方法没被实现前也不会报错；不过不实现其中的方法怎么能叫拦截器

```java
public class MyInterceptor implements HandlerInterceptor {
    // 在处理请求的方法执行前 执行 preHandle
    // 返回 true 放行
    // 返回 false 拦截！
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle:处理请求前");
        return true;
    }

    // 在处理请求的方法执行后 执行 postHandle
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle:处理请求后");
    }

    // 在 DispatcherServlet 处理后执行 afterCompletion 进行清理工作.
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion:清理——！");
    }
}
```

其中，`preHandle` 为处理请求前执行的方法，可以决定是否放行请求；`postHandle` 为处理请求后执行的方法；`afterCompletion` 为请求流程结束后执行的方法，进行清理工作。

写完拦截器类后，要在 SpringMVC 的配置文件中配置拦截器，设置使用哪个拦截器，拦截什么路径的请求

```xml
    <mvc:interceptors>
        <mvc:interceptor>
            <!-- 拦截的路径 -->
            <!-- /* 只包括下一级，如 /test/* 会拦截 /test/t1，但不会拦截 /test/t/t2 -->
            <!-- /** 包括路径下所有！ -->
            <mvc:mapping path="/**"/>
            <!-- 使用哪个拦截器 -->
            <bean class="com.qiyuan.interceptor.MyInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>
```

配置完拦截器后，运行测试一下，发起请求 `/test`

```java
@RestController
public class InterceptorController {

    @RequestMapping("/test")
    public String Test(){
        System.out.println("Controller: 处理请求");
        return "项目运行";
    }
}
// 控制台输出
/*
    preHandle: 处理请求前
    Controller: 处理请求
    postHandle: 处理请求后
    afterCompletion: 清理——！
*/
```

通过控制台输出就可以很清楚的看到拦截器中三个方法的执行时间点了！

拦截器中的方法执行在处理请求的方法执行前、执行后、执行结束。拦截器正是运用了 AOP，把其中的方法横切到了切入点（处理请求的方法）中！

### 3. 拦截器登录验证

现在尝试使用拦截器实现登录验证的功能！流程为

1. 用户在首页可以选择进行登录或进入成功页面
2. 用户进行登录后，服务器将用户的信息记录在 session 中，即登录成功，跳转到成功页面
3. 用户若未登录，进入成功页面应该被拦截器拦截

首先是首页 index.jsp，给出进入登录页面或成功页面的请求

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>首页</title>
</head>
<body>
<h1>首页</h1>
<hr>
<%--登录--%>
<a href="${pageContext.request.contextPath}/user/tologin">登录</a>
<a href="${pageContext.request.contextPath}/user/tosuccess">成功页面</a>
</body>
</html>
```

登录页面 login.jsp，输入用户名和密码后发起 `/login` 请求提交到服务器

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录页面</title>
</head>

<h1>登录页面</h1>
<hr>

<body>
<form action="${pageContext.request.contextPath}/user/login">
    用户名：<input type="text" name="username"> <br>
    密码：<input type="password" name="pwd"> <br>
    <input type="submit" value="提交">
</form>
</body>
</html>
```

登录成功页面 success.jsp，显示当前登录的用户信息，且可以注销当前登录信息

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>成功页面</title>
</head>

<h1>成功页面</h1>
<hr>
    
<body>
当前登录用户为：${user}
<a href="${pageContext.request.contextPath}/user/logout">注销</a>
</body>
</html>
```

编写控制器 UserController 处理上面的请求

```java
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
```

此时前端后端已经连接起来，可以运行了；不过未登录时，也能进入成功页面，当前用户显示为空，这就需要拦截器去拦截了！、

编写拦截器 UserInterceptor 拦截未登录时的请求

```java
public class LoginInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getRequestURI());
        if(request.getRequestURI().contains("login")){
            // 如果请求包含 login ，放行！
            return true;
        }
        // 获取当前会话 session
        HttpSession session = request.getSession();
        if(session.getAttribute("user")!=null){
            // session 中存在登录信息，放行！
            return true;
        }
        // 用户为登录访问其他页面，直接赶到登录页面去！
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request,response);
        // 拦截！！！
        return false;
    }
}

```

写完拦截器要记得在 SpringMVC 配置文件中配置，否则去哪找它！

```xml
    <mvc:interceptors>
        <mvc:interceptor>
            <!-- 拦截的路径 -->
            <!-- /* 只包括下一级，如 /test/* 会拦截 /test/t1，但不会拦截 /test/t/t2 -->
            <!-- /** 包括路径下所有！ -->
            <mvc:mapping path="/**"/>
            <!-- 使用哪个拦截器 -->
            <bean class="com.qiyuan.interceptor.LoginInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>
```

此时一个有模有样的登录验证就完成了！没登录的情况下再进入成功页面，会直接被转发到登录页面！

### 4. 总结

本节主要是了解了 SpringMVC 的拦截器的作用和用法！作用类似于 Servlet 的过滤器，可以在处理请求前后进行处理（实现方式为 AOP 思想）！用法为自定义的拦截器实现 SpringMVC 拦截器的接口，实现其中的方法，再到 SpringMVC 配置文件中配置即可！

没什么好说的了，通俗易懂🧐！