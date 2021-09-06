## SpringMVC注解

上节通过 HelloSpringMVC 程序研究了 SpringMVC 的执行过程，为了能看到执行流程，程序中的配置都是比较详细的；但 SpringMVC 强大的还是它的注解，用注解的方式，配置更加简单方便，这节就来学习 SpringMVC 的注解！

### 1. 注解开发

用注解的方式，重构一下上一节的项目，创建 Spring-03-Annotation 项目（ Maven 模板坑人）！

1. 导入相关依赖，在父项目中已经全导了（真不错）！

2. 配置 web.xml，注册 `DispatcherServlet`，和之前是一样的，重复步骤！

   ```xml
   <!--注册servlet-->
   <servlet>
       <servlet-name>SpringMVC</servlet-name>
       <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
       <!--绑定 SpringMVC 配置文件-->
       <init-param>
           <param-name>contextConfigLocation</param-name>
           <param-value>classpath:springmvc-servlet.xml</param-value>
       </init-param>
       <!-- 启动顺序，数字越小，启动越早 -->
       <load-on-startup>1</load-on-startup>
   </servlet>
   
   <!--所有请求都会被springmvc拦截 -->
   <servlet-mapping>
       <servlet-name>SpringMVC</servlet-name>
       <url-pattern>/</url-pattern>
   </servlet-mapping>
   ```

3. 创建对应的 Spring 配置文件 springmvc-servlet.xml，不一样的地方来了！

   **添加注解扫描**，扫描对应包下的注解

   ```xml
   <!-- 自动扫描包让注解生效，由 IoC 容器管理 -->
   <context:component-scan base-package="com.qiyuan.controller"/>
   ```

   **让 SpringMVC 不处理静态资源**，静态资源还处理啥啊

   ```xml
   <!-- 让Spring MVC不处理静态资源 -->
   <mvc:default-servlet-handler />
   ```

   **开启注解！**自动创建了使用注解需要的对象

   ```xml
   <!--
       开启注解！
       在 Spring 中采用 @RequestMapping 注解来完成映射关系
       要使 @RequestMapping 注解生效
       必须注册 DefaultAnnotationHandlerMapping
       和 AnnotationMethodHandlerAdapter 对象
       这两个实例分别在类级别和方法级别处理。
       annotation-driven 配置自动完成上述两个实例的注入！
   -->
   <mvc:annotation-driven />
   ```

   **添加视图解析器**，这个还是一样的

   ```xml
   <!-- 视图解析器 -->
   <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
         id="internalResourceViewResolver">
       <!-- 前缀 -->
       <property name="prefix" value="/WEB-INF/jsp/" />
       <!-- 后缀 -->
       <property name="suffix" value=".jsp" />
   </bean>
   ```

   可以发现，相比之前少了配置处理器映射和处理器适配器的部分！注解帮我们做了！

4. 创建控制器 Controller 处理业务

   这里和之前一样还是 HelloController，做一样的事；不过，**不用实现 Controller 接口了！直接添加 @Controller 注解！**

   ```java
   // 这样它就是一个控制器了！
   @Controller
   public class HelloController {
       
   }
   ```

   在其中**创建一个方法 Hello，并添加 @RequestMapping 注解！属性为请求名字！**

   ```java
   // 这样它就是一个控制器了！
   @Controller
   public class HelloController {
   
       // 访问地址 localhost:8080/项目路径/hello
       @RequestMapping("/hello")
       public String Hello(Model model){
           // 处理业务，结果放在 model 中！
           // 不过没返回，前面是怎么获取的呢？
           model.addAttribute("msg","Hello,SpringMVC");
   
           // 返回的视图名称！
           return "hello";
       }
   }
   ```

   **方法的返回值 String 字符串就是视图的名字，参数为 Model 模型，用于存放业务处理后的数据！**

   **提示**：@RequestMapping 注解也可以用在类上，相当于多加一级！

5. 控制器要有返回的视图，在 /WEB-INF/jsp 下创建 hello.jsp（路径由前缀后缀决定的），和之前一样（这里省略）！

6. 配置 Tomcat 后启动，输入请求，请求结果 Hello,SpringMVC（结果也略）！

   **遇到问题**：用 Maven 创建的 Web 项目模板，缺少文件夹、没标记就算了，连 Web 文件夹都缺，真不是楞！

   **解决方案**：Project Structure 中选择 Facets，为项目添加一个 Web Resources Directories！

### 2. 总结

可以对比发现，用注解多么简单！从头到尾过一遍流程：

1. 新建项目，导入依赖
2. 在 web.xml 中注册 `DispatcherServlet`
3. 编写 Spring 配置文件
4. 创建控制器类 Controller，处理业务返回视图
5. 前端也要有对应的视图，获取返回的 Model 数据
6. 运行测试！

使用 SpringMVC 的核心：**前端控制器**、**处理器映射器**、**处理器适配器**、**视图解析器**；使用注解后，注解帮我们搞定了处理器映射器和处理器适配器，就只有其他两个要写了，好日子来临啦😉！

