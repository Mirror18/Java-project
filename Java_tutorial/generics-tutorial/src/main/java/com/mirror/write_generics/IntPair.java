package com.mirror.write_generics;

public class IntPair extends Pair1<Integer> {
    public IntPair(Class<Integer> clazz) {
        super(clazz);
    }

    public IntPair(Integer first, Integer last) {
        super(first, last);
    }
}
