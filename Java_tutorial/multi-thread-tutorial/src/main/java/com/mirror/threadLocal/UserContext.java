package com.mirror.threadLocal;

/**
 * @author mirror
 */
public class UserContext implements AutoCloseable{
    static final ThreadLocal<String> ctx = new ThreadLocal<>();
    public UserContext(String user){
        ctx.set(user);
    }
    public static String currentUser(){
        return ctx.get();
    }
    @Override
    public void close(){
        ctx.remove();
    }
}
