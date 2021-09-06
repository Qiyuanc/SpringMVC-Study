import com.qiyuan.dao.BookMapper;
import com.qiyuan.entity.Book;
import com.qiyuan.service.BookService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName MyTest
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/3 18:07
 * @Version 1.0
 **/
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
