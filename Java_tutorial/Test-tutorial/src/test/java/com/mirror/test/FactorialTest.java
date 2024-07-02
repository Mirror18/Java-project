package com.mirror.test;

import org.junit.Test;
import org.junit.jupiter.api.function.Executable;


import static org.junit.jupiter.api.Assertions.*;

public class FactorialTest {
    @Test
    public void testFact() {
        assertEquals(1, Factorial.fact(1));
        assertEquals(2, Factorial.fact(2));
        assertEquals(6, Factorial.fact(3));
        assertEquals(3628800, Factorial.fact(10));
        assertEquals(2432902008176640000L, Factorial.fact(20));
        assertEquals(0.1, Math.abs(1 - 9 / 10.0), 0.0000001);
    }

    @Test
    public void testNegative() {
        assertThrows(IllegalArgumentException.class, new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Factorial.fact(-1);
                    }
                }
        );
    }
}