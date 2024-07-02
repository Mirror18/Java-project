package com.mirror.interrupt;

/**
 * @author mirror
 */
public class StatusInterrupt {
    public static void main(String[] args) {
        HelloThread1 t = new HelloThread1();
        t.start();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.running = false;
    }
}

class HelloThread1 extends Thread{
    /**
     * 线程间共享变量需要使用volatile关键字标记，
     * 确保每个线程都能读取到更新后的变量值。
     */
    public volatile boolean running = true;
    @Override
    public void run(){
       int n = 0;
       while (running){
           n++;
           System.out.println(n+" hello");
       }
        System.out.println("end");
    }
}