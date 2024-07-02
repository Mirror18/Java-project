package com.mirror.base_tuorial;


public class Operate {
    public static void main(String[] args) {
        //整数运算
        //四则运算
        int i = (10 + 200) * (99 - 90);
        System.out.println(i);
        //相除，两种，一个取商，一个取余
        System.out.println(12345 / 67);
        System.out.println(12345 % 67);
        //另外除数为零是运行错误。需要自己捕捉异常
        //溢出,没有写数值类型就是默认的int类型
        System.out.println(2147483640 + 15);
        //这里用了隐形类型转换
        System.out.println(2147483640 + 15L);

        //移位云算
        System.out.println(7 << 1);
        /*
        7的二进制0111，
        14的二进制1110.
        同时又知道int为4B也就是32位，
        所以把第3位的移到第32位，也就是需要29
         */
        System.out.println(7 << 29);
        /*
        这是算数移位，也就是负数移位右移位补1.其他情况都是补0
         */
        /*
        无符号运算
         */
        System.out.println(-1 >>> 1);
        /*
        -1的二进制11111111111111，无符号右移01111
         */
        /*
        byte 和short是转换为int进行位移
         */
        //位运算
        //与或非
        System.out.println(0 & 1);
        System.out.println(0 | 1);
        System.out.println(~1);
        /*
        关于类型提升和强制转型，前面long的使用就表示了类型提升
        强制转型就是(short) 1234
         */
        System.out.println((short)12345.0);

        /*
        浮点数运算，只有四则运算，没有移位运算。
        关于这个理解就是关于浮点数如何在内存中存入的方式作为解释
        整形和浮点型，运算就是提升到浮点型
        溢出没有除数为0时的报错，就是会返回几个特殊值
         */

        /*
        boolean运算
        只有比较运算，和与或非运算
        &&
        ||
        !
        至于短路云算，则是根据与或非运算做的优化，与是只有两个全为true
        或是只要一个为true即可。
        所以只要第一个为true的与运算就为true
        一个为false的与就是false
        三元运算符 b ? x: y就是b为true就x,false为y
         */

        /*
        字符类型和字符串
        字符类型说是保存的一个unicode字符，

         */
        System.out.println((char)'A');
        System.out.println((int)'A');
        /*
        所以同样一份数据，取出的方式不同，显示的就不同
         */
        /*
        字符串类型也可以说是character的数组吧,不过使用方式却不一样
         */
        int age = 18;
        System.out.println("age is " + age);
        String s = """
                SELECT * FROM
                    users
                WHERE id > 100
                ORDER BY name DESC
                """;
        System.out.println(s);

        /*
        另外就是不可变性，跟上述基本类型的时候用a 和 b 进行赋值的时候
        赋值完毕之后成为两个数据
         */
        String c = "hello";
        String d = c;
        c = "world";
        System.out.println(c);
        System.out.println(d);
        /*
        同样是赋值操作，这两个结果就变了，基本类型是不变
        引用类型是改变了。所以这也是在说明指向，c && d 只是存的地址
        另外就是空值null的空字符串的事
        这里就可以明白，当为null，代表村的地址是空的
        但是赋值空字符串。还是实际的物理地址，只不过这个物理地址内部是空的
         */
    }
}
