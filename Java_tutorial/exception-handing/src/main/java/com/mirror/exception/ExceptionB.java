package com.mirror.exception;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ExceptionB {
    public static void main(String[] args) {

        byte[] bs = toGBK("中文");
        System.out.println(Arrays.toString(bs));


        /*
        或许会吐槽说这里不捕获也没啥问题啊
        原因就在getBytes的定义
        public byte[] getBytes(String charsetName) throws UnsupportedEncodingException {
        方法定义的时候，使用throws 表示该方法可能会跑出的异常类型，调用方必须捕获这些异常
         */
    }

    static byte[] toGBK(String s) {
        Exception origin = null;
        try {
            // 用指定编码转换String为byte[]:
            return s.getBytes("GBK");
        } catch (Exception e) {
            origin =e;
            // 如果系统不支持GBK编码，会捕获到UnsupportedEncodingException:
            System.out.println(e); // 打印异常信息
            return s.getBytes(); // 尝试使用用默认编码
        }finally {
            Exception e = new UnsupportedEncodingException();
            if(origin != null){
                e.addSuppressed(origin);
            }
            e.printStackTrace();
            System.out.println(Arrays.toString(e.getSuppressed()));
            System.out.println("that is must execution");
        }
    }
}
