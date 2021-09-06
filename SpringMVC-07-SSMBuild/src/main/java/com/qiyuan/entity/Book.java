package com.qiyuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName Book
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/9/3 13:45
 * @Version 1.0
 **/
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
