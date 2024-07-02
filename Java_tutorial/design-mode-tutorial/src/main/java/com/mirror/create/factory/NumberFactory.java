package com.mirror.create.factory;

/**
 * @author mirror
 */
public interface NumberFactory {
    Number parse(String s);

    static NumberFactory getFactory(){
        return impl;
    }
    static NumberFactory impl = new NumberFactoryImpl();

}
