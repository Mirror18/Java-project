package com.mirror.write_generics;

import lombok.Data;

import java.lang.reflect.InvocationTargetException;
@Data
public class Pair1<T> {
    private T first;
    private T last;
    public Pair1(T first,T last){
        this.first = first;
        this.last = last;
    }
    public Pair1(Class<T> clazz) {
        try {
            first = clazz.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            last = clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    /*
    另外就是无法复写Object里的方法，只能换个同名的,或者把入参给换了
     */
//    @Override
//    public boolean equals(Object t){
//        return this == t;
//    }
}
