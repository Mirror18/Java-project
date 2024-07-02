package com.mirror.exception;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author mirror
 */
public class ExceptionA {
    public static void main(String[] args) {
        String s = "abc";
        try {
            int n = Integer.parseInt(s);
        }catch (NumberFormatException e){
//            e.printStackTrace();
//            throw new IllegalArgumentException();
            System.out.println(e);
        }
        /*
        关于异常
        都是继承自Throwable
        氛围Error和Exception
        其中Error是我们不能捕获的
        exception又分为RuntimeExceptIon和IOException等
        必须捕获的异常，包括Exception及其子类，但不包括RuntimeException及其子类，这种类型的异常称为Checked Exception。

不需要捕获的异常，包括Error及其子类，RuntimeException及其子类。
        当然RuntimeException也可以捕获，只是不做要求
         */
        /*
        捕获方式就上面的try
         */
        try {
            byte[] bs = ("中文").getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e);
            var a =("中文").getBytes();
            System.out.println(Arrays.toString(a));
        }
        /*
        如果不捕获会出现编译失败
        同时有没有发现，如果这层捕获了异常
        但是下方的程序却没有运行，原因也很简单
        程序计数器当前不在这里

         */
    }
}
