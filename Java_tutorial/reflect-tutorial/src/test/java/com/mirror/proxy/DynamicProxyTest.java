package com.mirror.proxy;

import com.mirror.Impl.CalculatorImpl;
import org.junit.Test;

import java.lang.reflect.*;

/**
 * 通过接口测试的时候，已经获取到了所有的相关信息
 * 所谓代理类，就是通过Proxy构造一个带有构造器的接口实现类的类信息
 * 这时候就可以按照反射进行写入了
 *
 * 但是有没有好奇，生成的构造器的内容，会发现除了名字，还有一个入参InvocationHandler
 * 这里越来越像之前写的代理类的诶模样了，拿来开始吧
 */
public class DynamicProxyTest {
    /**
     * 所以干了什么事呢
     * 第一，先获取到给接口包装好的代理类，获取到他的类名
     * 然后就是反射使用构造器的事，获取到构造器
     * 然后构造器进行实现，获取到实例对象
     * 然后，就没了
     */
    @Test
    public void testDynamicProxy(){
        Class<?> calculatorProxyClazz = Proxy.getProxyClass(Calculator.class.getClassLoader(),Calculator.class);
        Constructor<?> constructor = null;
        try {
             constructor = calculatorProxyClazz.getConstructor( InvocationHandler.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Calculator calculatorProxyImpl = null;
        try {
            assert constructor != null;
             calculatorProxyImpl = (Calculator) constructor.newInstance(new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return 1111;
                }
            });
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assert calculatorProxyImpl != null;
        System.out.println(calculatorProxyImpl.add(1,2));
    }
    /**
     * 那样就剩唯一的难点没解决了，就是InvocationHandler是什么，然后每次还都重写
     *
     * 那这里需要解释Proxy的来历，
     * JDK动态代理最终生成的其实是Class<Proxy>，最终代理对象是proxy对象，而且实现了Calculator接口。
     *
     * 所以接口还是被实现了，但是是交给内部去玩了
     * 那么静态和动态的区别
     * 静态代理的做法是吧目标对象传到代理对象
     * 动态代理则是把增强代码传入代理对象
     *
     * 怎么这样说，首先是静态代理，是实现了子类，但是需要给子类的方法内部增加拓展代码
     * 做法就是创建个代理类，代理类内部传入已经实现的子类对象。所以叫目标对象传入代理对象
     *
     * 那么动态代理的是怎么说，
     * 关键就在于调用构造器生成实例对象的时候，重写的invoke方法。
     * ● Object proxy：很遗憾，是代理对象本身，而不是目标对象（不要调用，会无限递归，一般不会使用）
     * ● Method method：方法执行器，用来执行方法（有点不好解释，Method只是一个执行器，传入目标对象就执行目标对象的方法）
     * ● Obeject[] args：方法参数
     * 关键就在这之上，
     *
     * 我们之前学过反射的方法，如果获取方法对象还行，但是方法的实现是需要指定哪个类去实现
     * 也就是说少个对象.当然要是不写也成，因为是接口，本来就没方法体，直接重写也是问题不大
     * 但对于拓展来说，再重新写一遍原始代码是真痛苦。所以一般来说需要传入一个可以调用的子类
     */
    @Test
    public void testDynamicProxy1(){
        Class<?> calculatorProxyClazz = Proxy.getProxyClass(Calculator.class.getClassLoader(),Calculator.class);
        Constructor<?> constructor = null;
        try {
            constructor = calculatorProxyClazz.getConstructor( InvocationHandler.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Calculator calculatorProxyImpl = null;
        try {
            assert constructor != null;
            calculatorProxyImpl = (Calculator) constructor.newInstance(new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    CalculatorImpl calculatorImpl = new CalculatorImpl();
                    System.out.println(method.getName()+"method start");
                    Object result = method.invoke(calculatorImpl,args);
                    System.out.println(result);
                    System.out.println(method.getName()+"method end");
                    return result;
                }
            });
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assert calculatorProxyImpl != null;
        System.out.println(calculatorProxyImpl.add(1,2));
        /*
       虽然不好看吧，不过也是凑合用了
         */
    }
    /*
    所以这里优化下
     */
    @Test
    public void testDynamicProxy2(){
        CalculatorImpl target = new CalculatorImpl();
        Calculator calculator = (Calculator) getProxy(target);
        assert calculator != null;
        calculator.add(1,2);

    }
    private Object getProxy(final Object target){
        Class<?> ProxyClazz = Proxy.getProxyClass(target.getClass().getClassLoader(),target.getClass().getInterfaces());
        Constructor<?> constructor = null;
        try {
            constructor = ProxyClazz.getConstructor(InvocationHandler.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            assert constructor != null;
            return constructor.newInstance(new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println(method.getName()+"start");;
                    Object result = method.invoke(target,args);
                    System.out.println(result);;
                    System.out.println(method.getName()+"end");
                    return result;
                }
            });
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
