package com.mirror.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author mirror
 */
public class ConstructorA {
    public static void main(String[] args) {
        /*
        还记的如何创建实例的么，根据一个类
        new Person
        如果通过反射的话，泽可以调用Class提供的newInstance
        但当时吐槽过，虽然能创建，但是被idea给报错了，或者说弃用了。
        当时吐槽过有专门的构造器方法，所以这个就来了
         */
        Constructor cons1 = null;
        try {
            cons1 = Integer.class.getConstructor(int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

// 调用构造方法:
        Integer n1 = null;
        try {
            n1 = (Integer) cons1.newInstance(123);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(n1);

        // 获取构造方法Integer(String)
        Constructor cons2 = null;
        try {
            cons2 = Integer.class.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Integer n2 = null;
        try {
            n2 = (Integer) cons2.newInstance("456");
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(n2);

        /*
        获取构造方法还是同理
        getConstructor(Class...)：获取某个public的Constructor；
        getDeclaredConstructor(Class...)：获取某个Constructor；
        getConstructors()：获取所有public的Constructor；
        getDeclaredConstructors()：获取所有Constructor
         */
        /*
        另外就是constructor是当前类定义的恶狗早方法，和服嘞无关
         */
    }
}
