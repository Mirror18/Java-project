package com.mirror.pool;

import java.util.concurrent.CompletableFuture;

/**
 * @author mirror
 */
public class TwoStock {
    public static void main(String[] args) {
        //第一个任务
        CompletableFuture<String> cfQuery = CompletableFuture.supplyAsync(
                ()-> {return queryCode("china oil");});
        //cfQuery成功后继续执行下一个任务
        CompletableFuture<Double> cfFetch =cfQuery.thenApply(
                TwoStock::fetchPrice
        );
        //cfFetch成功后打印结果
        cfFetch.thenAccept((result) -> {
            System.out.println("price " + result);
        });
        //主线程等待关闭
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static String queryCode(String name){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        };
        return "601857";
    }
    static Double fetchPrice(String code){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 5 + Math.random() * 20;
    }
}
