package com.mirror.base;

/**
 * @author mirror
 */
public  class AbstractA {
    /*
    这里是定义一些抽象类
     */

    public static void main(String[] args) {
        AbstractB abstractB = new InstanceA();
        abstractB.run();
        AbstractB.aa();
    }

    /*
    或许吐槽为什么要专门写一个空类去定义要实现的方法。
    虽然标准解释是，可以随意把子类写成数组。然后调用，不用考虑都有哪些子类，需要用什么样的子类去接受

    但自我理解就是，纯纯是因为前面定义class的时候，是把这个解释为一种数据引用型数据结构。
    那么自然也可以理解什么叫抽象类，把这个数据结构更进一步的抽象了
     */
    /*
    面对抽象编程的本质就是
    上层代码定义规范
    不需要子类就可以实现业务逻辑
    具体的业务逻辑由不同的子类实现。调用者并不关心
     */

}
abstract class AbstractB{
    /**
     * 输出当前子类是谁
     */
    public abstract void run();
    static void aa(){
        System.out.println("that is static method");
    }
    /*
    关于抽象类的解释。
    首先是修饰符号
    然后是没有方法体。静态方法不算。
    另外就是不能实例化抽象类，当然这玩意儿是肯定的
    因为构造方法也是要有方法体的，虽然有空方法体吧。
    上述还演示了一下怎么调用静态方法。
    至于静态方法和实例方法，其实这个要从运行周期去解释。因为是处于不同的
    周期，静态方法和抽象方法。所以在放入方法的时候，压根就没有实例方法，只有跟随类加载的静态方法
     */
}
class InstanceA extends AbstractB{
    @Override
    public void run(){
        System.out.println("instanceA.run");
    }
}