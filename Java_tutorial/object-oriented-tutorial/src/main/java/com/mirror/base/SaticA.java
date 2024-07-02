package com.mirror.base;

import lombok.Data;

/**
 * @author mirror
 */
public class SaticA {
    public static void main(String[] args) {
        Person1 ming = new Person1("Xiao Ming", 12);
        Person1 hong = new Person1("Xiao Hong", 15);
        ming.number = 88;
        System.out.println(hong.number);
        hong.number = 99;
        System.out.println(ming.number);
        /*
        就会发现会变，这就是下面说的共享区域

        至于静态方法的演示，前面已经演示很多次了
        这里强调的是，静态方法属于class而不是实例，说一静态方法内部无法访问this变量
        无法访问实例字段，只能访问静态字段
         */

        /*
        静态方法主要用于工具类
        例如排序
        Arrays.sort()
        获取随机数
        Math.random()
        还有最关键的一点Java的入口程序main
         */

        /*
        吐槽一句，接口中虽然不包含字段，但是可以包含静态字段。
        为什么呢，跟静态方法一样。要是不加static，那这些玩意儿存哪呢，
         */
    }
}

@Data
class Person1 {
    public String name;
    public int age;
    public static int number;

    public Person1(String name, int age) {
        this.name = name;
        this.age = age;
    }
    /*
    实例字段在每个实例中都有自己的一个独立空间。也就是在堆中
    但静态字段和静态方法只有一个共享空间，也就是跟随类加载的元空间中
     */

    /*
    所以这里拓展下JVM各个空间的信息吧
    1. 堆内存
        所有对象实例
        数组
        堆内存是所有线程共享的取余
        堆内存是垃圾回收的主要管理区域
        当用new关键字时，内存分配在堆内存中
    2. 方法去
        已经加载的类信息
        静态变量
        常量池
        字节码，即方法代码
        方法区是所有线程共享的区域，现在一般成为元空间，并从堆内存中移出，使用本地内存
        类加载时，类的元数据存储地
        静态变量和常量池存放地
     3. 栈内存
        存放每个线程的栈帧
        局部变量表
        操作数栈
        方法返回地址
        动态链接
        栈内存是线程私有的区域
        每个方法调用都会创建一个新的栈帧，方法执行后栈帧销毁
     4. 程序计数器
        当前线程执行的字节码指令的地址
        程序计数器是线程的私有区域
        并且这里没有内存溢出错误
        主要是记录当前线程的执行位置，控制程序的执行流
     5.本地方法栈
        存储的是Native关键字的方法的栈帧
        是线程私有的区域
        累死Java栈，但是用于本地方法
        执行本地方法时，方法的局部变量和结果存储在本地方法栈中

     */
    /*
    堆内存示例：

public class HeapMemoryExample {
    public static void main(String[] args) {
        // 创建对象实例，存储在堆内存中
        MyClass obj1 = new MyClass();
        MyClass obj2 = new MyClass();
    }
}
方法区示例：

public class MethodAreaExample {
    // 静态变量，存储在方法区
    private static int staticVariable = 10;

    public static void main(String[] args) {
        // 静态方法调用，方法代码存储在方法区
        staticMethod();
    }

    public static void staticMethod() {
        System.out.println("Static method");
    }
}
栈内存示例：
public class StackMemoryExample {
    public static void main(String[] args) {
        int localVariable = 5; // 局部变量，存储在栈内存中
        methodA(localVariable);
    }

    public static void methodA(int param) {
        int anotherLocal = param * 2; // 局部变量，存储在栈内存中
        System.out.println(anotherLocal);
    }
}
     */
}