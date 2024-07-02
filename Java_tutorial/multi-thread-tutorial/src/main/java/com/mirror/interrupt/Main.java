package com.mirror.interrupt;

/**
 * @author mirror
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new MyThread();
        //首先是这里创建一个新的线程，然后让其启动。
        //注意这个时候main主线程和mythread线程都在运行中
        t.start();
        //然后在这里让主线程也就是main线程暂停一秒钟，现在只有mythread线程在运行中
        //也就是说在这一秒钟内看只有mythread线程运行，转到mythread内部
        Thread.sleep(1000);
        /*
        或许该好奇怎么在主线程中终端子线程再启动的子线程
        ，诺，就靠interrupt，
        那或许说不对啊，我要中断子线程调用的子线程，我中断子线程干嘛，
        而且子线程里还没有对interrupted的处理，也就是说中断也没啥作用啊,
        就像最开始测试的那样，只会对子线程抛出一个InterruptedException异常
        但是子线程没有对中断请求的处理，所以中断没啥用，
        不过既然这里发出了中断请求mythread，那这个时候就去那里面看看吧

        记住现在的条件，主线程已经暂停一秒钟了
         */
        t.interrupt(); // 中断t线程
        t.join(); // 等待t线程结束
        System.out.println("end");
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        //线程启动运行这个run，
        //内容是又开了一个线程
        //也就是在main线程暂停前，有三个线程在运行
        Thread hello = new HelloThread();
        hello.start(); // 启动hello线程
        //也就是在main线程前这三个线程都启动了
        try {
            //在暂停的一秒钟的时候，是mythread线程和Hello线程运行
            //但是在这里。mythread线程卡住；也就是处于等待，等待Hello线程的运行结束
            hello.join(); // 等待hello线程结束
            /*
            这一秒钟里，mythread线程一直等待Hello线程中断，
            但是没等到，却等到一秒钟之后的main主线程发来的interrupted，
            但是在这个线程里没有对中断的处理啊，没有! isInterrupted()这个玩意儿
            那按理来说不做点啥显得这个中断信号很呆，
            所以看这里，

            或者说不像教程中说的是  如果对main线程调用interrupt()，join()方法会立刻抛出InterruptedException
            而是只要有个能捕捉InterruptedException异常的玩意儿就行，

            关键就在于这里捕获了这个异常，所以跳了出去，对Hello现成发起了中断，
            哦谢天谢地，终于是把线程给中断了


             */
        } catch (InterruptedException e) {
            System.out.println("mythread,interrupted!");
        }
        hello.interrupt();
        /*
        既然这里像Hello线程发出了中断，那么这里的任务就完成了，去Hello线程中看看去
         */
    }
}

class HelloThread extends Thread {
    @Override
    public void run() {
        int n = 0;
        //在这一秒钟内，这个在干什么呢
        //看是否有从mythread线程中发来中断，
        //为什么是mythread线程，因为主线程找不到这个线程的变量，怎么中断
        while (!isInterrupted()) {
            n++;
            //这里面干的事就是等待0.1秒钟就打印一次
            System.out.println(n + " hello!");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("hello,interrupted");
                break;
            }
            /*
            这里一直等待中断信号。
            、过了一秒钟后。
            主线程对mythread线程发来了中断，
            mythread线程对这里发出了中断

            发出了中断请求好啊，终于不用干活了，但是发现了不对。
            如果去掉break之后发现还是暂停不了，却不是像其他线程中断的时候，
            那while暂停为什么不行呢
            或许这个信号位可以解释，点进去查看源码，昂，isInterrupted（）返回的就是一个boolean值

            也就是说必须这个布尔值是false才能运行while，所以才是依靠这个条件中断线程的。
            那说了这么多没用的，还是没解释为什么会有Break.
            因为加了break，那岂不是说传递什么中断信息，我让Thread.sleep的时候发生中断异常不就行了么

            好吧其实就是这个道理，首先是因为计算机执行的速度很快，所以大部分其他线程发过来信息的时候，Hello线程
            基本上就是处于等待时间的，所以说才要添加个break，那或许说，添加什么break，既然传递过来信号了
            那直接跳出不好么，但这就是问题所在，当捕获到异常之后，中断状态会清除，所以这个时候while循环还是true。

            那明白了事情经过，那不就是因为当mythread发出中断之后，Hello是处于线程等待时间，所以捕获出一个循环
            直接进try里面的catch。然后跳出。

            那还有个问题，反正是break，那我while循环设置那么复杂干嘛，直接while true不就好了么。

            一般情况下确实是没问题，哪怕现在你修改也没问题，一样会结束

            但这个是概率问题，为什么这么说呢，因为当mythread发来中断信号的时候，线程处于的状态，
            状态有，新创建的线程，运行中的先整，阻塞的线程，等待的线程，计时等待的线程，线程终止。

            所以发现了么，运行中的线程，有正在运行，阻塞，等待，
            大部分时间是处于等待，所以while true ，没问题，因为很大概率就是这玩意儿
            但是处于运行的时候呢，阻塞虽然没写代码，但是也很清楚，到时候也是try 包裹的。
            所以这才是while设置的条件是这个。


             */
            /*
            实话说到了多线程这块，确实挺麻烦大的，因为程序不像以前是单独就干一件事了，同时要干很多事。
            虽然相比较于计组来说，线程的切换已经很简单了，使用的时候也是很简单，但必须是一步一步的顺理出
            所有的时间线，所以才显得这么长，而这个就是为了异步做处理。

            或者说同步和异步的概念就是因为多线程才有的区别。

             */

        }
    }
}

