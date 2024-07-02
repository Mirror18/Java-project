package com.mirror.proxy;

import com.mirror.Impl.CalculatorImpl;
import com.mirror.Impl.CalculatorProxy;
import org.junit.Test;

public class StaticProxyTest {
    @Test
    public void testStatic(){
        Calculator calculator = new CalculatorProxy(new CalculatorImpl());
        calculator.add(1,2);
        calculator.subtract(2,1);
    }
}
