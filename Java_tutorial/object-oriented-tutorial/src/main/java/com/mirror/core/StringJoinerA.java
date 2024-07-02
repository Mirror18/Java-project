package com.mirror.core;

import java.util.StringJoiner;

/**
 * @author mirror
 */
public class StringJoinerA {
    public static void main(String[] args) {
        /*
        在前面的最后一个留了一个坑，
        用String.join尝试拼接字符出错。
        因为是面对单独一个字符串，
        但join是要多个字符串，所以出错了。
        但是这里的StringJoiner是干什么的
        实话说跟上面的方法一个性质
        都是将多个字符串用分隔符组成一个字符串
        但估计是太常用了吧，所以把静态方法再做了一个类
        也就是拼接多个字符串可以玩出花样来了
         */
        String[] names = {"Bob", "Alice", "Grace"};
        var sj = new StringJoiner(", ", "Hello ", "!");
        for (String name : names) {
            sj.add(name);
        }
        System.out.println(sj.toString());
        /*
        只能说不指定开头和结尾的时候也特别好用

        这里是简单作出一个面对对象和面对过程的一个解释具体解释吧。
        面对过程大概上可以说是我需要什么函数就造出什么函数
        面对对象就是，可以说是一组方法的集合
         */
        System.out.println(String.join(", ",names));
    }
}
