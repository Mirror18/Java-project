package com.mirror.reflect;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author mirror
 */
public class DynamicProxy {
    public static void main(String[] args) {
        /*
        关于动态代理，实话说演示非常简单，
        但是要详细解释其中原理的话就十分蛋疼

        这里就先做一个简易的吧

         */
        /*
        首先我们要明白的是
        可以实例化class(非abstract)
        不能实例化interface
        所有interface类型的变量都是通过某个实例向上转型并赋值给接口类型变量的
         */
        CharSequence cs = new StringBuilder();

        /*
        那我们要通过动态代理干什么呢，不编写实现类
        直接在运行期创建某个interface的实例
         */
        Hello hello = new HelloWorld();
        hello.morning("mirror");
        /*
        就是有点纯，但是如果你跟着代码走的话，就是一步一步编写的
        就会发现
         */
        Hello hello1 = new Hello() {
            @Override
            public void morning(String name) {
                System.out.println("hi");
            }
        };
        hello1.morning("cat");
        /*
        还有这种写法，这应该叫内部类
        或许吧，内部匿名类
        那么动态代理呢
         */
        //实现接口方法的调用
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method);
                try {
                    if("morning".equals(method.getName())){
                        System.out.println("Good morning. that is dynamic, " + args[0]);
                    }
                }catch (NullPointerException ignored){}
                return null;
            }
        };
        /*
        通过Proxy.newProxyInstance()创建interface实例，它需要3个参数：
        使用的ClassLoader，通常就是接口类的ClassLoader；
        需要实现的接口数组，至少需要传入一个接口进去；
        用来处理接口方法调用的InvocationHandler实例。
        将返回的Object强制转型为接口。
         */
        Hello hello2 = (Hello)  Proxy.newProxyInstance(
                Hello.class.getClassLoader(),
                new Class[] {Hello.class},
                handler
        );
        hello2.morning("cat");
    }
}
/*
首先就是普通的调用
 */
interface Hello{
    void morning(String name);
}

class HelloWorld implements Hello{
    @Override
    public void morning(String name){
        System.out.println("Good morning. " + name);
    }
}

/**
 * 如果说将上面的动态代理转换为静态实现类
 * 大致长这样
 *
 */
class HelloDynamicProxy implements Hello{
    InvocationHandler handler;
    public HelloDynamicProxy(InvocationHandler handler){
        this.handler = handler;
    }

    @Override
    public void morning(String name) {
        try {
            handler.invoke(
                    this,
                    Hello.class.getMethod("morning", String.class),
                    new Object[]{name}
            );
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}