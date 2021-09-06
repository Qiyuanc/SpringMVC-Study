## SpringMVC结果跳转方式

上节研究了使用 REST 风格发起请求，这节来研究一下 Controller 如何跳转对对应的视图！还是使用 Spring-05-REST 项目（都是重复配置，还不如多写几个 Controller，反正内容不多）！

### 1. ModelAndView

刚开始学习时用的方式，仅限研究执行过程使用，一般都不用的啦。

编写 ControllerMAV 类实现 Controller 接口（需要 `org.springframework.web.servlet.mvc.Controller` 的包，和注解 Controller 的包不是同一个）

```java
public class ControllerMAV implements Controller {

    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        // 获取本次会话的 Session
        HttpSession session = httpServletRequest.getSession();
        String id = session.getId();
        // 放信息，设视图
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg",id);
        mv.setViewName("test");

        return mv;
    }
}
```

**通过 ModelAndView 的 setViewName 方法设置返回的视图名字，再返回这个 mv 对象，视图解析器解析其中的视图名字。**

使用这种方式不要忘记配置 bean

```xml
<bean id="/testmav" class="com.qiyuan.controller.ControllerMAV"/>
```

运行测试，成功输出 SessionID！不过好麻烦，就当小复习一下了！

### 2. Model

这就是使用注解后的方式了！

编写 ControllerM 类，添加 @Controller 注解

```java
@Controller
public class ControllerM {
    @RequestMapping("/testm")
    public String TestM(Model model, HttpServletRequest req, HttpServletResponse resp){
        // 获取本次会话的 Session
        HttpSession session = req.getSession();
        String id = session.getId();
        // 放信息
        model.addAttribute("msg",id);
        return "test";
    }
}
```

**这里用到了 req 和 resp，说明它们是存在的！**要用的话就当参数传进来就好了；Model 参数不用多说，放信息的嘛。

**通过直接返回 String 字符串设置要跳转的视图，这个 String 字符串就是视图名字。**

运行测试，也成功输出 SessionID，简单，舒服！

### 3. ServletAPI

既然有 req 和 resp，那就肯定能用它们的方法，当然也可以用原生 API 进行**转发**或**重定向**了！**这种方式就不需要视图解析器了！**

复习一下转发和重定向的区别：

- **转发**是服务器内部行为，用户只发起一次请求
- **重定向**是服务器返回另一请求给用户，用户发起二次请求

编写 ControllerSAPI 类，添加 @Controller 注解

```java
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
```

再复习一下，转发会直接在当前项目路径下寻找，而重定向由用户再次发起请求，所以路径要是完整的！

运行测试，跳转都成功！**这种方式的跳转就没有经过视图解析器了，完全是 Servlet 实现的（没人会这么干吧）！**

### 4. SpringMVC

用 SpringMVC 的方式实现一下 ServletAPI 做的转发和重定向，也是不需要视图解析器的（直接一步到位了，当然只是演示用）！

编写 ControllerSMVC 类，添加 @Controller 注解

```java
@Controller
public class ControllerSMVC {

    @RequestMapping("/testsmvc/forward")
    public String TestF(Model model){
        model.addAttribute("msg","Qiyuan转发！");
        // 转发
        // 没有视图解析器当然要手动拼接辽
        return "forward:/WEB-INF/jsp/test.jsp";
    }

    @RequestMapping("/testsmvc/redirect")
    public String TestM(Model model){
        model.addAttribute("msg","Qiyuan重定向！");
        // 重定向
        // 这里和直接用 Servlet 不一样！SpringMVC 会自动补上项目路径！
        return "redirect:/index.jsp";
    }
}
```

运行测试，跳转成功！**主要是通过返回的字符串中的 forward 和 redirect 关键字实现的！**

转发没什么好说的，重定向比 Servlet 智能多了，会自动加上项目路径！

### 5. 总结

没什么好总结的，就是盘点一下结果跳转的方式！用的最多的还是3，偶尔用用5！也就这样了😕。