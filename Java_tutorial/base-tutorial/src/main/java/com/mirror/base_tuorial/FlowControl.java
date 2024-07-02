package com.mirror.base_tuorial;

import java.util.Random;

/**
 * @author mirror
 */
public class FlowControl {
    public static void main(String[] args) {
        //创建随机数的三种方式
        int n = Math.toIntExact(System.currentTimeMillis() % 100);
        //或者
        int n1 = (int) (Math.random() * 100);
        var random = new Random(System.currentTimeMillis());
        int n2 = random.nextInt(100);
        System.out.printf("%d,%d,%d\n", n, n1, n2);
        //只是比较大小的两种方式
        int max = Math.max(n, n1);
        if (n2 >= max) {
            max = n2;
        }
        System.out.println("最大的值是" + max);
        int min;
        if (n <= n1) {
            min = Math.min(n, n2);
        } else {
            min = Math.min(n1, n2);
        }
        System.out.println("最小的值是" + min);

        //多级调用
        if (n <= n1) {
            System.out.println("n1最大");
        } else if (n1 <= n2) {
            System.out.println("n2最大");
        } else {
            System.out.println("n最大");
        }
        /*这个代码有问题，不过也就这样了，只是为了演示如何多条件
        同样的，这个例子也能反思，如果顺序放的不正确，就会有语句永远无法到达
         */
        double x = 1 - 9.0 / 10;
        if (Math.abs(x - 0.1) < 0.0001) {
            System.out.println("x is 0.1");
        } else {
            System.out.println("x is not 0.1");
        }
        /*
        根据上述浮点数的判别条件
        就可以得知，在计算机中有些类型是无法以相等判断的
        那么是不是可以想到引用类型。
         */
        String s1 = "hello";
        String s2 = "HELLO";
        System.out.println(s1);
        System.out.println(s2.toLowerCase());
        System.out.println(s1 == s2.toLowerCase());
        System.out.println(s1.equals(s2.toLowerCase()));
        /*
        因为这里涉及到equals
        有个问题，那就是s1如果为null 怎么办
        首先会抛出异常nullpointerexception。那么自然会有一个一丁不是null额字符串作为调用值
         */
        s1 = null;
//        System.out.println(s1.equals(s2));
        System.out.println("hello".equals(s1));

        /*
        switch 多重选择
         */
        String s = "mirror";
        switch (s) {
            case "mirror":
                System.out.println("that is mirror");
                break;
            case "cat":
                System.out.println("that is not ");
                break;
            default:
                System.out.println("hello");
                break;
        }
        /*
        或者这样写
         */
        switch (s) {
            case "mirror" -> {
                System.out.println("hello,mirror");
            }

            case "cat" -> System.out.println("that is not ");

            default -> System.out.println("hello");
        }

        String fruit = "apple";
        int opt = switch (fruit) {
            case "apple" -> 1;
            case "pear", "mango" -> 2;
            default -> 0;

        };
        System.out.println("opt = " + opt);

        String fruit1 = "orange";
        int opt1 = switch (fruit1) {
            case "apple" -> 1;
            case "pear", "mango" -> 2;

            default -> {
                yield fruit1.hashCode();
            }
        };
        System.out.println("opt1 =" + opt1);

        int random1 = (int) (Math.random() * 100);
        int random2 = Math.toIntExact(System.currentTimeMillis() % 100);
        int sum = 0;
        System.out.printf("random is %d and %d\n" , random1, random2);
        if(random2 > random1){
            while (random1< random2){
                sum+=random1;
                random1++;
            }
        }else if (random1> random2){
            while (random1>random2){
                sum += random2;
                random2++;
            }
        }else{
            sum=random1;
        }
        System.out.println("sum is" + sum);

        for (int i = random1; i < random2; i++){
            sum += i;
        }
        for(;random2<random1;random2++){
            sum+=random2;
        }

    }
}
