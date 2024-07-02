package com.mirror.proxy;

import com.mirror.Impl.CalculatorImpl;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InterfaceTest {
    /*
    前面通过静态代理，成功的将接口实现的子类添加上了增强代码
    但是不是偏题了，我记得我们要实现的是如何实例化一个接口
    得到的结论是通过动态代理

    那在这个测试类中输出接口所有的信息呗，就像反射的时候做的事

    通过以下的输出，我们知道一件事，那就是接口和实现类基本相似
    但无法常见接口对象，也是因为没有构造器

    所以动态代理，就是为了给接口装上构造器

    本质上用Class造Class,用接口Class创造一个代理类的Class
    Proxy.getProxyClass()返回代理嘞的Class对象

    自动生成代理对象
    将增强代码和代理对象解耦，从而代码服用
     */
    @Test
    public void testInterfaceDetail(){
        /*
        这里不是创建了一个Class对象，而是让JVM加载并创建Class对象
         */
        Class<Calculator> calculatorClazz = Calculator.class;
        Constructor<?>[] calculatorClazzConstructors = calculatorClazz.getConstructors();
        Method[] calculatorClazzMethods = calculatorClazz.getMethods();
        System.out.println("interface class for constructor");
        printClassInfo(calculatorClazzConstructors);
        System.out.println();
        System.out.println("interface class for method");
        printClassInfo(calculatorClazzMethods);
        System.out.println();

        /*
         * Calculator实现类的Class对象
         */
        Class<CalculatorImpl> calculatorImplClazz = CalculatorImpl.class;
        //Calculator实现类的构造器信息
        Constructor<?>[] calculatorImplClazzConstructors = calculatorImplClazz.getConstructors();
        //Calculator实现类的方法信息
        Method[] calculatorImplClazzMethods = calculatorImplClazz.getMethods();
        //打印
        System.out.println("------Impl cLass for constructor------");
        printClassInfo(calculatorImplClazzConstructors);
        System.out.println("\n");
        System.out.println("------Impl class for Method------");
        printClassInfo(calculatorImplClazzMethods);
        System.out.println();

        Class<?> calculatorProxyClazz = Proxy.getProxyClass(Calculator.class.getClassLoader(),Calculator.class);
        System.out.println(calculatorProxyClazz.getName());
        System.out.println(CalculatorImpl.class.getName());
        Constructor<?>[] constructors = calculatorProxyClazz.getConstructors();
        System.out.println("----constructor----");
        printClassInfo(constructors);
        System.out.println("");
        //打印代理Class对象的方法
        Method[] methods = calculatorProxyClazz.getMethods();
        System.out.println("----method----");
        printClassInfo(methods);
        System.out.println();
    }

    private void printClassInfo(Executable[] targets) {
        for (Executable target:targets
             ) {
            String name = target.getName();
            StringBuilder sBuilder = new StringBuilder(name);

            sBuilder.append('(');
            Class<?>[] clazzParams = target.getParameterTypes();

            for(Class<?> clazzParam : clazzParams){
                sBuilder.append(clazzParam.getName()).append(',');

            }
            if(clazzParams.length != 0){
                sBuilder.deleteCharAt(sBuilder.length()-1);

            }
            sBuilder.append(')');
            System.out.println(sBuilder.toString());
        }
    }

}
