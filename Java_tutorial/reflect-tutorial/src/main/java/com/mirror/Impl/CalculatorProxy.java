package com.mirror.Impl;

import com.mirror.proxy.Calculator;

/**
 * 所谓的静态代理买就是代理模式
 * 通过已经完成的接口，和接口实现的子类
 * 创建一个代理类，并传入实现子类
 * 添加拓展方式，并调用实现子类的构造方法
 * 成功的添加了拓展代码
 * 但无法解决的是代理嘞中的日志代码是重复的，虽然后面的AOP会解决这个问题
 * 但这是spring的内容，所以就是因果倒置，自然是先解决这个问题
 *
 * 优化在哪呢，就是我们不需要这个代理类，而是只想拥有这个代理对象，target
 * 虽然匿名类能解决这个问题，就是太麻烦
 *
 * 然后就是不像写这个代理类
 * @author mirror
 */
public class CalculatorProxy implements Calculator {
    private final Calculator target;

    public CalculatorProxy(Calculator target){
        this.target=target;
    }
    @Override
    public int add(int a, int b) {
        System.out.println("add method start...");
        int result = target.add(a,b);
        System.out.println("add method end... ");
        return result;
    }

    @Override
    public int subtract(int a, int b) {
        System.out.println("subtract method start...");
        int result = target.subtract(a,b);
        System.out.println("subtract method end... ");
        return result;
    }
}
