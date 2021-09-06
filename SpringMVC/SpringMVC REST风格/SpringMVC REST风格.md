## SpringMVC REST风格

本节新建 SpringMVC-05-REST 项目研究一下 REST 风格的使用，项目基本配置已经做好，后面就不说了！

### 1. REST介绍

REST 即 Representational State Transfer，意为表示层状态传递，是一种软件架构风格，可以降低开发的复杂性，提高系统的可伸缩性。REST 是一组架构约束条件和原则，满足这些约束条件和原则的应用程序或设计就是 RESTful。

首先，互联网上的信息都可以被抽象为资源，用户可以发起请求对这些资源进行操作，**操作的方式如：POST、DELETE、PUT、GET，对应增加、删除、修改、查询（不是全部）！**

按照之前的方式，发起请求操作资源时，请求的格式为

- **增加**：127.0.0.1/item/add.action?id=1
- **删除**：127.0.0.1/item/delete.action?id=1
- **修改**：127.0.0.1/item/update.action
- **查询**：127.0.0.1/item/select.action

若使用 REST 风格，请求就可以为

- **增加**：127.0.0.1/item/1，**POST**
- **删除**：127.0.0.1/item/1，**DELETE**
- **修改**：127.0.0.1/item，**PUT**
- **查询**：127.0.0.1/item，**GET**

**RESTFUL 的特点为**

1. 每一个URI代表1种资源
2. 客户端使用 GET、POST、PUT、DELETE 等表示操作方式的动词对服务端资源进行操作
3. 通过操作资源的表现形式来操作资源（不懂）
4. 资源的表现形式是XML或者HTML（不懂）

简而言之，**REST 风格即通过 URL 定位资源，用 HTTP 描述操作！**

### 2. REST风格使用

#### 2.1 之前的方式

使用 REST 风格前，先回顾一下之前的方式。

如这个控制器中 Add 方法的请求路径为 /add，参数为 a 和 b，将参数相加后输出到视图；

```java
@Controller
public class ControllerREST {

    // 请求路径: .../add?a=1&b=1
    @RequestMapping("/add")
    public String Add(int a, int b, Model model) {
        // 业务处理
        int res = a + b;
        model.addAttribute("msg","a + b = "+res);
        // 返回视图
        return "test";
    }
}
```

通过 localhost:8080/.../add?a=1&b=1 发起请求，前端显示结果为 1 + 1 = 2，没有问题！

#### 2.2 REST风格

再添加 AddREST 方法，使用 REST 风格获取参数

```java
public class ControllerREST {
    
    // 请求路径: .../add/1/2
	@RequestMapping("/add/{a}/{b}")
    public String AddREST(@PathVariable int a,@PathVariable int b, Model model) {
        // 业务处理
        int res = a + b;
        model.addAttribute("msg","a + b = "+res);
        // 返回视图
        return "test";
    }
}
```

访问这个方法的请求就变成了 localhost:8080/.../add/1/2，输出 a + b = 3！

**路径变量传值的优点：**

1. **通过 @PathVariable 注解，把 URL 中的路径变量 {a} 和 {b} 变成了参数（一样的会自动对应，不一样的要用注解声明）！**非常的简洁方便！
2. **通过路径变量的类型，约束了请求的参数类型。**如上面若使用 .../add/1/b 访问，则服务器会报400错误（错误的请求）！

#### 2.3 指定请求类型

通过 @RequestMapping，还可以指定访问的请求类型。

添加 AddRESTGET 方法，指定访问请求类型为 POST（要先把 AddREST 方法注释掉，不然其实访问的是 AddREST 方法）

```java
public class ControllerREST {
    
	// 请求路径: .../add/1/2，且请求方法必须为 POST
    @RequestMapping(value = "/add/{a}/{b}",method = RequestMethod.POST)
    public String AddRESTGET(@PathVariable int a,@PathVariable int b, Model model) {
        // 业务处理
        int res = a + b;
        model.addAttribute("msg","a + b = "+res);
        // 返回视图
        return "test";
    }
}
```

此时再通过上面的 URL 地址 localhost:8080/.../add/1/2 访问，会出现405错误（方法不允许）！**因为浏览器 URL 默认的请求方法是 GET，但设定了必须用 POST 方法访问！**

把请求方法改成 GET，浏览器就能正常访问了！

```java
	@RequestMapping(value = "/add/{a}/{b}",method = RequestMethod.GET)
```

或许这个注解有点长，可以使用衍生的 @GetMapping 注解，相当于提前设置好了请求方法为 GET，只要路径就行了！

```java
	@GetMapping("/add/{a}/{b}")
```

**注意**：上面这两个是不能同时存在为两个方法的，否则会出现 Ambiguous mapping 错误（模棱两可的映射），即不知道要让哪个方法处理请求了！

类似的，其他请求方式也有对应的注解

```java
    @GetMapping
    @PostMapping
    @PutMapping
    @DeleteMapping
    ...
```



### 3. Controller获取参数方式

这里可以回答一下上一节的问题了！

**Controller 中的方法，获取请求中的参数的方式有**：

1. 直接将请求参数名作为 Controller 中方法的形参，就是上面的 `之前的方式`，要求前端表单变量名与形参名相同！

   ```java
   @RequestMapping("/add")
   public String Add(int a, int b, Model model)
   ```

2. 使用 @RequestParam 给形参绑定请求参数，如前端表单参数为 var1 和 var2，可以通过这种方式对应！

   ```java
   @RequestMapping("/add")
   public String Add(@RequestParam("var1") int a, @RequestParam("var2") int b, Model model)
   ```

3. 使用 @RequestMapping 接收参数，再用 @PathVariable 对应到形参，就是上面的 `REST风格`！

   ```java
   @RequestMapping("/add/{a}/{b}")
   public String AddREST(@PathVariable int a,@PathVariable int b, Model model)
   ```

   如果前端表单变量名与形参名不同，还要在 @PathVariable 中设置，如 @PathVariable("a")！

4. 使用实体类对象（类中的属性作为参数），原理是利用反射找到对应的属性！

   ```java
   @ReauestMapping(value="/login")
   public String login(User user)
   ```

   这时的请求应为 .../login?id=1&name=qiyuan&pwd=0723，参数与实体类的属性对应上！

5. 设置方法参数为 req 和 resp，用 Servlet 的 API 获取！

   ```java
   public String login(HttpServletRequest req, HttpServletResponse resp){
   	String username = req.getParameter("username");
   }
   ```

   应该不会有人这么干吧···不过这种方式解释了我的疑惑，即请求和响应参数是存在的，只是看你怎么用！

### 4. 总结

没什么好总结的啦，REST 风格这么好使，以后肯定用啦。

至于 Controller 获取参数的方式，知道有这么多种，搞明白就好啦，反正也是用 REST 风格的😶。

