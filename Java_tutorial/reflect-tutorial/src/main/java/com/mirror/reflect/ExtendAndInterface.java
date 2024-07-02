package com.mirror.reflect;

/**
 * @author mirror
 */
public class ExtendAndInterface {
    public static void main(String[] args) {
        /*
    就是获取继承关系
     */
        String s = "";
        Class cls = s.getClass();
        Class n = cls.getSuperclass();
        System.out.println(n);

        Class o = n.getSuperclass();
        System.out.println(o);
        /*
        这里是记好这种关系。
        因为类双亲加载模型，到时候就够头疼的
         */

        /*
        获取接口
         */
        Class intA = Integer.class;
        Class[] is = intA.getInterfaces();
        for(Class i : is){
            System.out.println(i);
        }
        /*
        这些只有当前类的直接实现的接口类型，不包含父类实现的借口类型
         */

        // Integer i = ?
        Integer.class.isAssignableFrom(Integer.class); // true，因为Integer可以赋值给Integer
// Number n = ?
        Number.class.isAssignableFrom(Integer.class); // true，因为Integer可以赋值给Number
// Object o = ?
        Object.class.isAssignableFrom(Integer.class); // true，因为Integer可以赋值给Object
// Integer i = ?
        Integer.class.isAssignableFrom(Number.class); // false，因为Number不能赋值给Integer

    }
}
/*
关于反射已经写完了，剩下一个动态代理，虽然用这个创建的，但是是关于这个的应用
这些主要写管于instance的创建
众所周知，实例是通过new 一个类实现的，那么JVM究竟干了什么

先明白这是基于哪，这是基于变异好的文件，在编译好的文件中new Person()
1.加载类，classloader加载.class文件到内存，（将类的字节码加载到JVM中，并将其转换为class对象）
    1.1 类的连接。三步，验证，准备（为类的静态变量分配内存，初始化）。解析（常量池的符号引用替换为直接引用
    1.2类的初始化，执行类的静态初始化代码
2.执行new，申请一片空白内存。
    2.1在堆内存分配内存
    2.2初始化默认值
    2.3设置对象头，包括对象的元数据，
3.调用构造器，创建一个空白对象，
4.子类调用父类构造器
5.构造器执行，构造器按照定义顺序执行实例变量初始化

这里先详细解释元信息是什么
如果打开class文件，以二进制，有字段，构造器，方法
权限修饰符，类名，参数化类型，接口，注解
至于另外的，例如使用字段，获取方法，使用方法的
这些个东西，可以好好想一想为什么要传入那么多的数据
像字段那样直接点不好么，想想看

类型不能传递，我们只能传递值，要么传递对象
 */
