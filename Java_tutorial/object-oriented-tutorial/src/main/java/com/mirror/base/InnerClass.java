package com.mirror.base;

/**
 * @author mirror
 */
public class InnerClass {
    public static void main(String[] args) {
       Outer outer = new Outer("Mirror");
       Outer.Inner inner = outer.new Inner();
       inner.hello();
       inner.asyncHello();
        System.out.println(outer.getClass());
        System.out.println(inner.getClass());
        /*
        这里的输出顺序跟原本预想的有偏差
        不过这还是解释下线程的区别
        首先是inner.hello()运行
        输出Hello，aa
        然后是inner。asyncHello，
        这个语法是创建一个线程并加入运行。但现在是由主线程main调用
        但是在这之外还有一个获取到inner.runable的类名，所以就运行
        然后就是获取outer和inner的类名，在main主线程运行期间。所以开始运行输出
        等到主线程结束，开始调用其他县城，才会输出Hello，
         */
        /*
        不过本质上是想让明白普通类，内部类，内部类中的匿名类这些的编译的结果是什么样的。
        从这里就可以很清楚的看出来
         */

        /*
        关于静态内部类的书写
        或者说调用
         */
        Outer.StaticNested sn = new Outer.StaticNested();
        sn.hello();
        /*
        关于静态内部类和内部类的调用区别可以详细看下
        首先是静态内部类只能访问静态字段
        其次静态内部类的调用
        就会发现少了一个 new，至于少个入参括号，那是因为是调用Outer的静态方法
        用这个静态方法创建了一个类
         */
        Outer.Inner oi  =new Outer("cat").new Inner();
    }
}
class Outer{
    private String name;
    private static  int age;

    Outer(String name){
        this.name = name;
    }
    /*
    内部类有什么用呢
    实话说没啥用，除了访问private的字段
    那为什么要学呢
    因为内部类可以说没用过，
    但是匿名嘞太特么常见了
     */
    /**
     怎么评价内部类的功能呢
     这个就是内部类
     */

    class Inner{
        void hello(){
            System.out.println("hello,aa  " + Outer.this.name);
        }

        void asyncHello(){
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    /*
                    那普通的调用是这样的this.name
                     */
                    System.out.println("hello, " + Outer.this.name);
                }
            };
            new Thread(r).start();
            System.out.println(r.getClass());
        }
    }

    /*
        静态内部类
         */
    static class StaticNested{
        void hello(){
            System.out.println("hello,nested  " + Outer.age);
        }
    }
}