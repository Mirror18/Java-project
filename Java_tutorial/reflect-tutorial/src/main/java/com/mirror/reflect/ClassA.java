package com.mirror.reflect;

/**
 * @author mirror
 */
public class ClassA {
    /*
    什么是反射
    其实前面在写enum的util工具包的时候就用到了
    通过获取到当前传入的对象，获取到对象的字段信息，方法，构造器

    或者说为什么要这么麻烦，都获取到对象了。
    但如果获取到的只有对象呢，不知道字段，方法，构造器都是什么
    反射就这么用上了，通过获取到对象的类，然后列举字段，列举方法，
    然后运行。

    自己个人浅显理解，所谓反射，其实说白了还是为了泛型准备。
    作出一个通用的模板，

    解决在运行期间，对某个实例一无所知的情况下调用方法
     */

    /*
    大概思路就是类似之前写的enumUtil。获取到类
    然后用方法类进行装填，字段类装填。列举，匹配
     */

    /*
    首先要明确的。除了int等基本类型外，其他类型全部都是class
    而class本质上是数据类型。无继承关系的数据类型无法赋值

    class是JVM在执行过程中动态加载的。第一次读取到class类型时，将还在进内存
    每次加载一种class，就创建一个class类型的实例，并关联起来

    因为是数据类型，锁以都会包含该class的所有完整信息
     */
    public static void main(String[] args) {
        /*
        如何获取到一个class的Class实例
         */
        //第一种，是一个类
        Class cls = String.class;
        //第二种，是一个实例变量
        String s = "Hello";
        Class cls1 = s.getClass();
        //第三种，这里输入的是完整类名
        try {
            Class cls2 = Class.forName("java.lang.String");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //并且Class实例在JVM中是唯一的，所以可以用==
        System.out.println(cls==cls1);
        //关于类的比较，前面用过instanceof，但是这俩不一样，或者说instanceof还能比较子例
        System.out.println(s instanceof String);

        //获取到类的信息
        printClassTnfo("".getClass());
        String[] strings = new String[5];
        printClassTnfo(strings.getClass());
        try {
            printClassTnfo(Class.forName("java.lang.Math"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        printClassTnfo(int.class);

        //通过Class实例创建对应类型的实例
        try {
            String ss = (String) cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        //零外吐槽这个构建实例，
        /*
        首先我们知道的是，构造一个类的实例是通过构造方法
        但是我们会在后续有构造方法的获取，所以这个就显得很尴尬
        现在写出来也是说这个函数是被弃用的

        这玩意儿能干啥呢，只能调用public的无参构造
         */

        /*
        动态加载
        就是JVM加载类的时候，除非是程序要这个类，否则不会假爱
         */
    }

    static void printClassTnfo(Class cls){
        System.out.println("class name:" +cls.getName());
        System.out.println("simple name: " + cls.getSimpleName());
        if(cls.getPackage() != null){
            System.out.println("package nam: " + cls.getPackage().getName());

        }
        System.out.println("is interface: " + cls.isInterface());
        System.out.println("is enum: " + cls.isEnum());
        System.out.println("is array: " + cls.isArray());
        System.out.println("is primitive: "+cls.isPrimitive());
        System.out.println();
    }
}
