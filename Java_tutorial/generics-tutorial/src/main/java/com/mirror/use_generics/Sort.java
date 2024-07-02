package com.mirror.use_generics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;

/**
 * @author mirror
 */
public class Sort {
    /*
     *这是有关泛型接口
     * 就是接口也能用泛型
     * 太多，所以用排序来解释
     */

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        String[] ss = new String[] { "Orange", "Apple", "Pear" };
        Arrays.sort(ss);
        System.out.println(Arrays.toString(ss));

        Person[] ps = new Person[] {
                new Person("Bob", 61),
                new Person("Alice", 88),
                new Person("Lily", 75),
        };
        Arrays.sort(ps);
        System.out.println(Arrays.toString(ps));
    }
}
@Data
@AllArgsConstructor
class Person implements Comparable<Person>{
String name;
int score;

    @Override
    public int compareTo(Person o) {
        return this.name.compareTo(o.name);
    }
    @Override
    public String toString(){
        return this.name + "," + this.score;
    }
}
