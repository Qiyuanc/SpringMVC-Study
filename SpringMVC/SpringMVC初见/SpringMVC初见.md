## SpringMVC初见

现在就正式开始学习 SpringMVC 了！

### 1. SpringMVC简介

SpringMVC 是 Spring Framework 的一部分，是基于 Java 实现 MVC 的轻量级 Web 框架，**本质上相当于 Servlet！**

SpringMVC 的优点

- 轻量级框架，简洁灵活
- 基于请求响应的 MVC 框架，高效
- 与 Spring 无缝衔接！

也没啥好介绍的了，SpringMVC 和 Spring 已经是绑定的了，学习的理由自不必说，直接开始行动！

### 2. HelloSpringMVC

在研究 SpringMVC 执行的流程和原理之前，先对着教程写一个 HelloSpringMVC 程序，感受一下流程（学习流程，开发还会简化）！

1. 创建 SpringMVC-02-Hello 项目，为项目添加 Web 的框架，配置 Tomcat（创建 Artifact 时把前一个项目的包给删了，后面也这么干）。

2. 配置 web.xml，注册 **`DispatcherServlet`** （ SpringMVC 的核心！）

   ```xml
   <!-- web.xml 中-->
   
   <!--注册 DispatcherServlet -->
   <servlet>
       <servlet-name>springmvc</servlet-name>
       <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
       <!--关联 springmvc 的配置文件:[servlet-name]-servlet.xml-->
       <init-param>
           <param-name>contextConfigLocation</param-name>
           <param-value>classpath:springmvc-servlet.xml</param-value>
       </init-param>
       <!--启动级别-1-->
       <load-on-startup>1</load-on-startup>
   </servlet>
   
   <!--/ 匹配所有的请求（不包括.jsp）-->
   <!--/* 匹配所有的请求（包括.jsp）-->
   <servlet-mapping>
       <servlet-name>springmvc</servlet-name>
       <url-pattern>/</url-pattern>
   </servlet-mapping>
   ```

   其中有一个 SpringMVC 的配置文件需要关联。

3. 在 resources 目录下创建 SpringMVC 的配置文件，名称格式为 `[servletname]-servlet.xml`（官方建议）

   ```xml
   <!--约束就是标准的 Spring 约束！-->
   
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd">
   
   </beans>
   ```

4. 在配置文件中，添加**处理器映射**

   ```xml
   <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
   ```

   这没有 id 的 bean 一看就知道要自动装配了！

5. 在配置文件中，添加**处理适配器**

   ```xml
   <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>
   ```

   这个 bean 也没有 id。

6. 在配置文件中，添加**视图解析器**

   ```xml
   <!--视图解析器 给 DispatcherServlet 的 ModelAndView 都会经过这里 -->
   <!-- 1.获取 ModelAndView 的数据-->
   <!-- 2.解析 View 的名字-->
   <!-- 3.拼接视图名字，找到对应的视图-->
   <!-- 4.把 Model 渲染到 View 上！-->
   <bean  id="InternalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
       <!--前缀-->
       <property name="prefix" value="/WEB-INF/jsp/"/>
       <!--后缀-->
       <property name="suffix" value=".jsp"/>
   </bean>
   ```

   也不知道是干什么用的，后面再说。不过可以知道**前缀和后缀用于和请求进行拼接**，如请求为 hello，就能拼接成 `/WEB-INF/jsp/hello.jsp`。

7. 在 com.qiyuan.controller 包下编写**操作业务的 HelloController 类**，实现了 Controller 接口，它就是一个 Controller（或者注解！）

   ```java
   // 这里先使用 Controller 接口实现
   public class HelloController implements Controller {
   
       public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
           // ModelAndView 模型和视图
           ModelAndView mv = new ModelAndView();
   
           // 封装对象，放在ModelAndView中。Model
           mv.addObject("msg","HelloSpringMVC!");
           // 封装要跳转的视图，放在ModelAndView中
           // 由于设置了前缀和后缀，相当于 /WEB-INF/jsp/hello.jsp
           mv.setViewName("hello");
           return mv;
       }
   }
   ```

8. 将 Controller 类交给 Spring 管理，注册对应的 bean

   ```xml
   <!--Handler-->
   <bean id="/hello" class="com.qiyuan.controller.HelloController"/>
   ```

   这里也是写在 springmvc-servlet.xml 中的。

9. 好了，差不多了，把要跳转的页面 hello.jsp 写了

   ```jsp
   <%-- /WEB-INF/jsp/hello.jsp --%>
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>Qiyuanc</title>
   </head>
   <body>
   ${msg}
   </body>
   </html>
   ```

10. 启动项目，请求 `http://localhost:8080/SpringMVC_02_Hello/hello`，成功输出 `HelloSpringMVC!`！

**注意**：这里可能会出现请求的页面找不到（404）的情况，**原因为项目中的 jar 包在 Tomcat 中找不到！**

**解决方法**：可以把项目的 jar 包全丢进 Tomcat 的 lib 下，但这太暴力了😓。所以用 IDEA 添加进去：**Project Structure --> Artifacts --> 对应项目的 Available Elements --> 选中所有的 jar 包 --> 右键 Put into /WEB-INF/lib**，完美解决！

**小结**：这里已经可以看出和普通 Web 项目的区别了。按照普通 Web 项目的做法，想用 hello 请求访问某个页面，是要在 web.xml 中做好 servlet 和 mapping 的对应的。但在上面，web.xml 中只有一个 servlet，即 **`DispatcherServlet`**，也就是说，它帮我们做了对应的工作。

此时，如果想新增请求 /hello2 访问某个控制器（ Servlet ）进行业务处理，只需要写一个对应的 Controller 类，在其中进行业务处理，最后在 Spring 中注册它的 bean 就行了！

**这个 `DispatcherServlet` 就是 SpringMVC 的核心。**

### 3. SpringMVC执行过程

#### 3.1 **简单的执行过程**

一图流解释 SpringMVC 的执行过程🧐

<img src="F:\TyporaMD\SpringMVC\SpringMVC初见\image-20210829230627900.png" alt="image-20210829230627900" style="zoom:80%;" />

1. 前端控制器 `DispatcherServlet` 决定了用户的请求交给哪个控制器处理（1、2）
2. 被委托的控制器调用模型（业务层）进行业务处理，获取处理的数据（3、4）
3. 将处理的数据和要返回的视图，放在 `ModelAndView` 中交给前端控制器（5）
4. 前端控制器用 Model 渲染 View，然后将视图响应给用户（6、7、8，存疑）

#### 3.2 详细的执行过程

SpringMVC 比较完整的执行流程图，具体到不同对象的调用

<img src="F:\TyporaMD\SpringMVC\SpringMVC初见\image-20210830105220369.png" alt="image-20210830105220369" style="zoom:67%;" />

**其中，实线部分是 SpringMVC 的配置已经完成了的，只有虚线部分是需要自己写的（业务、跳转视图什么的肯定是自己写）！**

**SpringMVC 内部的执行流程**（实线部分，先说具体配置了的，还没见到的就不写了）

1. **用户发出请求，`DispatcherServlet` 接收请求并拦截请求**

    `DispatcherServlet` 即前端控制器，是 SpringMVC 的控制中心，本质上是一个 Servlet，所以需要注册，同时要绑定 Spring 的配置文件以获取需要的对象

   ```xml
   <!--注册 DispatcherServlet -->
   <servlet>
       <servlet-name>springmvc</servlet-name>
       <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
       <!--关联 springmvc 的配置文件:[servlet-name]-servlet.xml-->
       <init-param>
           <param-name>contextConfigLocation</param-name>
           <param-value>classpath:springmvc-servlet.xml</param-value>
       </init-param>
       <!--启动级别-1-->
       <load-on-startup>1</load-on-startup>
   </servlet>
   <!--/ 匹配所有的请求（不包括.jsp）-->
   <!--/* 匹配所有的请求（包括.jsp）-->
   <servlet-mapping>
       <servlet-name>springmvc</servlet-name>
       <url-pattern>/</url-pattern>
   </servlet-mapping>
   ```

2. **`DispatcherServlet` 调用处理器映射 `HandlerMapping`，找到与请求对应的控制器**

   上面用的是 `BeanNameUrlHandlerMapping`，所以需要配置 bean，id 为请求，class 为与其对应的控制器；也可以使用注解，配置方式就不一样了

   ```xml
   <!-- springmvc-servlet.xml -->
   <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
   ...
   <bean id="/hello" class="com.qiyuan.controller.HelloController"/>
   ```

   处理器映射的类有很多，这种方式比较容易理解执行的过程！

3. **`DispatcherServlet` 获取到请求对应的控制器后，调用对应的处理器适配器 `HandlerAdapter`** 

   **调用 `HandlerAdapter` 处理的大致流程**

   启动时，`DispatcherServlte` 会根据配置文件信息注册 `HandlerAdapter`，若配置文件中没有，则按默认配置注册（默认会注册三个）

   ```xml
   <!-- springmvc-servlet.xml -->
   <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>
   ```

   **`DispatcherServlte` 会根据 `HandlerMapping` 传过来的 `Controller` 与注册的 `HandlerAdapter` 进行匹配，找到支持该 `Controller` 类型的 `HandlerAdapter`，然后该 `HandlerAdapter` 就调用自己的 `handle` 方法，利用反射机制执行 `Controller` 的具体方法，获取 `ModelAndView`。**

   如上面的 `SimpleControllerHandlerAdapter` 就支持实现了 `Controller` 接口的控制器；因为 `HelloController` 实现了 `Controller` 接口，所以适配器可以执行其中的 `handleRequest` 方法，进行业务操作！

4. **`DispatcherServlte` 获取到控制器执行完返回的 `ModelAndView` 后，调用视图解析器 `ViewResolver` 解析视图名字**

   在控制器中，用 `setViewName` 方法设置视图名字，在这里解析成为实际存在的视图（ `hello` --> `/WEB-INF/jsp/hello.jsp` ）

   ```xml
   <!--视图解析器--> 
   <!-- 给 DispatcherServlet 的 ModelAndView 都会经过这里 -->
   <!-- 1.获取 ModelAndView 的数据-->
   <!-- 2.解析 View 的名字-->
   <!-- 3.拼接视图名字，找到对应的视图-->
   <!-- 4.把 Model 渲染到 View 上！-->
   <bean  id="InternalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
       <!--前缀-->
       <property name="prefix" value="/WEB-INF/jsp/"/>
       <!--后缀-->
       <property name="suffix" value=".jsp"/>
   </bean>
   ```

   视图解析器也不只这一种，还有模板引擎如 Themleaf 和 Freemarker。

5. **`DispatcherServlet`  根据视图解析器解析的视图结果，调用具体的视图响应给用户**

简单的理解执行过程大致就是这样，总结其中几处关键点：

- `DispatcherServlte` ：核心，运筹帷幄
- `HandlerMapping`：查找与请求对应的控制器
- `HandlerAdapter`：利用反射调用 `Controller` 中的方法
- `Controller`：进行业务操作，返回 `ModelAndView`，其中有模型数据和要返回的视图
- `ViewResolver`：解析要返回哪个视图

具体的理解还是在后面慢慢进行吧！

### 4. 总结

本节主要是要理解 SpringMVC 大致的执行过程，一些细节就先不研究了。而且实际应用中也不会这么用，要使用更加简便的注解，不过对执行过程要有把握🤔！
