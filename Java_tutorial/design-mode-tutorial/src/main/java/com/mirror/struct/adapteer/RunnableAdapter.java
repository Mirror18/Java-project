package com.mirror.struct.adapteer;

import java.util.concurrent.Callable;

/**
 * @author mirror
 */
public class RunnableAdapter implements Runnable{
    private final Callable callable;
    public RunnableAdapter(Callable callable){
        this.callable = callable;
    }

    @Override
    public void run() {
        try {
            callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
