package com.mirror.test;



import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AbsTest {

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 5, 100 })
    public void testAbs(int x) {
        assertEquals(x, Math.abs(x));
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, -5, -100 })
    void testAbsNegative(int x) {
        assertEquals(-x, Math.abs(x));
    }

}
