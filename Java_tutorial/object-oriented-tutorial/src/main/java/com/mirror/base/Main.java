package com.mirror.base;

import java.util.Arrays;

/**
 * @author mirror
 */
public final class Main {
    public static void main(String[] args) {
        /*
        new 创建一个实例
        Person ming是定义Person类型的变量 ming
        new Person()是创建Person的实例
        访问实例变量可以用 变量.字段
         */
        Person ming = new Person();

//        ming.name = "Xiao Ming";
//        ming.age = 18;
        ming.setName("xiao ming");
        System.out.println(ming.getName());
        Person hong = new Person();
//        hong.name = "xiao hong";
//        hong.age = 18;

        /*
        这里有一个有意思的事。
        不过也只是引用变量的事
         */
        var p = new Person();
        String bob = "Bob";
        p.setName(bob);
        System.out.println(p.getName());
        bob = "Alice";
        System.out.println(p.getName());
        /*
        这里猜猜看会输出什么
        结论是不变
        因为传输进去的是Bob的地址。
        下方是把bob的指向地址改为了Alice
        但是实际地址没有改变
        感觉懂了吧
        哪看下一个
         */
        /*
        不过这里需要先改下类，因为传输进去的参数有点问题
         */
        String[] fullname = new String[] {"homer", "Simpson"};
        p.setNames(fullname);
        System.out.println(Arrays.toString(p.getNames()));
        fullname[0] = "Bart";
        System.out.println(Arrays.toString(p.getNames()));
        /*
        就会发现名字变了
        咦，为啥。
        原因很简单，String[] 是传入的内存地址
        String传入的也是物理地址
        传入函数的地址没变。但是数组内部的地址变了。
        所以怎么比较呢，当成一个基本数组，内容变了自然就是变了。

         */
        var p1 = new Person("xiao",21);
        System.out.println(p1.getClass());
        /*
        向上转型
         */
        Person p2 = new Student("xiao",21,86);
        System.out.println("p2"+p2.getClass());
//        p2.setScore(21);
        /*
        就会发现这种操作是不允许的
         */
        /*
        向下转型
         */
//        Student s1 = (Student) p1;
        Student s2 = (Student) p2;
        System.out.println("s2 "+s2.getClass());
        /*
        向下转型会发现都可以。
        但是如果把原本父类的向下转型就会出问题
        就是因为子类一般来说都是比父类多了内容，那么自然指向就会出问题
         */
//        s1.setScore(98);

        /*
        这个运行就会出现问题
         */
        /*
        为了确定实例的类型可以用
        instanceof
         */
        System.out.println(p1 instanceof Person);
        System.out.println(p2 instanceof Person);
        System.out.println(s2 instanceof Person);
        /*
        现在加入判断
         */
        System.out.println(p1.getClass());
        if(p1 instanceof Student s3){
            System.out.println(s3.getClass());
        }


        /*
        多态
        Java的实例方法调用是基于运行时的实际类型的动态调用
        而不是变量的申明类型
         */
        Person person = new Student();
        System.out.println("person "+person.getClass());
        person.run();
    }
}
