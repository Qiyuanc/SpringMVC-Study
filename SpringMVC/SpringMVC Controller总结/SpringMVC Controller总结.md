## SpringMVC Controller总结

经过前面的学习，对 SpringMVC 已经有一定的了解了，其中执行的流程也算略知一二。在 SpringMVC 中，控制器 Controller 是实际执行业务的部分，如何让控制器生效就是 SpringMVC 的目标。本节用 SpringMVC-04-Controller 项目总结一下 Controller 的使用！

### 1. 控制器Controller

- 控制器复杂提供访问应用程序的行为，可以通过**接口定义**或**注解定义**两种方法实现。
- 控制器负责解析用户的请求并将其转换为一个模型。
- 在 Spring MVC 中一个控制器类可以包含多个方法（使用注解）。
- 在 Spring MVC 中，Controller 有多种配置方式。

### 2. 实现Controller接口

Controller 是一个接口，在 `org.springframework.web.servlet.mvc `包下，接口中只有一个方法 `handleRequest`

```java
@FunctionalInterface
public interface Controller {
    @Nullable
    ModelAndView handleRequest(HttpServletRequest var1, HttpServletResponse var2) throws Exception;
}
```

某个类实现 Controller 接口中的 `handleRequest` 方法后，这个类就是一个控制器了，可以处理传递到该控制器中的请求（用  `handleRequest` 方法）！

配置 web.xml，注册 `DispatcherServlet`；配置 springmvc-servlet.xml，添加视图解析器（处理器注册器和处理器适配器都有默认配置的！）

```xml
<!--处理器映射器-->
<!--<bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>-->
<!--处理器适配器-->
<!--<bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>-->
<!-- 视图解析器 -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
      id="internalResourceViewResolver">
    <!-- 前缀 -->
    <property name="prefix" value="/WEB-INF/jsp/" />
    <!-- 后缀 -->
    <property name="suffix" value=".jsp" />
</bean>
```

**配置完成的情况下，想要增加一个控制器，只要创建一个控制器类实现 Controller 接口即可！**

创建 ControllerTest1 类，保存模型数据并设置视图名字

```java
public class ControllerTest1 implements Controller {

    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        //返回一个模型视图对象
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg","Test1Controller");
        mv.setViewName("test");
        return mv;
    }
}
```

由于用接口定义的方式实现，所以要把这个类注册到 Spring 中（这个 id 是要加斜杠的，找了好久的 BUG😓）

```xml
<bean id="/test1" class="com.qiyuan.controller.ControllerTest1"/>
```

对应的视图页面 /WEB-INF/jsp/test.jsp，只是获取一下模型的数据

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Qiyuan测试！</title>
</head>
<body>
${msg}
</body>
</html>
```

配置 Tomcat，导入 lib，运行测试，输出 Test1Controller，测试成功！

**重大挫折**：这 Maven 项目删东西怎么删不干净啊！！！最后只能把项目名变成  SpringMVC-04-Contr0ller。 

通过实现接口 Controller 定义控制器属于老方法了，除了麻烦之外，还有一个明显缺点：一个控制器中只有一个方法，如果要多个方法则需要定义多个 Controller。所以还是得用注解！

### 3. 使用注解@Controller

使用注解的方式就非常简单了，在上面配置的基础上开启注解支持

```xml
<!-- 自动扫描包，让指定包下的注解生效,由IOC容器统一管理 -->
<context:component-scan base-package="com.qiyuan.controller"/>
<!-- 让Spring MVC不处理静态资源 -->
<mvc:default-servlet-handler />
<mvc:annotation-driven />
```

**开启注解后，直接创建 ControllerTest2 类，通过 @Controller 注解定义就可以使其成为一个控制器**

添加 @Controller 注解后，若方法的返回值是 String，就会被当成视图处理，被视图解析器拿去寻找视图；而 @RequestMapping 则设置了访问控制器的请求路径！

```java
@Controller
public class ControllerTest2 {
    @RequestMapping("/test2")
    public String Test2(Model model){
        model.addAttribute("msg","Test2Controller");

        return "test";
    }
}
```

运行测试，输出 Test1Controller，测试成功！

**小结**：可以发现，两个控制器指向了同一个视图 test.jsp，输出了不同的内容！说明**视图是可以复用的**，只要有一个大致的框架，向里面输出不同的内容就行了，**控制器和视图之间是弱耦合的关系**！

**问题**：用注解的话，怎么获取 `req` 和 `resp` 参数呢？

### 4. @RequestMapping

细说 @RequestMapping：**@RequestMapping 注解用于映射 url 到控制器类或某个特定的方法，可用于类或方法上。**用于类上，表示类上的路径是方法上的路径的上一级！

再创建一个控制器 ControllerTest3，为这个类也添加请求路径

```java
@Controller
@RequestMapping("/qiyuan")
public class ControllerTest3 {
    @RequestMapping("/test3")
    public String Test3(Model model){
        model.addAttribute("msg","Test3Controller");

        return "test";
    }
}
```

运行测试，通过 `http://localhost:8080/SpringMVC_04_Contr0ller/qiyuan/test3` 发起请求，输出 Test3Controller，测试成功！

### 5. 总结

两种方法都需要在 web.xml 中注册 `DispatcherServlet`，因为它是中央控制器！同时还要给它一个配置文件 springmvc-servlet.xml（叫啥都行）！

实现接口，就要配置相应的 bean；开启注解，只需要在对应的类中配置就行了！

又回顾了一下创建控制器的两种方式，我们注解真太方便啦😀！
