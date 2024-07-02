package com.mirror.map;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        Person s = new Person("Xiao","Ming", 99);
        Map<String, Person> map = new HashMap<>();
        map.put("Xiao Ming", s); // 将"Xiao Ming"和Student实例映射并关联
        Person target = map.get("Xiao Ming"); // 通过key查找并返回映射的Student实例
        System.out.println(target == s); // true，同一个实例
        System.out.println(target.age); // 99
        Person another = map.get("Bob"); // 通过另一个key查找
        System.out.println(another); // 未找到返回null
        Person s1 = new Person("Xiao","Hong", 18);
        Person s2= new Person("Xiao","Jun", 19);
        Person s3= new Person("Xiao","Ming", 20);
        map.put("Xiao jun",s2);
        map.put("Xiao hong",s1);
        map.put("Xiao Ming",s3);
        for (String key:
             map.keySet()) {
            Person value = map.get(key);
            System.out.println(key + " = " + value);

        }
        System.out.println();
        for (Map.Entry<String,Person> entry:
             map.entrySet()) {
            String key = entry.getKey();
            Person value = entry.getValue();
            System.out.println(key + " = " + value);
        }
    }
}

/**
 * @author mirror
 */
@AllArgsConstructor
class Person {
    String firstName;
    String lastName;
    int age;

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, age);
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Person p) {
            return Objects.equals(this.firstName, p.firstName)&& Objects.equals(this.lastName,p.lastName) && this.age == p.age;
        }
        return false;
    }
}