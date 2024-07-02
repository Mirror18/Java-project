package com.mirror.core;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

public class StringAndCode {
    /*
    因为前面常用String。所以前面有所涉及

    这里需要强调基本类型和引用类型。class也是一个引用类型

    前面详细介绍过基本类型，也就是八种，这些都是直接存储他们的值，放入到栈内存中。。所以取出特别快

    引用类型就是包括class，interface，array和enum,也就是类，接口，数组，枚举
    特点就是存储的是对象的引用，也就是对象的内存地址，而不是对象本身
    对象的数据存放在堆内存中，引用变量本身存储在栈内存中

    也就是说在Java中，引用类型一共就四种，
    所以本身String也就是一个包含char[]数组的class类

     */
    String string = new String(new char[]{'H', 'e', 'l', 'l', 'o', '!'});
    /*
    上述是标准创建方式（去看了一眼源码，简直给看疯；额）
    有很多创建方式，不过这里就最常见的一种呗
     */
    String s1 = "Hello";
    /*
    或者把上面的给简化下
     */
    char[] chars = new char[5];
    String s2 = new String(chars);
    /*
    所以应该能分清，数组，类之间的区别了吧。
    虽然同样是引用类型，
    但数组本质上就是内存中的一串空间做集合，变量名就是指这个内存空间
    所以对数组的操作也就绕不过去取出和放入

    类呢，有了之前面对对象的基础，应该是明白，这些个模式是不一样的
     */

    /*
    那么关于类，那自然就可以重写和编写各种方法，
    这个软件包就是认识类用的，还有常见的核心类的使用方法
     */

    /*
    首先是确定字符串的不可变型，
    通过上述的创建方式，所以明白数据存放的类型是char[]，数组内容是可变得
     */
    public static void main(String[] args) {
        String string = "helloaa";
        System.out.println(string);
        string = string.toUpperCase();
        System.out.println(string);
        /*
        下方展示下如何访问外部的非静态字段
        因为main是静态方法，只能访问静态字段
        所以要想访问外部的string，就需要实例化当前类，并通过实例去访问非静态字段
         */
        System.out.println(new StringAndCode().string);
        /*
        这里会造成无限递归
         */
//        StringAndCode.main(new String[]{"mirror","cat"});

        /*
        字符串比较
         */
        /*
        其实主要的问题就是用== 是判断变量指向的值是否想等，
        但是对于引用变量就不可取，因为里面存的是内存地址
        或许有个优化
         */
        String s1 = "hello";
        String s2 = "hello";
        System.out.println(s1 == s2);
        ;
        /*
        这里是想等的，因为那是常量。做了优化
         */
        s1 = "hello";
        s2 = "HELLO".toLowerCase(Locale.ROOT);
        System.out.println(s1 == s2);
        /*
        正确的方式是
         */
        System.out.println(s1.equals(s2.toLowerCase(Locale.ROOT)));
        /*
        或者
         */
        System.out.println(s1.equalsIgnoreCase(s2));
        /*
        至于提取子串，
        因为方式很多，实在是懒得搞。例如
        "Hello".contains("ll");
        s1.substring(2);
        常用的可能就是
        去除首尾空白字符
        这个是返回一个新的字符串

         */
        System.out.println("\tHello\r\n".trim());
        /*
        还有一个可以做到取出空白字符
        但是会把中文的空格也给去除了
         */
        System.out.println(s1.strip());
        /*
        判断字符串是否为空和空白字符串
        isEmpty()
        isBlank()
        这里光说确实没啥用
        用的地方也不多，因为对于输入输出的数据一般来说，都是标准输入
        撑死就是爬虫的时候会多做区分
        还有调用api,
         */
        /*
        分割字符串
         */
        String[] s3 = s1.split("");
        System.out.println(Arrays.toString(s3));
        /*
        拼接字符串
         */
        String s4 = String.join("", s3);
        System.out.println(s4);

        /*
        格式化字符串
        其实就是printf的时候
        给换成println也能调用
         */
        String s5  ="hi %s,your score is %d!";
        System.out.println(s5.formatted("mirror",100));
        System.out.println(String.format("hi,%s,you are god","cat"));


        /*
        当然最主要的就是valueOf了，很常见，也很常用
         就是类型转换
         任意基本类型和引用类型转换为字符串
         */
        System.out.println(String.valueOf(123));
        System.out.println(String.valueOf(new Object()));
        /*
        或者字符串转换成其他类型
         */
        System.out.println(Boolean.parseBoolean("true"));
        System.out.println(Integer.parseInt("123"));
        /*
        都是套模板，但是有个点在Integer中
         */
        System.out.println(Integer.getInteger("java.version"));
        /*
        这个是把该字符串对应的系统变量转换过去
         */
        var integer =  Integer.getInteger("java.version");
        String ss = String.valueOf(integer);
        System.out.println(ss);
        /*
        尝试了几次，没法做到获取系统版本。
        因为现在系统版本不是整数
         */
        System.out.println(System.getProperty("java.version"));
        /*
        当然要是用
        System.setProperty("jva.version","21");
        倒是可以做到，没放出来主要是会污染系统属性
         */

        /*
        字符串转换为char[]

         */
        char[] cs = "hello".toCharArray();
        /*
        同时String的不可变型跟引用类型的不可变型不一样
        这里String通过char[]创建字符串的时候，是复制了一份新的到那里面，
        所以就是真正意义上的不可变，而不是引用类型的不可变。
        引用类型的不可变是传入的地址是内存地址
        然后外面的的变量传入进去的变量指向的地址变了，那自然是不关引用变量的事。
        但是String创建的时候，通过char[]创建，他是复制了一份过去。所以才会有不可变。跟内部代码有关
         */

        /*
        剩下的就是编码了
        因为熟知各种编码，就不多做介绍。主要是字节码的转换
         */
        System.out.println(Arrays.toString("hello".getBytes(StandardCharsets.UTF_8)));
        System.out.println(Arrays.toString("hello".getBytes()));
        /*
        在Java内部，String和char类型始终是以unicode编码表示
         */
        System.out.println(Arrays.toString("中国".getBytes(StandardCharsets.UTF_8)));
        System.out.println(Arrays.toString("中国".getBytes()));
        try {
            System.out.println(Arrays.toString("中国".getBytes("GBK")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /*
        如果有心，或许看过String的源码了。
        或许会对这个很好奇，coder.其实这个就是采用什么样的编码，目的是节省内存
         0 = LATIN1, 1 = UTF16
         */

    }

}
