package com.mirror.write_generics;

/**
 * @author mirror
 */
public class Pair<T, K> {
    private T first;
    private K last;

    public Pair(T first, K last) {
        this.first = first;
        this.last = last;
    }

    public T getFirst() {
        return first;
    }

    public K getLast() {
        return last;
    }

    /**
     * 但是编写静态方法的时候不能像上述一样编写方法
     */
    @org.jetbrains.annotations.NotNull
    @org.jetbrains.annotations.Contract(value = "_, _ -> new", pure = true)
    public static <L, I> Pair<L, I> create(L first, I last) {
        return new Pair<>(first, last);
    }

    public static void main(String[] args) {
        Pair<String, Integer> p = new Pair<>("test", 123);
        System.out.println(Pair.create("mirror", 456));
    /*
    这里没有写明泛型到底是干了什么
    包括编译出来的也看不到。
    但其实就是用Object去接收，输出的时候进行强制转型
    所以这是擦拭法

    通过转型为Object可以发现关于这其实室友问题的

    例如泛型是不能写基本类型，因为实际类型是无法持有基本类型
    第二就是我无法获取到带泛型的class
     */
        Pair<String, String> p1 = new Pair<>("Hello", "world");
        Pair<Integer, Integer> p2 = new Pair<>(123, 456);
        Class c1 = p1.getClass();
        Class c2 = p2.getClass();
        System.out.println(c1 == c2); // true
        System.out.println(c1 == Pair.class); // true
        /*
        所以说无论T的类型是什么，getClass()返回同一个Class实例

        另外就是无法实例化T类型
         */
    }

}