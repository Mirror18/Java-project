package com.mirror.exception;

public class Assertion {
    public static void main(String[] args) {
        double x = Math.abs(-123.45);
        //断言就是这里一定是正确的才会继续下去，否则会跑出AssertionError
        assert x >= 0 : "x must >= 0";
        System.out.println(x);
    }
}
