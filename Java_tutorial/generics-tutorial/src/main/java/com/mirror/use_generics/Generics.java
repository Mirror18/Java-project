package com.mirror.use_generics;

import java.util.ArrayList;

/**
 * @author mirror
 */
public class Generics {
    /*
    什么是泛型，
    其实前面已经用到很多了，反射的时候获取到的class对象
    还有可变数组，ArrayList<>
    都在用泛型。

    那么究竟什么事泛型呢
    说白了就是模板，省去了强制转型，不然就很容易转错
    为什么会这样说呢，因为都是Object类型。那么鬼知道是那个子类
    尤其是还有继承啥之类的，如果全用Object接受，那么就会失去继承的意义

    这里以动态数组举例
     */
    /**
     * 动态数组
     */
    ArrayList<String> arrayList = new ArrayList<>();


}
class StringArrayList{
    private String[] array;
    private int size;
    public void add(String e){}
    public void remove(int index){}
}
/*
如果再写一个IntArrayList
就实在是2太麻烦了
所以引入了泛型

 */

/**
 * 这个就是泛型，或者说是模板
 * @param <T>
 */
class ArrayListA<T>{
    private T[] array;
    private int size;
    public void add(T e){}
    public void remove(int index){}

}