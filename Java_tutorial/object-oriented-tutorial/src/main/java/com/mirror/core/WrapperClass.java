package com.mirror.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class WrapperClass {
    public static void main(String[] args) {
        /*
        这里是包装类
        怎么说呢，就是吧基本类型转换为引用类型。
        再一次对面对对象进行一个解释吧
        好好的一个基本类型为什么要转换为包装类，也就是一个引用类型
        Integer类只包含一个实例字段int.
        这不纯纯找事么，实际上也差不多是找事吧
        因为Java的通用方法编写也基本上是都是面对类的，要想通用只能委屈下这些基本类型
        或许光说没有什么概念
        还记得前面的ArrayList么
         */
        ArrayList<Integer> arrayList = new ArrayList<>();
        int size = (int) (Math.random() * 10);
        int n1 = Math.toIntExact(System.currentTimeMillis() % 100);
        int n2 = new Random().nextInt(100);
        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) {
                arrayList.add(n1);
            } else {
                arrayList.add(n2);
            }
        }
        System.out.println(arrayList);
        Integer[] integer = arrayList.toArray(new Integer[0]);
        int[] ints = Arrays.stream(integer).mapToInt(Integer::intValue).toArray();
        System.out.println(arrayList.getClass());
        System.out.println(integer.getClass());
        System.out.println(ints.getClass());
        System.out.println(Arrays.toString(ints));
        /*
        这些就是我们之前定义的关于类型之间的转换
        首先是用列表，这里是arraylist，就是可变长列表，因为是对象，在内存中不是连续存储的
        所以链表，但是实际上的链表是linkedlist这个才是内存链式存储

        Integer则是包装类，或许已经发现为什么会有包装类的存在，因为arraylist是针对引用类型的操作

        那么包装类和基本类型的转换需要这么麻烦么，还调用了stream流API，
        或者说本质上调用这个API也是for循环赋值，

        至于单独的，可以随意转换，有自动解包和装包
         */
        /*
        看一下Integer的定义就知道了
        抛开各种复杂的静态方法之外
        其实本质就是
        public class Integer {
    private int value;

    public Integer(int value) {
        this.value = value;
    }

    public int intValue() {
        return this.value;
    }
}
所以明白这是怎么装包和解包的吧
         */
        /*
        一般操作就是
        Integer n = null;
Integer n2 = new Integer(99);
int n3 = n2.intValue();

但实际情况是可以两个自己赋值就行
         */

        int i = 100;
        // 通过new操作符创建Integer实例(不推荐使用,会有编译警告):
//        Integer n3 = new Integer(i);
        // 通过静态方法valueOf(int)创建Integer实例:
        Integer n4 = Integer.valueOf(i);
//        Integer n4 = i;
        // 通过静态方法valueOf(String)创建Integer实例:
        Integer n5 = Integer.valueOf("100");
//        int n5 = Integer.valueOf("100");
        System.out.println(n5.intValue());
        /*
        下方隐藏的两条语句就是自动装箱和拆箱的方法
        这些只是发生在变异阶段
         */
        /*
        然后就是关于不变类
        还有类的引用，可以说跟String一样
        同时对Integer也是做了优化。但只是针对127以下的数
         */
        /*
        另外也可以注意到上面还有一个给忽略了
        就new Integer的操作。忽略的原因呢也很简单。
        jdk不支持了
        那不支持了怎么创建实例
        这就是静态工厂了
        下方的valueOf，如果去看源码，就会发现内部还是new了一个
        那这样累不累，确实挺累，但是为了优化，不寒蝉

        这种方式就是静态工厂方法，尽可能的返回缓存实例用于节省内存

        所以一个警告，创建的时候有限静态工厂方法，鬼知道什么时候一个优化就没了
         */

        /*
        然后就是进制转换
        前面的String的时候好像就说过，
        parseInt，本质上就是String转换为基本类

        至于关于整数格式转换为指定进制的字符串，看着玩就行

        不过这里有一个重要的设计原则
        数据的存储和显示要分离。不能说是可以输出八进制和十六进制，就存为这些，本质上还是二进制
         */

        /*
        另外就是所有整数和浮点数的包装类都是继承于Number,
        还记得前面的向上转型和乡下转型么
         */
        // 向上转型为Number:
        Number num = Integer.valueOf(999);
// 获取byte, int, long, float, double:
        byte b = num.byteValue();
        int n = num.intValue();
        long ln = num.longValue();
        float f = num.floatValue();
        double d = num.doubleValue();
        /*
        点进number的源码看看
        还有integer的源码也看看
        然后就会发现只是把int的给强转了
         */

        /*
        至于无符号整型

        但是在Java中没有无符号整型的基本数据类型
        所以想要无符号整型就需要借助包装类
         */
        byte x = -1;
        byte y = 127;
        System.out.println(Byte.toUnsignedInt(x)); // 255
        System.out.println(Byte.toUnsignedInt(y)); // 127

        /*
        另外就是装箱和拆箱实惠影响执行效率，并且拆箱会发生NullPointerException
        原因就是赋值的时候引用类型可以为null
        基本类型是0，所以这才是问题所在
         */
    }
}
