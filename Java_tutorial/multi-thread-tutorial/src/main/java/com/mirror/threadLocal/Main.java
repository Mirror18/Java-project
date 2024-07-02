package com.mirror.threadLocal;


/**
 * @author mirror
 */

public class Main {

    public static void main(String[] args) {
        log("start main...");
        new Thread(() -> {
            log("run task...");
        }).start();
        new Thread(() -> {
            log("print...");
        }).start();
        log("end main");

        try (var ctx = new UserContext("mirror")) {
            String currentUser = UserContext.currentUser();

        }


    }

    private static void log(String s) {
        System.out.println(Thread.currentThread().getName() + ": " + s);
    }
}

