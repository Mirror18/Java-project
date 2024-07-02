package com.mirror.pool;

import java.util.concurrent.CompletableFuture;

/**
 * @author mirror
 */
public class StockFuture {
    public static void main(String[] args) {
        //常见异步执行任务
        CompletableFuture<Double> cf  = CompletableFuture.supplyAsync(StockFuture::fetchPrice);
        //如果执行成功
        cf.thenAccept((result)->{
            System.out.println("price: " + result);
        });
        //如果执行异常
        cf.exceptionally((e)-> {
            e.printStackTrace();
            return null;
        });
        //主线程关闭，线程池立马关闭
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Double fetchPrice() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Math.random() < 0.3){
            throw new RuntimeException("fetch price failed!");
        }
        return 5 + Math.random() * 20;
    }
}
