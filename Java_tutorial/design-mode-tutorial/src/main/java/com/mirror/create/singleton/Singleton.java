package com.mirror.create.singleton;

/**
 * @author mirror
 */
public class Singleton {
    // 静态字段引用唯一实例:

    private static final Singleton INSTANCE = new Singleton();

    // private构造方法保证外部无法实例化:
    private Singleton() {
    }

    // 通过静态方法返回实例:
    public static Singleton getInstance() {
        return INSTANCE;
    }
}
