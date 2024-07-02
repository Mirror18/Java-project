package com.mirror.base;

import lombok.Data;

/**
 * @author mirror
 */
public class InterfaceA {
    public static void main(String[] args) {
        InterfanceB aa = new InstanceB("mirror");
        System.out.println(aa.getName());
        aa.run();
        InterfanceB.aa();

        /*
        所以抽象类和接口的区别在哪呢
        抽象类虽然是抽象方法，但可以有字段
        抽象类中可以包含具体方法和构造方法。通过可以加上@Data注解就可以发现。不过里面是要包含抽象方法的

        接口就只能包含抽象方法
        虽然也可以包含静态方法和默认方法吧
        但是不能包含具体方法和字段。
        省略了abstract static这些个关键字，加上也无伤大雅，虽然没啥用
        当然接口也是可以继承的，抽象程度进一步加深。
        静态方法跟默认方法有什么异同，其实就是都不用子类可以去复写
        Default方法是可以通过实例名字调用
        static 方法只能通过接口名调用
         */
        aa.bb();
//        InterfanceB.bb();
        /*
        无法通过接口名调用
         */
        /*
        但这里有个问题，就是默认方法
        首先默认方法一般是只存在于接口之中
        通过了解
        继承类只能有一个
        接口可以有无数个
        接口的默认方法怎么办，方法声明可不管
        那么在继承多个接口
        关于默认方法方法声明都一样。
        那只有重写
        重写的规则也很简单

         */
//        @Override
//        public void method(){
//        InterfanceA.super.method();
//        InterfanceB.super.method();
//    }
        /*
        就这样姐可以重新使用默认方法了
         */
    }
}

interface InterfanceB {
    void run();

    String getName();

    static void aa() {
        System.out.println("that is interface static method");
    }

    default void bb() {
        System.out.println("that is interface default method");
    }
}

@Data
class InstanceB implements InterfanceB {
    private String name;

    public InstanceB(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(this.name + " run");
    }


}