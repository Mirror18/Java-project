package com.mirror.list;

import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        List<Person> list = List.of(
                new Person("Xiao Ming"),
                new Person("Xiao Hong"),
                new Person("Bob")
        );
        System.out.println(list.contains(new Person("Bob")));
    }
}

class Person {
    String name;
    int age;
    public Person(String name) {
        this.name = name;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Person p) {
            return Objects.equals(this.name, p.name) && this.age == p.age;
        }
        return false;
    }
}
