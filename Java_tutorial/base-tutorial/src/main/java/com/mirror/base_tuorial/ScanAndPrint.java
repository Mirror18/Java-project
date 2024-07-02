package com.mirror.base_tuorial;

import java.util.Scanner;

/**
 * @author mirror
 */
public class ScanAndPrint {
    /*
    主要是if ，for， while 循环
     */
    /*
    首先介绍如何输入和输出，scan和print
     */
    //输出

    /**
     * 没啥吊用
     * @param args
     */
    public static void main(String[] args) {
        System.out.print("a");
        System.out.println("b");
        System.out.printf("%.2f\n", 3.1415926);
        System.out.printf("%% %08x\n", 31415926);
    /*
    输入就麻烦了很多
     */
        var scanner = new Scanner(System.in);
        System.out.println("Input your name: ");
        String name = scanner.nextLine();
        System.out.println("input your age: ");
        int age = scanner.nextInt();
        System.out.printf("hi,%s,you are %d\n",name,age);
    }

}
