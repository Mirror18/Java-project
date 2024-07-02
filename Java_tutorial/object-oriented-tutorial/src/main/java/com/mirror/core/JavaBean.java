package com.mirror.core;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * @author mirror
 */

public class JavaBean {
    /*
    javaBean就是
    一个标准的Java类
    首先要明白什么是一个标准的类
    有字段，有构造方法，跟字段有关的读写方法
    至于特殊用法就不属于一个标准类，属于拓展
     */

    /*
    或许这里可以拓展下pojo,DAO,DTO
    等这一系列概念
    主要还是应用分层搞出来的
    DAO数据访问对象
    DTO数据传输对象
    POJO简单无规则Java对象
     */

    /*
    同时如果要枚举JavaBean的属性
    可以用这个

     */
    public static void main(String[] args) {
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(Person.class);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        assert info != null;
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            System.out.println(pd.getName());
            System.out.println("  " + pd.getReadMethod());
            System.out.println("  " + pd.getWriteMethod());
        }
    }
}

/**
 * 这就是一个标准的POJO
 * 虽然多了@Data，只是有了这个，就
 * 可以只写private 字段，而不用写set和get，偷个懒
 */
//@Data
class Person {
    private String name;
    private int age;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

//    public boolean isChild() {
//        return age <= 6;
//    }
}