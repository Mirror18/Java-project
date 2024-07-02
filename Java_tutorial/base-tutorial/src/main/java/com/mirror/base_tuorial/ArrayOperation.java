package com.mirror.base_tuorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author mirror
 */
public class ArrayOperation {
    public static void main(String[] args) {
        /*
        生成一个可变数组，并且赋值
         */
        var list = new ArrayList<Integer>();
        for (int i = 0; i <= Math.random() * 10; i++) {
            list.add(i + 1);
        }
        /*
        检测复制的内容是否正确
         */
        for (int i : list) {
            System.out.print(i + " ");
        }
        System.out.println();
        /*
        将可变数组转换为Integer的数组
         */
        Integer[] ns = list.toArray(new Integer[0]);
        System.out.println(ns);
        //在这里可以看到包装类和基本类打印地址的时候有什么区别
        /*
        转换为int数组
         */
        int[] ns1 = Arrays.stream(ns).mapToInt(Integer::intValue).toArray();
        System.out.println(ns1);
        //这里展示下如何输出数组内容的方式
        System.out.println(Arrays.toString(ns1));
        for (int i = ns1.length - 1; i >= 0; i--) {
            System.out.print(ns1[i] + " ");
        }
        System.out.println();
        System.out.println("测试完成");

        /*
        有了以上的内容，可以制作自己的任意数组了
        1. 首先创建一个可变数组ArrayLIst
            然后在可变数组中进行赋值，但是这个赋值是调用了一个蝉蛹随机数的函数
            同时也说明如果想立马调用当前函数需要设置为静态的。

         */
        ArrayList<Integer> numberList = new ArrayList<>();
        int size = (int) (Math.random() * 10);
        for (int i = 0; i < size; i++) {
            int n = randomNumber();
            numberList.add(n);
        }
        /*
        这里尝试输出我们产生的随机数组的内容
        同时想要输出内存地址，但是无法获取
        于是产生哈西地址，这是JVM中识别这唯一地址的方式。进行一个模拟调用
         */
        System.out.println(numberList);
        System.out.println(System.identityHashCode(numberList));
        /*
        这里是把可变数组变为int数组。当然是偷懒了，
        本质上是分为两步
        1. 将ArrayList转换为Integer[]数组
        2.将Integer[]转换为int[]数组
        原因就是ArrayList是基于对象的集合
        int是基本数据类型
         */
        System.out.println("star to copy");
        int[] number = Arrays.stream(numberList.toArray(new Integer[0])).mapToInt(Integer::intValue).toArray();
        System.out.println(number);

        /*
        这是将数组copy一个新的数组
        原因是本身没写一个生成方法，所以每次使用的是同一个数组。（因为引用类型的不可变型）
        有4种方法。但最后一种是用for循环，取出和放入，太累
         */
        int[] number1 = Arrays.copyOf(number, size);
        System.out.println(Arrays.toString(number1));
        //第二种方法
        int[] number2 = new int[size];
        System.arraycopy(number, 0, number2, 0, size);
        System.out.println(Arrays.toString(number2));
        //第三种方法
        int[] number3 = number.clone();
        System.out.println(Arrays.toString(number3));
        System.out.println("start to order");
        for (int i = 0; i < number.length - 1; i++) {
            for (int j = 0; j < number.length - i - 1; j++) {
                if (number[j] > number[j + 1]) {
                    int tmp = number[j];
                    number[j] = number[j + 1];
                    number[j + 1] = tmp;
                }
            }

        }
        System.out.println("first");
        System.out.println(Arrays.toString(number));
        System.out.println("two");
        Arrays.sort(number1);
        System.out.println(Arrays.toString(number1));

        ArrayList<ArrayList<Integer>> towNumber = new ArrayList<>();
        int n1 = (int) (Math.random()*10);
        int n2 = new Random().nextInt(3);
        for (int i = 0; i < n1; i++) {
            towNumber.add(new ArrayList<>());
            for (int j = 0; j < n2; j++) {
                towNumber.get(i).add(randomNumber());
            }
        }
        System.out.println(towNumber);
        int[][] intArray2D = new int[n1][n2];
        for (int i = 0; i <n1 ; i++) {
            for (int j = 0; j <n2 ; j++) {
                intArray2D[i][j]=towNumber.get(i).get(j);
            }
        }
        System.out.println(Arrays.deepToString(intArray2D));
    }

    public static int randomNumber() {
        Random num = new Random();
        return num.nextInt(100);
    }
}
