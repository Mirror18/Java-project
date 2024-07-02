package com.mirror.map;

import lombok.Data;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author mirror
 */
public class TreeMapA {
    public static void main(String[] args) {

            Map<PersonA, Integer> map = new TreeMap<>(new Comparator<PersonA>() {
                @Override
                public int compare(PersonA p1, PersonA p2) {
                    return p1.name.compareTo(p2.name);
                }
            });
            map.put(new PersonA("Tom"), 1);
            map.put(new PersonA("Bob"), 2);
            map.put(new PersonA("Lily"), 3);
            for (PersonA key : map.keySet()) {
                System.out.println(key);
            }
            // {PersonA: Bob}, {PersonA: Lily}, {PersonA: Tom}
            System.out.println(map.get(new PersonA("Bob"))); // 2
        }
    }

@Data
class PersonA {
    public String name;
    PersonA(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "{PersonA: " + name + "}";
    }
}
