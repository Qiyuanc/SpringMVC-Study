package com.qiyuan.service;

import com.qiyuan.entity.Book;

import java.util.List;

/**
 * @ClassName BookService
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/3 14:11
 * @Version 1.0
 **/
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
