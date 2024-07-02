package com.mirror.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author mirror
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Student extends Person {
    private int score;

    /**
     * 同时在这里可以看到调用了super
     * 原因也很简单
     * 任何class的构造方法
     * 第一行必须是调用父类的构造方法
     * @param name
     * @param age
     * @param score
     */
    public Student(String name , int age , int score){
        super(name,age);
        this.score = score;
    }
    public Student(){}

    @Override
    public void run(){
        System.out.println("Student.run");
    }
    public void run(String s){}
}
