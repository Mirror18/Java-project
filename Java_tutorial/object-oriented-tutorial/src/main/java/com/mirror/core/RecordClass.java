package com.mirror.core;

public class RecordClass {
    public static void main(String[] args) {
        Point p = new Point(123, 456);
        System.out.println(p);
        System.out.println(p.getClass());
    }
}

/**
 * 虽然我也不知道这里反编译应该是会出现编译结果的
 * 不过没有
 * 但这个关键字就是省略了点东西
 */
record Point(int x, int y) {
    public Point {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException();
        }
    }

    public static Point of(){
        return new Point(0,0);
    }
    public static Point of(int x, int y){
        return new Point(x,y);
    }
}

//效果类似于下面
//final class PointA extends Record{
//    private final int x;
//    private final int y;
//
//    public PointA(int x, int y){
//if(x<0||y< 0){
//        throw new IllegalArgumentException();
//        this.x = x;
//        this.y = y;
//    }
//    public int x(){
//        return this.x;
//    }
//    public int y(){
//        return this.y;
//    }
//
//    @Override
//    public String toString(){
//        return String.format("Pint[x=%s,y=%s]",x,y);
//
//    }
//    @Override
//    public boolean equals(Object o){
//        return false;
//    }
//}
