package com.mirror.generics;

import com.mirror.write_generics.Pair1;

import java.lang.reflect.Array;

public class GenericsAndReflectTest {
    /*
    并没有太多稀奇的，因为写反射的时候，用的基本上都是泛型

    至于说为什么哪里多，那是因为本来就是模板，反射是要面对所有类的，自然是类名不可能写好
    所以自然那里全是泛型，也就是用的就是泛型
     */
    public void main(){
        Class<? super String> sup = String.class.getSuperclass();
        /*
        至于获取泛型数组，则是必须配合强转
         */
        @SuppressWarnings("unchecked")
        Pair1<String>[] ps = (Pair1<String>[]) new Pair1[2];

        /*
        虽然我们用过构造器的数组，或者，字段，方法，获取到一大堆的数组
        而这些就是泛型数组，用是我们用过，可是怎么创建的，好像是头一次

        怎么创建泛型数组，则是必须借助Class<T>来创建泛型数组

         */
    }
    private <T> T[] createArray(Class<T> cls) {
        return (T[]) Array.newInstance(cls, 5);
    }
}
