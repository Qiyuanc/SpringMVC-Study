## SpringMVC JSON使用

现如今前后端分离，传输数据离不开 JSON。在对 SpringMVC 进一步使用前，先来了解一下 JSON！本节新建 SpringMVC-06-JSON 项目学习 JSON 的基本使用。

### 1. JSON简介

[JSON](https://baike.baidu.com/item/JSON)（ JavaScript Object Notation，JS 对象简谱）是一种轻量级的数据交换格式，目前使用广泛。

JSON 的特点有

- 采用完全独立于编程语言的**文本格式**来存储和表示数据；
- 层次结构简单清晰，方便数据交换；
- 易于程序员阅读和编写，也易于机器解析和生成。

在 JavaScript 中，一切都是对象。因此，任何 JavaScript 支持的类型都可以通过 JSON 来表示，如字符串、数字、对象、数组等。

JSON 的语法格式为

- 方括号 `[ ]` 表示数组
- 花括号 `{ }` 表示对象
- 对象中的属性用键值对表示，冒号 `:` 表示属性，对象间由逗号 `,` 分割

```js
{"name": "Qiyuan", "age": 18, "address": {"country" : "CHINA", "city": "NJ"}}
[1, 2, 3, 4, 5, 6, 7, 8] // 数组中当然也可以保存上面的对象
```

 **JSON 和 JavaScript 对象的区别**

JSON 是 JavaScript 对象的字符串表示法，它使用文本表示一个 JS 对象的信息，本质是一个字符串

```js
// 这是一个对象，注意键名也是可以使用引号包裹的
var obj = {a: 'Hello', b: 'World'}; 
// 这是一个 JSON 字符串，本质是一个字符串
var json = '{"a": "Hello", "b": "World"}'; 
```

**JSON 和 JavaScript 对象之间的转换**

从 JSON 字符串转换为 JavaScript 对象，使用 JSON.parse() 方法

```js
var obj = JSON.parse('{"a": "Hello", "b": "World"}');
//结果为 {a: 'Hello', b: 'World'}
```

从  JavaScript 对象转换为 JSON 字符串，使用 JSON.stringify() 方法

```js
var json = JSON.stringify({a: 'Hello', b: 'World'});
//结果是 '{"a": "Hello", "b": "World"}'
```

基本的使用就这么多，还是来试试吧！

### 2. 前端转换

创建一个 JSONTest.html 文件，用代码测试一下 JSON 和 JS 对象之间的转换

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>JSONTest</title>
</head>
<body>
    <script>
        // 创建一个 JS 对象
        var user = { name:"祈鸢", age:18, id:7 }
        console.log(user)
    </script>
</body>
</html>
```

打开这个 html 文件，检查控制台输出，可以看到

```console
Object
age: 18
id: 7
name: "祈鸢"
```

这就输出了一个 JS 对象了（怎么在学 JS 了，不过也挺好）！

然后通过 `JSON.stringify` 把对象转换成 JSON 字符串输出，在通过 `JSON.parse` 把 JSON 字符串解析成对象

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>JSONTest</title>
</head>
<body>
    <script>
        // 创建一个 JS 对象
        var user = { name:"祈鸢", age:18, id:7 }
        // console.log(user)
        var json = JSON.stringify(user)
        console.log(json)
        console.log("==========")
        var obj = JSON.parse(json)
        console.log(obj)
    </script>
</body>
</html>
```

检查控制台输出

```console
{"name":"祈鸢","age":18,"id":7}
==========
Object
age: 18
id: 7
name: "祈鸢"
```

很简单啦，没什么问题。

### 3. Jackson使用

在前端测试完了，就要回到后端了。那么现在的问题是：如何在后端（ Controller ）把对象的信息转换为 JSON？

说到底，就是按照特定的格式把对象的信息转换为字符串，这种事情写个（亿个）工具类当然也能做到。不过这种事情肯定是有人已经做过了，所以只需要站在巨人的肩膀上，**导入 Jackson 的依赖**。

```xml
<!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.12.4</version>
</dependency>
```

接下来就可以使用一下了，配置 web.xml 和 springmvc-servlet.xml 的步骤就不多说了。

#### 3.1 返回JSON

首先创建一个实体类 User，就是我们要传输的对象（ Lombok，行！）

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String name;
    private int age;
    private String gender;

}
```

然后添加 UserController 类，先写个方法**直接返回一个字符串**试试

```java
@Controller
public class UserController {

    @RequestMapping("/json1")
    // 使用这个注解，返回的结果就不会去视图解析器，还是一个字符串
    @ResponseBody
    public String JSONTest1() throws JsonProcessingException {
        // 创建对象
        User user = new User("祈鸢", 18, "男");
        // 因为 @ResponseBody，返回的就是一个普通字符串
        return user.toString();
    }
}
// 网页输出
// User(name=??, age=18, gender=?)
```

**乱码了，要在 @RequestMapping 中设置返回类型和编码才行！**

```java
	@RequestMapping(value = "/json", produces = "application/json;charset=utf-8")
// 再次请求
// User(name=祈鸢, age=18, gender=男)
```

这样返回的字符串就正常啦。

既然能正常返回字符串，JSON 也是字符串，所以能返回 JSON（完美论证）！

再写一个方法，**通过对象解析器 `ObjectMapper` 的方法，把对象转换为 JSON 字符串返回！**

```java
@Controller
public class UserController {
    
    @RequestMapping(value = "/json2", produces = "application/json;charset=utf-8")
    // 使用这个注解，返回的结果就不会去视图解析器，还是一个字符串
    @ResponseBody
    public String JSONTest2() throws JsonProcessingException {
        //创建 jackson 的对象映射器以解析数据
        ObjectMapper mapper = new ObjectMapper();
        // 创建对象
        User user = new User("祈鸢", 18, "男");
        // 把对象转换为 json 字符串
        String str = mapper.writeValueAsString(user);
        // 因为 @ResponseBody，返回的就是一个普通字符串
        return str;
    }
}
// 网页输出
// {"name":"祈鸢","age":18,"gender":"男"}
```

看这输出，花括号表示对象，冒号表示属性，标准的 JSON！

**前后端分离：前端接收 JSON 格式的数据，解析后渲染到页面上；而后端只要提供接口给前端，返回 JSON 格式的数据就行了！**

#### 3.2 乱码统一解决

上面的乱码虽然解决了，不过每有一个方法，就要设置一下 produces，太麻烦了。SpringMVC 当然也想到了这点，所以可以在配置文件中统一配置!

在配置文件 springmvc-servlet.xml 中添加（扩展了开启注解的标签）

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

真好使啊，设置完后字体都变好看了😳。

总之用到了 Jakson 把它配置上去准没错！

#### 3.3 返回JSON统一解决

和乱码一样啦，要是有很多方法，每个方法都是返回 JSON 的，每个都添加 @ResponseBody 不也很麻烦？SpringMVC 又想到了（太牛了），可以在控制器类中统一配置！

把控制器类的 @Controller 注解换成 @RestController 即可

```java
@RestController
//@Controller
public class UserController {
    // 所有方法都不需要 @ResponseBody 了
    ...
}
```

就是这么简单，都没什么好说的了。

#### 3.4 集合对象输出

该解决的问题都解决了，上面测试的是一个对象的输出，没有问题，那么对象集合呢？再来试试。

再写一个方法，将集合转换为 JSON 字符串返回

```java
@RestController
//@Controller
public class UserController {
    
    @RequestMapping("/json3")
    public String JSONTest3() throws JsonProcessingException {

        //创建 jackson 的对象映射器以解析数据
        ObjectMapper mapper = new ObjectMapper();
        // 创建好多对象
        User user1 = new User("祈鸢A", 18, "男");
        User user2 = new User("祈鸢B", 18, "男");
        User user3 = new User("祈鸢C", 18, "男");
        User user4 = new User("祈鸢D", 18, "男");
        List<User> list = new ArrayList<User>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        list.add(user4);

        // 将对象解析成为 JSON 字符串
        String str = mapper.writeValueAsString(list);
        return str;
    }
}
// 网页输出
/*
[{"name":"祈鸢A","age":18,"gender":"男"},
 {"name":"祈鸢B","age":18,"gender":"男"},
 {"name":"祈鸢C","age":18,"gender":"男"},
 {"name":"祈鸢D","age":18,"gender":"男"}]
*/
```

看这方括号，是 JSON 数组（）！

#### 3.5 时间对象输出

基本的对象返回单个或集合都没有问题了，不过还有一种特殊的对象：时间对象，年月日横线分割乱七八糟的能不能输出呢？再来试试。

再增加一个方法，返回时间对象转换后的 JSON 字符串

```java
@RestController
//@Controller
public class UserController {
    
    @RequestMapping("/json4")
    public String JSONTest4() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        //创建时间对象 注意是 java.util.Date
        Date date = new Date();
        // 将对象解析成为 JSON 字符串
        String str = mapper.writeValueAsString(date);
        return str;
    }
}
// 网页输出
// 1630573794971
```

直接输出了一串好像毫无意义的数字，不过其实这是**时间戳**，之前也遇到过（还研究了一下时区的问题），得转换成看得懂的日期

```java
@RestController
//@Controller
public class UserController {
    
    @RequestMapping("/json5")
    public String JSONTest5() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        // 不使用时间戳
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 自定义日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 指定日期格式
        mapper.setDateFormat(sdf);

        Date date = new Date();
        String str = mapper.writeValueAsString(date);

        return str;
    }
}
// 网页输出
// "2021-09-02 17:14:12"
```

这样输出的就是正常的日期了！注意还有双引号哦，这还是一个 JSON 字符串！

如果经常使用到时间戳与正常日期的转换，可以直接提取为工具类 JsonUtil

```java
public class JsonUtil {

    public static String getJson(Object object) {
        // 调用带格式的方法
        return getJson(object,"yyyy-MM-dd HH:mm:ss");
    }

    public static String getJson(Object object,String dateFormat) {
        ObjectMapper mapper = new ObjectMapper();
        // 不使用时间戳
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 自定义日期格式
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        // 指定日期格式
        mapper.setDateFormat(sdf);
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

使用的时候直接调用工具类的方法，更加简便了！

```java
@RestController
//@Controller
public class UserController {
    
	@RequestMapping("/json6")
    public String JSONTest6() throws JsonProcessingException {

        //创建时间对象 注意是 java.util.Date
        Date date = new Date();
        // 将对象解析成为 JSON 字符串
        String str = JsonUtil.getJson(date);
        return str;
    }
}
```

当然，**`ObjectMapper` 会自己判断转换的对象是否是日期，是日期则应用设置的日期格式，若不是则会正常转换对象。所以这个工具类在什么地方都是能用的，省去了创建 `ObjectMapper` 对象的步骤！**

### 4. 总结

本节了解了什么是 JSON 以及 Jackson 的使用，总而言之，JSON 就是一种前端和后端约定好的，传输数据的格式。后端提供接口执行业务返回 JSON 数据（不像之前直接返回前端页面了），前端通过接口获取 JSON 数据去渲染页面，就是**前后端分离**了！

除了 Jackson，Fastjson 也可以做到对象和 JSON 之间的转换，不过就先不实践了，迟早得忘的东西用到再说吧😌！

