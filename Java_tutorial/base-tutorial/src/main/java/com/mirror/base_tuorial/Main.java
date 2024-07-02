package com.mirror.base_tuorial;

public class Main {
    public static void main(String[] args) {
        int x = 100;
        System.out.println(x);
        x = 200;
        System.out.println(x);
        /*
        这里是重新赋值，并打印用的
         */
        //在这里有个有意思的点记下
        int a = 100;
        System.out.println(a);
        a = 200;
        System.out.println(a);
        int b = a;
        System.out.println(b);
        b += 100;
        System.out.println(b);
        System.out.println(a);
        /*
        从这里得到什么结论呢，
        int类型是可变的，每一个变量名在物理内存上代表一个实际位置
        所以取用是无关的。
        也就是说现在在内存中有两份物理地址，a,b这两份。
        那这里强调肯定是有原因。
        而在这里引入基本数据类型

        整数类型：byte(1B)，short(2B)，int(4B)，long(8B)
        浮点数类型：float(4B)，double(8B)
        字符类型：char(2B)
        布尔类型：boolean(4B/1B)单独4B。数组1B
        这里又引出这些能表示的范围是多大，没必要记，本身就是算出来的
        byte：-128 ~ 127
        short: -32768 ~ 32767
        int: -2147483648 ~ 2147483647
        long: -9223372036854775808 ~ 9223372036854775807
         */
        //定义整形
        int i = 214783647;
        int i2 = 2_000_000_000;
        int i3 = 0xff000;//十六进制
        int i4 = 0b1001010;//二进制
        long n1 = 9000000L;//long型要加L
        long n2 = 900;//int 类型可以赋值给long.反着不行
        //定义浮点型
        float f1 = 3.14f;
        float f2 = 3.14e38f;
        double d1 = 1.79e308;
        double d2 = 3.14f;//float可以赋值给double。
        //这些能不能转型赋值，主要是看位数
        //boolean
        boolean b1 = true;
        boolean b2 = false;
        //char
        char a1 = 'A';
//        char a2 = '中';

        //除此之外就是引用类型
        String s1 = "hello";
        /*
        这里把类型全部区分开
        基本类型都是一个变量代表内存中实际的物理内存
        引用类型就是指针，变量名存储的是物理内存地址信息，说是指针也成。
        字节类型和字符串类型的使用方式要区分好
         */
        //既然有变量就会有常量
        final double PI = 3.14;
        /*
        常亮就是加了修饰符，表示不可修改了。所以常量赋值的位置也是很有讲究
         */
    }
}