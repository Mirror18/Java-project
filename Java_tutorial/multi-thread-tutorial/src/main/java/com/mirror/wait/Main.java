package com.mirror.wait;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author mirror
 */
public class Main {
    public static void main(String[] args) {
        //首先创建个队列
        var q = new TaskQueue();
        //创建个线程列表，当然后续是直接用线程池了
        var ts = new ArrayList<Thread>();
        //讲一个启动的线程加入到线程列表中
        //加入的线程都是要获取到值的
        for (int i = 0; i < 5; i++) {
            var t = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        String s = null;
                        try {
                            s = q.getTask();
                            System.out.println("execute task: " + s);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }

                    }
                }
            };
            t.start();
            ts.add(t);
        }
        //这是一个产生值的线程
        var add = new Thread(() -> {
            //这个线程调用十次随机产生值
            for (int i = 0; i < 10; i++) {
                String s = "t-" +(int) (Math.random()*100);
                System.out.println("add task: " + s);
                q.addTask(s);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //产生值的线程启动
        add.start();
        //主线程等待添加值线程结束
        try {
            add.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //等待0.1秒钟
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //为每个获取值的线程产生一个中断
        for (var t :
                ts) {
            t.interrupt();
        }
    }
}

class TaskQueue {
    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String s) {
        this.queue.add(s);
        this.notify();
    }

    public synchronized String getTask() throws InterruptedException {
        while (queue.isEmpty()) {
            this.wait();
        }
        return queue.remove();
    }
}