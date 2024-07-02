package com.mirror.struct.adapteer;

import java.util.concurrent.Callable;

/**
 * @author mirror
 */
public class Task implements Callable<Long> {

    private final long num;
    public Task(long num){
        this.num = num;
    }
    @Override
    public Long call() throws Exception {
        long r = 0;
        for(long n = 1; n <= this.num;n++){
            r += n;
        }
        System.out.println("Result: " + r);
        return r;
    }
}
