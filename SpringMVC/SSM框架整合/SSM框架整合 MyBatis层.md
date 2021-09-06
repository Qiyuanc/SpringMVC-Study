## SSM框架整合 MyBatis层

搭建完基本的环境后，就要从 SSM 框架最基本的 MyBatis 层开始搭建了。

### 1. 数据库配置文件

为了方便修改（虽然这里不会修改），数据库配置最好提取出来，单独写在一个配置文件 db.properties 中

```properties
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/ssmbuild?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC
# 为了 c3p0，这里只能叫 user 了
user=root
password=0723
```

然后用 IDEA 连接数据库，执行一下最基本的查询语句看看数据库是否正常即可。

### 2. MyBatis配置文件

大部分的配置都可以在 Spring 的配置文件中进行，不过 MyBatis 的配置文件中还是留下 typeAlias 和 mappers 的配置，方便查看

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <typeAliases>
        <package name="com.qiyuan.entity"/>
    </typeAliases>
    
    <!--数据源交给 Spring 配置-->

    <mappers>
        <!--现在还没有-->
    </mappers>

</configuration>
```

### 3. 编写实体类

在 com.qiyuan.entity 包下，对应数据库中表的字段，编写对应的实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {

    private int bookID;
    private String bookName;
    private int bookCounts;
    private String detail;

}
```

数据库就一张表 books，所以实体类也就只有一个 Book，使用了 Lombok

### 4. 编写Dao层接口

在 com.qiyuan.dao 包下，编写一个接口包含所有对数据库的操作

```java
public interface BookMapper {
    // 增
    int addBook(Book book);
    // 删
    int deleteBook(int bookID);
    // 改
    int updateBook(Book book);
    // 查 一个
    Book queryBook(int bookID);
    // 查 所有
    List<Book> queryAllBook();
}
```

基本的增删改查操作，查询分为查一个或查所有。

### 5. 编写Mapper.xml文件

由接口指定了对数据库的操作后，就要由 Mapper.xml 文件来实现了（暂时的实现类），Mapper.xml 文件也放在 dao 包下

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.qiyuan.dao.BookMapper">

    <!--增加 Book -->
    <insert id="addBook" parameterType="Book">
      insert into books(bookName,bookCounts,detail)
      values (#{bookName}, #{bookCounts}, #{detail})
    </insert>

    <!--根据 id 删除 Book -->
    <delete id="deleteBookById" parameterType="int">
      delete from books where bookID = #{bookID}
    </delete>

    <!--更新 Book -->
    <update id="updateBook" parameterType="Book">
      update books
      set bookName = #{bookName}, bookCounts = #{bookCounts}, detail = #{detail}
      where bookID = #{bookID}
    </update>

    <!--根据 id 查询 Book -->
    <select id="queryBookById" resultType="Book">
      select * from books
      where bookID = #{bookID}
    </select>

    <!--查询全部Book-->
    <select id="queryAllBook" resultType="Book">
      select * from books
    </select>

</mapper>
```

编写完 Mapper.xml 后要记得注册！这时候留着 MyBatis 配置文件的好处就来了

```xml
    <mappers>
        <mapper class="com.qiyuan.dao.BookMapper"/>
    </mappers>
```

可以直接在 MyBatis 的配置文件中配置，不用跑到 Spring 去，思路比较清晰！

### 6. 编写Service层

现在要把 Dao 层的对数据库的操作封装到 Dervice 层中，对应不同的业务！

在 com.qiyuan.service 包下编写 Service 层接口，方法对应 Dao 层的接口

```java
// BookService 调用 dao 层
public interface BookService {
    //增加 Book
    int addBook(Book book);
    //根据 id 删除 Book
    int deleteBookById(int id);
    //更新 Book
    int updateBook(Book book);
    //根据 id 查询 Book
    Book queryBookById(int id);
    //查询全部 Book
    List<Book> queryAllBook();
}
```

有了 Service 层的接口后，需要一个实现类实现接口中的方法

```java
public class BookServiceImpl implements BookService{

    // 用一个 BookMapper 对象进行对数据库的操作，这个对象由 Spring 创建和注入，
    // 不过 Spring 注入的应该是实现类 BookMapperImpl，配置 Spring 的时候再说！
    private BookMapper bookMapper;

    public void setBookMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public int addBook(Book book) {
        return bookMapper.addBook(book);
    }

    public int deleteBookById(int id) {
        return bookMapper.deleteBookById(id);
    }

    public int updateBook(Book book) {
        return bookMapper.updateBook(book);
    }

    public Book queryBookById(int id) {
        return bookMapper.queryBookById(id);
    }

    public List<Book> queryAllBook() {
        return bookMapper.queryAllBook();
    }
}
```

通过内置一个 Mapper 对象，封装了使用 Mapper 对象的操作，方便 Spring 管理，用户只需调用 Service 层即可进行业务操作，不用在意 Dao 层的实现！

**注意**：Spring 注入进来的，应该是 BookMapperImpl 对象，也就是要在虚拟的实现类 BookMapper.xml 的基础上封装出一个真实的实现类 BookMapperImpl 来注入！

至此，MyBatis 层就搭建完成了（包含 Dao 层和 Service 层）！

### 7. MyBatis层测试

为了保证后面出问题好找，写完 MyBatis 层先测试一下！

为了测试要在配置文件中先加上数据源配置

```xml
<environments default="development">
    <environment id="development">
        <transactionManager type="JDBC"/>
        <dataSource type="POOLED">
            <property name="driver" value="com.mysql.jdbc.Driver"/>
            <property name="url" value="jdbc:mysql://localhost:3306/ssmbuild?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=true&amp;serverTimezone=UTC"/>
            <property name="username" value="root"/>
            <property name="password" value="0723"/>
        </dataSource>
    </environment>
</environments>
```

然后写个方法测试一下查询和增加的功能

```java
public class BookServiceImpl implements BookService{

    @Test
    public void Test() throws IOException {
        // 之前 MyBatis 工具类的工作
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // 查询一个
        /*Book book = this.queryBookById(3);
        System.out.println(book.toString());*/
        // 增加
        /*Book book = new Book(4, "C++教程", 3, "复活级C++教程");
        System.out.println(this.addBook(book));
        sqlSession.commit();*/
        // 查询所有
        List<Book> books = this.queryAllBook();
        for (Book book : books) {
            System.out.println(book.toString());
        }
    }
}
```

运行测试，最后的结果（增加的时候又忘记事务了）

```java
Book(bookID=1, bookName=Java教程, bookCounts=1, detail=入门级JAVA教程)
Book(bookID=2, bookName=MySQL教程, bookCounts=10, detail=提升级MySQL教程)
Book(bookID=3, bookName=Linux教程, bookCounts=5, detail=入土级Linux教程)
Book(bookID=4, bookName=C++教程, bookCounts=3, detail=复活级C++教程)
```

查询成功，说明 MyBatis 层没有问题！

未完，没有总结。