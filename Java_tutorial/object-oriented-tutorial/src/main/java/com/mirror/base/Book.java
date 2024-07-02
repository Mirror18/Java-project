package com.mirror.base;

/**
 * @author mirror
 */
public class Book {
    public String name;
    public String author;
    public String isbn;
    final double price;

    public Book(double price){
        this.price = price;
    }
    public Book(){
        price = 100;
    }


    public String hello(){
        return "hello " + name;
    }
    public final String hi(){
        return "hi"+author;
    }
    /*
    一个class可以包含多个字段field。
    字段用来描述一个类的特征。
    通过class，把一组数据汇集到一个对象上，实现数据封装
     */

    /*
    关于复写。因为所有类都是继承于Object类
    所以有这三个方法是需要去实现的
    toString() : 把instance 输出为String；
    equals() :判断两个instance 是否逻辑想等
    hashCode():计算一个instance的哈希值

     */
    @Override
    public String toString(){
        return "Book:name=" + name;
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof Book book){
            return this.name.equals(book.name);
        }
        return false;
    }
    @Override
    public int hashCode(){
        return this.name.hashCode();
    }
}
