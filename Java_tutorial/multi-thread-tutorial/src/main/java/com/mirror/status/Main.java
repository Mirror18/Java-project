package com.mirror.status;

public class Main {
    public static void main(String[] args) {
        Thread t = new Thread(()->{
            System.out.println("hello");
        });
        System.out.println("start");
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end");
    }
}
