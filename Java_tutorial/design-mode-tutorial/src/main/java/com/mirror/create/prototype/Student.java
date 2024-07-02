package com.mirror.create.prototype;

public class Student implements Cloneable {
    private int id;
    private String name;
    private int score;

    // 复制新对象并返回:
    @Override
    public Object clone() {
        try {
            Object clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Student std = new Student();
        std.id = this.id;
        std.name = this.name;
        std.score = this.score;
        return std;
    }

    public Student copy() {
        Student std = new Student();
        std.id = this.id;
        std.name = this.name;
        std.score = this.score;
        return std;
    }
}