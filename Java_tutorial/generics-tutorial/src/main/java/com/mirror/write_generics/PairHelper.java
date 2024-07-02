package com.mirror.write_generics;

/**
 * @author mirror
 */
public class PairHelper {
    static int add(Pair1<? extends Number> p){
        Number first = p.getFirst();
        Number last = p.getLast();
        return first.intValue()  + last.intValue();
    }
}
