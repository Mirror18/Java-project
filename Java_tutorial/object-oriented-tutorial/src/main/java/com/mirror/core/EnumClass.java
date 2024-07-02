package com.mirror.core;

import lombok.Getter;

/**
 * @author mirror
 */
public class EnumClass {
    /*
    关于枚举类的介绍
    可以说可以很简单，也可以很复杂
     */

    /**
     * 主要运行程序
     *
     * @param args
     */
    public static void main(String[] args) {
        /*
        枚举类可以是一个JavaBean，也可以是单独的一个类，实现方式挺复杂的
         */
        /*
        下方的Weekday可以说是简单的常亮定义一组枚举值
        单据是有个很严重的问题，编译器无法检测每个值的合理性
        例如weekday定义的是0到6，如果输入七会怎么样
         */
        if (Weekday1.FRI == 7) {
        }
        /*
        而且下方定义的常亮仍然可以于其他变量比较，因为就相当于是一个常量
        但是这种随意调用会产生误解
         */

        /*
        使用枚举类
         */
        Weekday day = Weekday.SUN;
        if (day == Weekday.SAT || day == Weekday.SUN) {
            System.out.println("Work at home!");
        } else {
            System.out.println("Work at office!");
        }
        /*
        和常亮比较，使用enum定义枚举有一下好处

        首先就是enum常量本身带有类型信息
         */
//        if(Weekday.SUN==1){}
        /*
        这里就无法编译通过，可以放出来试试
         */

        /*
        不可能引用到非枚举的值
        不同类型的枚举不能互相比较或者赋值
         */
//        int x = Weekday.SUN;
        /*
        这里的比较可以用== 也可以用equals,因为i岁然是类，但是这是唯一实例
        所以可以用==
         */

        /*
        定义的enum类型总是继承自java.lang.Enum，且无法被继承；
只能定义出enum的实例，而无法通过new操作符创建enum的实例；
定义的每个实例都是引用类型的唯一实例；
可以将enum类型用于switch语句。
         */
        System.out.println(Weekday.SUN.name());
        System.out.println(Weekday.SUN.ordinal());

        System.out.println(Weekday.SUN.dayValue);


        Weekday day1 = Weekday.MON;
        if (day1.dayValue == 6 || day1.dayValue == 0) {
            System.out.println("Today is " + day + ". Work at home!");
        } else {
            System.out.println("Today is " + day + ". Work at office!");
        }

        switch(day1) {
            case MON:
            case TUE:
            case WED:
            case THU:
            case FRI:
                System.out.println("Today is " + day + ". Work at office!");
                break;
            case SAT:
            case SUN:
                System.out.println("Today is " + day + ". Work at home!");
                break;
            default:
                throw new RuntimeException("cannot process " + day);

//                var a = new Enum<String>();
//                var b = new Class<>();
                /*
                这里可以把这俩放出来看看源码怎么写的
                 */

        }
    }
}

/**
 * 简单的类
 * 可以说就是定义了一些常量
 */
class Weekday1 {
    public static final int SUN = 0;
    public static final int MON = 1;
    public static final int TUE = 2;
    public static final int WED = 3;
    public static final int THU = 4;
    public static final int FRI = 5;
    public static final int SAT = 6;
}

/**
 * 这里是星期天
 */
@Getter
enum Weekday {
    //星期的枚举
    MON(1, "星期一"),
    TUE(2, "星期二"),
    WED(3, "星期三"),
    THU(4, "星期四"),
    FRI(5, "星期五"),
    SAT(6, "星期六"),
    SUN(0, "星期日");

    public final int dayValue;
    private final String chinese;

    private Weekday(int dayValue, String chinese) {
        this.dayValue = dayValue;
        this.chinese = chinese;
    }

    /**
     * 书写toString是为了方便输出的内容
     */
    @Override
    public String toString() {
        return this.chinese;
    }
}
/*
虽然反编译的结果有点不太对，不过也凑合着看
一般情况下编译后的enum和普通的class没有任何区别
 */