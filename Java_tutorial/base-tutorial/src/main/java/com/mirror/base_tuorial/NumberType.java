package com.mirror.base_tuorial;

public class NumberType {
    public static void main(String[] args) {
        System.out.println(new int[5].length);
        var ns1 = new int[] {15,78,46,45,21};
        System.out.println(ns1.length);
        /*
        数组是引用类型，所以
         */
        String[] names  ={ "ABC","DEF"};
        var s = names[1];
        names[1] = "cat";
        System.out.println(s);
        System.out.println(names[1]);
        /*
        这里表达了三种创建数组的方式
        同时还用到了var 这个关键字
        这个关键字是IDE进行编译的时候自动填充的
        关于编译和运行，主要是到注解的时候有一个深刻的认知。
        关于编译就是打开这个编译的结果，就会发现var 填充的会自动转换成应该写的类型
         */
    }
}
