## SSM框架整合 Spring层

搭建完了 MyBatis 层，就要把它纳入 Spring 的框架中来了！

### 1. 整合MyBatis配置

引入 Spring 后，MyBatis 的配置可以由 Spring 来进行，在 resources 目录下创建 spring-dao.xml 文件！

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd">
    
</beans>
```

配置连接池（这里使用 c3p0 连接池），本来在 MyBatis 配置文件中做的事情交给 Spring（数据库配置文件在 MyBatis 层写好了）！

```xml
    <!--关联数据库配置文件-->
    <context:property-placeholder location="classpath:db.properties"/>

    <!--配置连接池
        dbcp: 半自动化操作 不能自动连接
        c3p0: 自动化操作 自动加载配置文件
        还有 druid hikari 以后再说-->
    <!--这里用 c3p0 -->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <!-- 配置连接池属性 -->
        <property name="driverClass" value="${driver}"/>
        <property name="jdbcUrl" value="${url}"/>
        <!-- 这里 name 叫 user 和 MyBatis 不一样（username）！-->
        <property name="user" value="${user}"/>
        <property name="password" value="${password}"/>

        <!-- c3p0 连接池的私有属性 -->
        <property name="maxPoolSize" value="30"/>
        <property name="minPoolSize" value="10"/>
        <!-- 关闭连接后不自动 commit -->
        <property name="autoCommitOnClose" value="false"/>
        <!-- 获取连接超时时间 -->
        <property name="checkoutTimeout" value="10000"/>
        <!-- 获取连接失败重试次数 -->
        <property name="acquireRetryAttempts" value="2"/>
    </bean>
```

注册 SqlSessionFactory 的 bean，绑定 MyBatis 配置文件

```xml
    <!-- SqlSessionFactory -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!-- 引用上面的数据源 -->
        <property name="dataSource" ref="dataSource"/>
        <!-- 绑定 MyBatis 配置文件 -->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
    </bean>
```

**重点来了！在之前（ https://www.cnblogs.com/qiyuanc/p/Spring_MyBatis.html ），需要将接口虚拟的实现类 Mapper.xml 实例化为一个真实的实现类 MapperImpl，才能注入到 Service 层的对象中（总不能注入 Mapper.xml 吧）！**

**这里通过使用 `MapperScannerConfigurer`，把这种工作交给 Spring 去做！**

```xml
    <!-- 配置扫描 Dao 接口的包，动态地将 Dao 接口实现类注入到 Spring 容器中 -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!-- 注入 sqlSessionFactory，注意是 BeanName！所以不用 ref！ -->
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <!-- 给出需要扫描 Dao 接口的包 -->
        <property name="basePackage" value="com.qiyuan.dao"/>
    </bean>
```

> basePackage 属性是让你为映射器接口文件设置基本的包路径，可以使用分号或逗号作为分隔符设置多于一个的包路径。每个映射器将会在指定的包路径中递归地被搜索到。
>
> 注意，没有必要去指定 SqlSessionFactory 或 SqlSessionTemplate，因 为 MapperScannerConfigurer 将会创建 MapperFactoryBean，之后自动装配。（**Qiyuan：也就是说会自动创建 SqlSession 并注入到实现类中**）
>
> 但是，如果你使用了一个以上的 DataSource，那么自动装配可能会失效。这种情况下，可以使用  sqlSessionFactoryBeanName 或 sqlSessionTemplateBeanName 属性来设置正确的 bean 名称来使用。这就是它如何来配置的，注意 bean 的名称是必须的，而不是 bean 的引用。

**`MapperScannerConfigurer` 会扫描指定包下的接口和其对应的 Mapper.xml，通过反射自动生成接口实现类（真），也就是说我们虽然没有写 BookMapperImpl，但它已经帮我们生成了！**

### 2. 配置Service层

上面是 Spring 对应 Dao 层的配置了。现在还需要将 Dao 层的对象注入到 Service 层中，Service 层才能用这些对象执行相应的业务。创建 spring-service.xml 文件，模板同上。

将业务层对象交给 Spring 管理，同时在其中注入 Dao 层对象

```xml
    <bean id="BookServiceImpl" class="com.qiyuan.service.BookServiceImpl">
        <!-- 这个 bookMapper 就是之前自动创建的实现类！ -->
        <property name="bookMapper" ref="bookMapper"/>
    </bean>
```

这里的 bookMapper 就是  `MapperScannerConfigurer` 扫描接口和其对应的 Mapper.xml 创建的实现类！

也可以通过注解的方式把业务层对象交给 Spring，不过要在业务层的类上添加注解和开启注解扫描，这里先不用这种方式了

```xml
	<!-- 类上 @Service -->
	<!-- 扫描注解，不过这里其实没用到 -->
    <context:component-scan base-package="com.qiyuan.service"/>
```

下一步就是配置事务管理器了

```xml
    <!-- 配置事务管理器 -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!-- 注入数据库连接池 -->
        <property name="dataSource" ref="dataSource" />
    </bean>
```

开启事务管理器后，就可以使用 AOP 进行事务的织入了！不过这里没什么事要干，织入包也没导，就先这样吧！不过这样就可以管理到数据库事务了吗······

### 3. Spring层测试

和之前一样，写完一层测试一下，不然以后出问题找谁去！

在测试前，先在 applicationContext.xml 这个总的配置文件中导入其他配置文件

```xml
    <import resource="classpath:spring-dao.xml"/>
    <import resource="spring-service.xml"/>
```

这样加载的时候只加载这一个文件就行了。

写个测试方法测试一下

```java
public class MyTest {

    @Test
    public void Test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookService bookServiceImpl = context.getBean("BookServiceImpl", BookService.class);
        // 增加
        Book book1 = new Book(5, "Python教程", 6, "起飞级Python教程");
        bookServiceImpl.addBook(book1);
        // 查询所有
        List<Book> books = bookServiceImpl.queryAllBook();
        for (Book book : books) {
            System.out.println(book.toString());
        }
    }
}
```

加载配置文件，获取业务对象，执行业务，三步！

运行的时候遇到了数据库连接错误

```java
java.sql.SQLException: Access denied for user 'ASUS'@'localhost' (using password: YES)
```

经过排查（百度），这个弱智 c3p0 居然会把 `${username}` 当成计算机用户名！

```xml
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    	...
        <!-- 弱智 c3p0！！！ -->
        <property name="user" value="${username}"/>
        ...
    </bean>
```

解决方法就是把 db.properties 和 spring-dao.xml（即上面）中的 username 改成 user 即可，这样连接时的用户名就是 db.properties 中的 user 了。气死我了！

改完 BUG 再次运行，结果正常！

```java
Book(bookID=1, bookName=Java教程, bookCounts=1, detail=入门级JAVA教程)
Book(bookID=2, bookName=MySQL教程, bookCounts=10, detail=提升级MySQL教程)
Book(bookID=3, bookName=Linux教程, bookCounts=5, detail=入土级Linux教程)
Book(bookID=4, bookName=C++教程, bookCounts=3, detail=复活级C++教程)
Book(bookID=6, bookName=Python教程, bookCounts=6, detail=起飞级Python教程)
```

可以看到插入也是成功的，说明事务确实是开启了也提交成功了；至于 bookID 对不上的问题，就是数据库自增的问题了，后面再研究！

未完，没有总结。