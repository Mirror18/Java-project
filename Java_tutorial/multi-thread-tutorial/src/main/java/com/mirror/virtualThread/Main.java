package com.mirror.virtualThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author mirror
 */
public class Main {
    public static void main(String[] args) {
        // 传入Runnable实例并立刻运行:
        Thread vt = Thread.startVirtualThread(() -> {
            System.out.println("Start virtual thread 1...");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("End virtual thread. 1");
        });

        // 创建VirtualThread:
        var vt1 =Thread.ofVirtual().unstarted(() -> {
            System.out.println("Start virtual thread 2...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("End virtual thread. 2");
        });
// 运行:
        vt1.start();


        // 创建ThreadFactory:
        ThreadFactory tf = Thread.ofVirtual().factory();
// 创建VirtualThread:
        Thread vt2 = tf.newThread(() -> {
            System.out.println("Start virtual thread 3...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("End virtual thread. 3");
        });
// 运行:
        vt2.start();

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        ThreadFactory tf1 = Thread.ofVirtual().factory();
        for (int i = 0; i < 10000; i++) {
            Thread vt3 = tf1.newThread(()->{});
            executor.submit(vt3);
            executor.submit(()->{
                System.out.println("start virtual thread...");
                Thread.sleep(1000);
                System.out.println("end virtual thread...");
                return true;
            });
        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
