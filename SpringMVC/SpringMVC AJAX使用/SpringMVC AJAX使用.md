## SpringMVC AJAX使用

之前在分析 SMBMS 项目的时候稍微了解过其中的 AJAX 的作用和用法，现在学习了 SpringMVC，就要在 SpringMVC 中使用一下 AJAX。本节创建 SpringMCV-08-AJAX 项目学习 AJAX 的使用。

### 1. 回顾AJAX

**AJAX（ Asynchronous Javascript And XML，即异步 JavaScript 和 XML ）是一种在无需重新加载整个网页的情况下，能够更新部分网页的技术。**

**AJAX 不是新的编程语言，而是用于创建更好更快以及交互性更强的 Web 应用程序的技术。**

比较典型的 AJAX 应用就是百度的搜索栏，在搜索栏中输入任意字符后百度就会给出可能要搜索的内容的列表，改变输入内容这个列表也会改变，而我们并没有刷新页面获取新信息，这就是通过 AJAX 实现的！

### 2. 导入jQuery

AJAX 的核心是 **XMLHttpRequest 对象**（ 即 XMR ）。XHR 为向服务器发送请求和解析服务器响应提供了接口，能够以异步方式从服务器获取新数据。

JS 原生的 AJAX 就是要通过 XMLHttpRequest 对象实现，不过纯 JS 原生的 AJAX 了解就好，这里直接用 jQuery（一个 JavaScript 代码库，封装了 JavaScript 常用的功能代码）提供的 AJAX 方法！

**框架搭建**：给 SpringMCV-08-AJAX 项目添加 Web 框架，配置 Web（ Servlet、Filter ），配置 SpringMVC（开启注解、忽略静态资源、视图解析器、扫描注解），发布 war 包并导入 lib 库，这个过程基本是固定的了（基本配置，此处省略）。

然后通过一个简单的请求测试配置是否成功，这样 SpringMVC 的框架就搭建好了！

```java
@RestController
public class AJAXController {

    @RequestMapping("/test")
    public String Test(){
        return "Request Success!";
    }
}
```

然后将 jQuery 导入到项目中，可以去官网下载 js 文件放到项目的 /web/statics 目录下（静态资源目录），然后用到的时候作为资源导入

```html
<script src="${pageContext.request.contextPath}/statics/js/jquery-3.1.1.min.js"></script>
```

这样 jQuery 库就导入成功了！

### 3. jQuery AJAX

编写 index.jsp 使用 AJAX，导入 jQuery 库后，可以使用其中的 ajax、get、post 方法，后面两个都是对 ajax 方法的封装！

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>AJAX测试</title>
    <script src="${pageContext.request.contextPath}/statics/jquery-3.6.0.js"></script>
    <script>
      /* $ 符号能点出东西来说明 jQuery 导入成功了 */
      function ATEST(){
        /* post 方法封装了 ajax 方法*/
        $.post({
          /* 请求的路径 */
          url:"${pageContext.request.contextPath}/ajax",
          /* 携带的数据 */
          /* 这里将 username 的值命名为 name 发送，后端接收的是 name */
          data:{'name':$("#username").val()},
          /* 成功后执行回调函数 */
          success:function (data,status){
            alert(data);
            alert(status);
          }
        });
      }
    </script>
  </head>
  <body>
  <%-- onblur 失去焦点触发 ATEST 事件--%>
  用户名:<input type="text" id="username" onblur="ATEST()"/>
  </body>
</html>
```

由 input 标签设置失去焦点时触发 ATEST 方法，该方法提交了 AJAX 请求，`url` 为请求的路径，`data` 为请求时携带的数据，`success` 为请求成功后调用的函数，这里为弹出对话框，内容为返回数据 data 和状态 status！

在控制器中添加方法处理这个请求

```java
@RestController
public class AJAXController {

    @RequestMapping("/ajax")
    // 传过来的参数叫 name，用 response 响应数据
    public void AJAXTest(String name, HttpServletResponse response) throws IOException {
        if ("qiyuan".equals(name)){
            response.getWriter().print("true");
        }else{
            response.getWriter().print("false");
        }
    }
}
```

如果前端传过来的 name 为 `qiyuan`，则返回 data 为 true，否则为 false！

运行测试，**遇到了 BUG**：*Uncaught ReferenceError: $ is not defined*

这个问题是因为导入的 jQuery 库没有被加载，使用 Maven 的 clean 功能清理一下项目即可！

再次运行测试，返回数据显示成功！

**小结**：AJAX 和普通的请求一样，都是**带着数据**向某个地址**发起请求**，**接收返回数据**；只不过，在前端使用 AJAX 发起请求，可以使页面不刷新而获取数据，即这个请求和当前页面是**异步**的！

### 4. AJAX获取数据

上面的例子只是最简单的 AJAX 的使用，现在再写个例子，用 AJAX 获取实际数据。

首先添加一个实体类 User，只有最基本的三个属性 name、age、gender

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private String name;
    private int age;
    private String gender;
}
```

然后有一个请求，获取 User 列表（模拟一下，直接放进去）

```java
@RestController
public class AJAXController {

    @RequestMapping("/ajax2")
    // 传过来的参数叫 name，用 response 响应数据
    public List<User> AJAXTest2() {
        List<User> userList = new ArrayList<User>();
        userList.add(new User("祈鸢",18,"男"));
        userList.add(new User("小祈",18,"女"));
        userList.add(new User("Qiyuan",18,"男"));
        // 直接返回 List，前端接收的是什么类型的呢？
        return userList;
    }
}
```

**关键问题**：处理请求后直接返回 List 类型，前端能接受吗？**当然不能，并且会出现406错误（ HTTP 状态 406 - 不可接收），所以返回的应该是 JSON 字符串。**导入 Jackson 的包后（导完还要添加进 lib，烦），返回的 List 会直接被转换为 JSON 字符串（之前学习 JSON 的时候还手动转换······）！

此时发起请求测试一下，获取数据正常，是 JSON 字符串的格式！

```java
[{"name":"祈鸢","age":18,"gender":"男"},
 {"name":"小祈","age":18,"gender":"女"},
 {"name":"Qiyuan","age":18,"gender":"男"}]
```

前端显示数据的页面 test2.jsp（对应 ajax2 请求）

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>获取数据</title>
    <script src="${pageContext.request.contextPath}/statics/jquery-3.6.0.js"></script>
    <script>
        function AJAX2(){
            console.log("点击按钮")
            $.post("${pageContext.request.contextPath}/ajax2",function (data) {
                console.log(data)
                /* 把拿到的数据拼接到页面上 吐了！ */
                var html="";
                for (var i = 0; i <data.length ; i++) {
                    html+= "<tr>" +
                        "<td>" + data[i].name + "</td>" +
                        "<td>" + data[i].age + "</td>" +
                        "<td>" + data[i].gender + "</td>" +
                        "</tr>"
                }
                $("#content").html(html);
            });
        }
    </script>
</head>
<body>

<input type="button" id="btn" value="获取数据" onclick="AJAX2()"/>
<table width="80%" align="center">
    <tr>
        <td>姓名</td>
        <td>年龄</td>
        <td>性别</td>
    </tr>
    <tbody id="content">
        <%--通过请求获取！--%>
    </tbody>
</table>

</body>
</html>

```

刚进入这个页面时是没有  `<tbody>` 即数据内容的，点击获取数据按钮后，调用了 AJAX2 方法，发起 AJAX 请求获取数据，将数据拼接到当前页面上。此时页面也是没有刷新就获取到了数据！

### 5. AJAX验证登录

在之前 SMBMS 项目中就有这个功能，可以动态地提示输入的用户名是否存在，而不用等到和密码一起提交时再验证。现在就可以自己实现这个功能了！

首先是处理验证用户名是否存在的请求及方法

```java
@RestController
public class AJAXController {

    @RequestMapping("/ajax3")
    // 传过来的参数叫 name，用 response 响应数据
    public String AJAXTest3(String username) {
        String msg = "";
        // 模拟数据库中存在数据
        if (username!=null){
            if ("qiyuan".equals(username)){
                msg = "OK";
            }else {
                msg = "用户名不存在";
            }
        }
        // 由于 @RestController 注解和 jackson 包
        // 返回结果会被转换为 JSON
        return msg;
    }
}
```

通过 test3.jsp（对应 ajax3 请求😓）输入用户名

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>验证登录</title>
    <script src="${pageContext.request.contextPath}/statics/jquery-3.6.0.js"></script>
    <script>
        function AJAX3(){
            $.post({
                url:"${pageContext.request.contextPath}/ajax3",
                data:{'username':$("#name").val()},
                success: function (data) {
                    if (data.toString()=='OK'){
                        $("#userInfo").css("color","green");
                    }else {
                        $("#userInfo").css("color","red");
                    }
                    $("#userInfo").html(data);
                }
            });
        }
    </script>
</head>
<body>
<p>
    用户名:<input type="text" id="name" onblur="AJAX3()"/>
    <span id="userInfo"></span>
</p>
</body>
</html>
```

输入用户名后，点击其他地方，就会导致输入框失去焦点，调用 AJAX3 方法携带当前输入的用户名发起 AJAX 请求，后端根据当前的输入查询数据库是否有该用户，返回成功或失败的信息即可！

**遇到问题**：返回的中文信息又乱码了！回顾之前的 JSON 使用，要在 SpringMVC 的配置文件中添加配置

```xml
    <mvc:annotation-driven>
        <mvc:message-converters register-defaults="true">
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="UTF-8"/>
            </bean>
            <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
                <property name="objectMapper">
                    <bean class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
                        <property name="failOnEmptyBeans" value="false"/>
                    </bean>
                </property>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>
```

在返回的 JSON 转换为 HTTP 信息时进行了过滤，解决乱码问题！

### 6. 总结

简单的 AJAX 使用就学完了（没错还是简单的），对后端来说只不过是添加一个处理请求的方法，返回对应的数据，和应对普通的请求没有什么区别；对前端来说可就麻烦了，要携带数据发起请求，处理返回的信息，有很多地方要考虑！前端还不怎么熟练，希望学习 Vue 后能好点吧😶！
