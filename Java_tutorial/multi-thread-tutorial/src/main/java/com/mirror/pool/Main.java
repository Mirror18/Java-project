package com.mirror.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mirror
 */
public class Main {
    public static void main(String[] args) {
        //创建线程池
        ExecutorService es = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 6; i++) {
            es.submit(new Task(" " + i));
        }
        es.shutdown();
    }
}

class Task implements Runnable {
    private final String name;
    public Task(String name){
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("start task " + name);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end task " + name);
    }
}