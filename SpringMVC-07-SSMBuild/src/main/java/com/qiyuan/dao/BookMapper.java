package com.qiyuan.dao;

import com.qiyuan.entity.Book;

import java.util.List;

public interface BookMapper {
    // 增
    int addBook(Book book);
    // 删
    int deleteBookById(int bookID);
    // 改
    int updateBook(Book book);
    // 查 一个
    Book queryBookById(int bookID);
    // 查 所有
    List<Book> queryAllBook();
}
