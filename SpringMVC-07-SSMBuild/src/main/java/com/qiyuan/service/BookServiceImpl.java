package com.qiyuan.service;

import com.qiyuan.dao.BookMapper;
import com.qiyuan.entity.Book;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName BookServiceImpl
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/3 14:16
 * @Version 1.0
 **/
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

    @Test
    public void Test() throws IOException {
        // 之前 MyBatis 工具类的工作
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        this.bookMapper=sqlSession.getMapper(BookMapper.class);
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
