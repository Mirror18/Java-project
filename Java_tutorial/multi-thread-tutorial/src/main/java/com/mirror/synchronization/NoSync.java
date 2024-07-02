package com.mirror.synchronization;

/**
 * @author mirror
 */
public class NoSync {
    public static void main(String[] args) {
        var add = new AddThread();
        var dec = new DecThread();
        add.start();
        dec.start();
        /*
        首先经过前面的洗礼，这些代码很容易看懂
        并且都是同样的循环次数
        那按理来说应该是结果为0的
        但是答案却是一个随机数

        那问题出在哪了呢，按照理论来说不会出问题的，哪怕是随机的加减，只要一次的加减操作完成了
        就不会出问题啊，这是啥情况。
        问题就在这个一次的加减操作上。

        或许这里看着是单独的一条语句，就算线程不同步，或者说这两个线程启动的时间啊什么的不一样
        那结果也是相同的。

        问题就在一次的加减操作上，首先这玩意儿稍微涉及到汇编了
        其实就是一条语句是一个微程序，对于汇编来说，一个微程序由一堆微指令组成的。
        昂稍微有点不太对，因为中间还涉及了语句和指令的关系，大概是三层来着，就这一次到底了。

        然后又知道线程之间的切换很快的，因为不涉及到自愿的更替。比起进程来说。当然还有微线程。

        所以有可能我刚取出来这个数打算进行一次add，但是线程切换了，切换的时候那边已经完成了
        一次dec,或者多个，谁也说不准。那么这个时候我已经拿到了数据了啊，就是更新前的数据。
        我这个时候写完在放回去。那是不是感觉这不对啊，那对于这整个程序来说，。那就是少了几次的dec
        操作。
        所以这才是问题所在。

        或许前面的设置线程的flag位置的时候还在好奇为什么会使用volatile,这就是问题所在。
        鬼知道拿的数据是什么时候的数据。

        这玩意儿要牢记，因为MySQL啊，还有一堆的事务管理啊，什么同步异步，乱七八糟的要解决的就是这玩意儿

        或许在其他地方听说了原子性，昂，就是这玩意儿，大意是干这个的。
        不指望看这一两句话就能解决，因为这玩意儿占据操作系统一本书的一半篇幅。
        计组的也是占了有一半的篇幅解决相关问题。其实也是够头疼的。

         */
        try {
            add.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            dec.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Counter.count);

    }
}

class Counter {
    public static int count = 0;
    public static int num = 1000;
    //加锁
    public static final Object lock = new Object();
}

class AddThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < Counter.num; i++) {
            //获取锁
            synchronized (Counter.lock) {
                Counter.count++;
            }//释放锁

        }
    }
}

class DecThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < Counter.num; i++) {
            synchronized (Counter.lock) {
                Counter.count--;
            }
        }
    }
}

class Point{
    int x;
    int y;

    public void set(int x, int y){
        synchronized (this){
            this.x = x;
            this.y = y;
        }
    }

    public int[] get(){
        int[] copy = new int[2];
        copy[0] = x;
        copy[1] = y;
        return  copy;
    }
}