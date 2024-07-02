package com.mirror.base;

import lombok.Data;

import java.time.Year;

/**
 * @author mirror
 */
/*
这里用了@Data注解。说白了就是把get和set还有hashcode给设置进去了
不用手写这些方法。这是lombk的注解包。后续会常用
尤其是到spring框架之后
 */
@Data
public class Person {
    /*
    类是一种数据模板，所以class本身就是一种数据类型
     */

    private String name;
    protected int age;
    private String[] names;
    private int birth;
    /*
    这是使用组合
     */
    protected Book book;

    /*
    这是构造函数。有多种形式。顺便说明什么叫方法声明
    返回嘞星星 ，函数名 ， 入参；这三个一起构成一个方法声明
    但是方法重载只看的参数列表不同，也就是入参不同。
    方法重载的前提是方法名相同，在同一个类中。至于返回类型无所谓
     */
    /**
    所以这个构造函数就是一个方法重载
     构造方法和普通方法的区别就是构造方法没有返回值，名字是类名
     同时构造方法也可以调用其他构造方法，就是this
     */
    public Person(){}
    public Person(String name , int age){
        this.name = name;
        this.age = age;
    }
    public Person(String name){
        this("mirror",18);
    }

    /**
     * 定义方法
     * 修饰符 方法返回类型 方法名（方法参数列表）{
     * 方法语句；
     * return 方法返回值；
     * }
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("invalid name");
        }
        this.name = name.strip();
    }

    public int getAge() {
        return calcAge(Year.now().getValue());

    }

    private int calcAge(int currentYear) {
        /*
        this 始终指向当前实例
        通过this.field就可以访问当前实例的字段
         */
        return currentYear - this.birth;
    }

    public void run(){
        System.out.println("Person.run");
    }
    public void aa(){}
//    public abstract void bb(){}
}
