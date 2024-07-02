package com.mirror.proxy;

import com.mirror.Impl.CalculatorImpl;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class EnhancementTest {
    /*
    虽然已经实现了两个，但其实还能再增强
    增强到什么地步呢，
    还记得最开始写的动态代理的一个例子么。
    里面用的不是Proxy.getProxy
    而是Proxy.newProxyInterface
    所以这一步就相当于是仿写了，写出自己的
     */
    @Test
    public void testProxy(){
        CalculatorImpl target = new CalculatorImpl();
        InvocationHandler logInvocationHandler = getLogInvocationHandler(target);
        Calculator calculator = null;
        try {
            calculator = (Calculator) getProxy(target,logInvocationHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert calculator != null;
        calculator.add(1,2);

    }
    private Object getProxy(final Object target,final InvocationHandler handler)throws Exception{
        Class<?> proxyClazz = Proxy.getProxyClass(target.getClass().getClassLoader(),target.getClass().getInterfaces());
        Constructor<?> constructor = proxyClazz.getConstructor(InvocationHandler.class);
        return constructor.newInstance(handler);
    }
    private InvocationHandler getLogInvocationHandler(final Object target){
        return new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName()+" start...");
                Object result = method.invoke(target,args);
                System.out.println(result);
                System.out.println(method.getName()+" end...");
                return result;
            }
        };
    }

    @Test
    public void testProxy1(){
        CalculatorImpl target = new CalculatorImpl();
        // 2.传入目标对象，得到增强对象（如果需要对目标对象进行别的增强，可以另外编写getXxInvocationHandler）
        InvocationHandler logInvocationHandler = getLogInvocationHandler(target);
        // 3.传入目标对象+增强代码，得到代理对象（直接用JDK的方法！！！）
        Calculator calculatorProxy = (Calculator) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),   // 随便传入一个类加载器
                target.getClass().getInterfaces(),    // 需要代理的接口
                logInvocationHandler                  // 增强对象（包含 目标对象 + 增强代码）

        );
        calculatorProxy.add(1, 2);
    }
    /*
    然后就会神奇的发现，这俩货一模一样
    所以动态代码结束

    顺便完结静态代码，虽然实话说，用不到这么多东西
    但也就这样了，总好过死记硬背来完成动态代理
    这一块我跟着内容也是敲了四个小时吧
    还不算上提前看过几遍
    所以这一块，务必，请求，手敲一遍。
    就是写了一遍才发现里面简直了
     */
}
