## SpringMVC学前回顾

学完了 MyBatis 和 Spring，就到 SSM 框架的最后一部分——SpringMVC 了！和之前一样，新建 SpringMVC-Study 父项目来练练手！

### 1. 回顾MVC框架

#### 1.1 什么是MVC

**MVC 即模型( Model )、视图( View )、控制器( Controller )的简写**，是一种软件设计规范！

MVC 不是一种设计模式，**而是一种架构模式**，主要作用是**降低了视图与业务逻辑间的双向耦合**。

- **Model（模型）：**数据模型，提供要展示的数据，因此包含**数据**和**行为**。一般分为数据层（ Dao 层） 和 服务层（ Service 层）。总而言之，模型负责数据查询和数据的状态更新等功能。
- **View（视图）：**负责进行模型的展示，也是用户与之进行交互的界面，即前端页面。
- **Controller（控制器）：**接收用户请求，委托给模型进行处理（状态改变），处理完毕后把返回的模型数据交给视图，由视图负责展示。即控制器的任务就是在视图展示和模型处理之间进行调度！

**最典型的 MVC 框架即 JSP + Servlet + JavaBean 模式**

<img src="F:\TyporaMD\SpringMVC\SpringMVC学前回顾\image-20210829112928680.png" alt="image-20210829112928680" style="zoom: 80%;" />

#### 1.2 Model1方式

早期的 Web 开发，采用的都是 Model1方式。

Model1 主要分为两层：模型层和视图层。

<img src="F:\TyporaMD\SpringMVC\SpringMVC学前回顾\image-20210829113511008.png" alt="image-20210829113511008" style="zoom:80%;" />

其中 JSP 本质上就是 Servlet，既负责渲染页面，也负责了调度模型和视图之间的数据。

这种方式的**优点是架构简单**，适合小项目，写个什么管理系统作业用这种方式完全可以做到；**缺点是 JSP 职责过重，不利于维护**，大项目要是这么干维护起来就是究极折磨！

#### 1.3 Model2方式

Model2 就出现了 MVC 了，将一个项目分成三部分：**控制、模型、视图**。

<img src="F:\TyporaMD\SpringMVC\SpringMVC学前回顾\image-20210829114330071.png" alt="image-20210829114330071" style="zoom:80%;" />

**职责分析**：

**控制：Controller**

1. 获取表单请求参数
2. 调用业务逻辑处理
3. 转向指定页面

**模型：Model**

1. 处理业务逻辑
2. 保存数据状态（数据库操作）

**视图：View**

1. 展示页面！

可以看出，Model2 方式使得框架各部分的任务更加明显。Model1 中 JSP 同时承担了 View 和 Controller 的任务，将控制逻辑和表现逻辑混在一起，耦合度非常高，增加了应用的扩展性和维护的难度。而 Model2 通过分离 View 和 Controller 角色，解决了 Model1 的问题。

### 2. 回顾Servlet

在使用 SpringMVC 前，先回顾一下基础，Servlet 和 Tomcat 的使用！**先给父项目 SpringMVC-Study 导入依赖**

```xml
<dependencies>
        <!-- Spring 框架！包含所有 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.3.9</version>
        </dependency>
        <!-- servlet -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>
        <!-- jsp -->
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>javax.servlet.jsp-api</artifactId>
            <version>2.3.3</version>
            <scope>provided</scope>
        </dependency>
        <!--jstl表达式 -->
        <dependency>
            <groupId>javax.servlet.jsp.jstl</groupId>
            <artifactId>jstl-api</artifactId>
            <version>1.2</version>
        </dependency>
        <!-- junit -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.7.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

然后在父项目中创建 SpringMVC-01-Servlet 项目（不使用模板），再用 Add Framework Support 给项目添加 Web 的框架！这样这个项目就有 web 文件夹了，代表这是一个 Web 项目！

好了，现在就可以写一个简单的 Servlet 程序了（详细回顾，一步步来）！

#### 2.1 编写 Servlet 类

创建 HelloServlet 类继承 HttpServlet ，它就是一个 Servlet 了，可以处理用户的请求！

```java
public class HelloServlet extends HttpServlet {

}
```

然后就是重写其中的 doGet 和 doPost 方法（ Ctrl + o 快速重写方法）！

```java
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 处理请求的代码！
        // 1.获取前端的参数
        String method = req.getParameter("method");
        if (method.equals("add")){
            req.getSession().setAttribute("msg","执行了add方法");
        }
        if (method.equals("delete")){
            req.getSession().setAttribute("msg","执行了delete方法");
        }
        // 2.调用业务层
        // 目前没有业务！

        // 3.视图转发或重定向！这里用转发，两者的区别回去 JavaWeb 看！
        req.getRequestDispatcher("/jsp/Test.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

这里转发到了一个 JSP 页面去，所以要有对应的页面！

#### 2.2 编写 JSP 页面

为了真实一点，不要直接输入参数去访问 Servlet，改造一下 index.jsp 让它能提交请求！

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>提交请求</title>
  </head>
  <body>
  <form action="${pageContext.request.contextPath}/hello" method="post">
    <input type="text" name="method">
    <input type="submit">
  </form>
  </body>
</html>
```

**注意**：时空穿越回来了！后面把项目路径设置为了 `/SpringMVC_01_Servlet`，所以这里要**通过 `${pageContext.request.contextPath}` 获取项目路径**，再加上请求的路径。否则就直接进到 `http://localhost:8080/hello` 了。

然后在 web 目录下创建 jsp 文件夹，在其中创建 Test.jsp，就是 Servlet 处理完请求展示的页面！

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Qiyuanc!</title>
</head>
<body>
<%--取参数！--%>
${msg}
</body>
</html>
```

很简单，就取了一下参数！

**注意**：如果把 jsp 文件夹创建在了 WEB-INF 目录下，肯定是有区别的，不过我还不知道😛！

#### 2.3 注册 Servlet

很关键的步骤嗷，不能忘记！

**通过在 web.xml 中进行注册，让输入的地址能够找到正确的 Servlet 去处理请求！**

```xml
<servlet>
    <servlet-name>hello</servlet-name>
    <servlet-class>com.qiyuan.servlet.HelloServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>hello</servlet-name>
    <url-pattern>/hello</url-pattern>
</servlet-mapping>

<!-- Session 持续时间 -->
<session-config>
    <session-timeout>15</session-timeout>
</session-config>

<!-- 欢迎页，就是首页！-->
<welcome-file-list>
    <welcome-file>index.jsp</welcome-file>
</welcome-file-list>
```

在 web.xml 中还能配置很多东西哦，如 Session 的持续时间和欢迎页面（默认 index.jsp ）！

#### 2.4 配置Tomcat

用了那么多次，总得会配了。不会回去看 JavaWeb！

 不过要强调一下，添加 Artifact 时的 Application context 就是项目的路径（怎么和 Spring 的配置文件一个名），直接用项目名就好，如 `/SpringMVC_01_Servlet`！不写的话，路径就是 `localhost:8080` 了（直接进去就是）。

配置完后启动，直接就会进入 index.jsp，输入 add 或 delete 后提交，就会显示执行了对应的方法了！这么简单就不贴图了（其实有 BUG ）。

### 3. 总结

回顾完了，再看看 MVC 框架要做什么事情

1. 将 url 映射到 java 类或类中的方法
2. 封装用户提交的数据
3. 处理请求——调用相关的业务进行处理——封装响应数据
4. 将响应的数据渲染到前端页面

再小提一下，除了 MVC 框架，还有 MVVM 框架，即 M：Model、V：View、VM：ViewModel（双向绑定！），不过这就是 Vue 的东西了！到后面的学习中再去理解吧😀！

