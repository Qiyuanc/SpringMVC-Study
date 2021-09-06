package com.qiyuan.controller;

import com.qiyuan.entity.Book;
import com.qiyuan.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName BookController
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/3 21:19
 * @Version 1.0
 **/
@Controller
@RequestMapping("/book")
public class BookController {
    // 自动装配 bean
    @Autowired
    @Qualifier("BookServiceImpl")
    private BookService bookService;

    @RequestMapping("/booksList")
    public String queryAllBook(Model model){
        // 调用业务层执行业务，业务层调用 Dao 层获取数据！
        List<Book> books = bookService.queryAllBook();
        // 将结果放入 Model 中
        model.addAttribute("booksList",books);
        // 返回视图
        return "booksList";
    }

    @RequestMapping("/toAddBook")
    public String toAddBook(Model model){
        // 前往新增书籍的页面
        return "addBook";
    }

    @RequestMapping("/addBook")
    // 实体类做参数，控制器会用反射获取表单中对应实体类属性的数据
    public String addBook(Book book){
        System.out.println("addBook: "+book);
        bookService.addBook(book);
        // 添加完书籍 返回列表（会自动补上项目路径）
        return "redirect:/book/booksList";
    }

    @RequestMapping("/toUpdateBook")
    public String toUpdateBook(Model model, int id) {
        // 查询要修改的书籍的信息
        Book book = bookService.queryBookById(id);
        System.out.println("toUpdateBook: "+book);
        // 携带修改前信息前往修改书籍的页面
        model.addAttribute("book",book );
        return "updateBook";
    }

    @RequestMapping("/updateBook")
    public String updateBook(Model model, Book book) {
        System.out.println("updateBook: "+book);
        // 修改书籍信息
        bookService.updateBook(book);
        // 修改完 返回列表
        return "redirect:/book/booksList";
    }

    @RequestMapping("/deleteBook/{bookID}")
    public String deleteBook(@PathVariable("bookID") int id) {
        System.out.println("deleteBook: "+id);
        // 删除书籍
        bookService.deleteBookById(id);
        // 删除完 返回列表
        return "redirect:/book/booksList";
    }
}
