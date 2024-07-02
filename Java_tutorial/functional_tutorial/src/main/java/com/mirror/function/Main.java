package com.mirror.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mirror
 */
public class Main {
    public static void main(String[] args) {
        String[] array = new String[] { "Apple", "Orange", "Banana", "Lemon" };
        Arrays.sort(array, Main::cmp);
        System.out.println(String.join(", ", array));

        Arrays.sort(array, String::compareTo);
        System.out.println(String.join(", ", array));


        List<String> names = List.of("Bob", "Alice", "Tim");
        List<Person> persons = new ArrayList<>();
        for (String name : names) {
            persons.add(new Person(name));
        }

        List<Person> personList = names.stream().map(Person::new).collect(Collectors.toList());
        System.out.println(personList);
    }

    static int cmp(String s1, String s2) {
        return s1.compareTo(s2);
    }
}

class Person{
    String name;
    public Person(String name){
        this.name=name;
    }
    @Override
    public String toString(){
        return "Person: " + this.name;
    }
}