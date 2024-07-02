package com.mirror.start;

import java.time.LocalTime;

/**
 * @author mirror
 */
public class Main {
    public static void main(String[] args) {
        Thread t = new Thread();
        t.start();
        Thread t1 = new Mythread();
        t1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.interrupt();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("main");
        }



        Thread t2 = new Thread(new MyRunnable());
        //设置守护线程
        t2.setDaemon(true);
        t2.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread t3 = new Thread(() -> {
            System.out.println("t3 start new thread");
        });
        t3.start();
    }
}

class Mythread extends Thread {
    @Override
    public void run() {
        System.out.println("t1 start new thread");
//        for (int i = 0; i < 100; i++) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                System.out.println("mythread");
//                e.printStackTrace();
//            }
//            System.out.println(i);
//        }
        int n = 0;
        while (!isInterrupted()) {
            n++;
            System.out.println(n + " hello!");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("mythread");
                break;
            }
        }

    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("t2 start new thread");
        int n = 0;
        while (true) {
            n++;
            System.out.println(n + " myRunnable");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

        }
    }
}


class TimerThread extends Thread {
    @Override
    public void run() {
        while (true) {
            System.out.println(LocalTime.now());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}