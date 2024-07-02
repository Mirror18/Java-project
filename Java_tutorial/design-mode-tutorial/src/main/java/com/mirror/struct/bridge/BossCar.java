package com.mirror.struct.bridge;

public class BossCar extends RefinedCar{
    public BossCar(Engine engine) {
        super(engine);
    }

    @Override
    protected String getBrand() {
        return "Boss";
    }
}
