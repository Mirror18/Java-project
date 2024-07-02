package com.mirror.core;

public class StringBuilderA {
    /*
    这里主要讲的是字符串的拼接
     */
    public static void main(String[] args) {
        String s = "";
        for (int i = 0; i < 10; i++) {
            s =s + "," + i;

        }
        System.out.println(s.replaceFirst(",","").strip()+".");

        /*
        这是普通且常规的用法，确实好用。除了开销很大，每次都是新创建一个字符串对象。
        普通的该怎么办
         */
        StringBuilder s1 = new StringBuilder(1024);
        for (int i = 0; i < 10; i++) {
            s1.append(',');
            s1.append(i);
        }
        System.out.println(s1.deleteCharAt(0).append('.'));

        /*
        当然有没有好奇为什么可以多次用.进行调用下一个方法
        原因就在于return this。可以继续指向这个实例。这个是链式调用
         */

        /*
        上述问题处理完了，那就说点优化
        就是对于普通的字符串+，并不需要将其改写为StringBuilder，
        因为编译器在编译的时候会自动把+改为StringConcatFactory操作
        运行期间最自动八字富川的连接操作有华为数组复制或者StringBuilder操作。
        但这仅限于一个语句中，如果是循环语句，还是没有优化

        另外关于StringBilderu的另一个版本StringBuffer，这是一个线程安全版本
        通过同步保证多线程操作是安全的。
        但实际情况是这俩接口完全相同，没有必要使用StringBuffer
         */

//        String ss = "";
//        for (int i = 0; i < 10; i++) {
//            ss = String.join(ss,",");
//            ss = String.join(ss,String.valueOf(i));
//
//        }
//        System.out.println(ss);
        /*
        另外会考虑过join，但是这个是将多个字符串用一个分隔符连接起来
        所以本质上还是不一样
         */
    }
}
