package com.mirror.generics;

import com.mirror.write_generics.Pair;
import com.mirror.write_generics.Pair1;
import org.junit.Test;

import java.util.ArrayList;

public class GenericsTest {
    @Test
    public void main(){
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        integerArrayList.add(123);
        //这里是行不通的，因为接受变量类型不一致，如果需要接受，则需要改写ArrayList代码
//        ArrayList<Number> numbers = integerArrayList;

    }
    @Test
    public void testExtend(){
        Pair1<Integer> p = new Pair1<>(123,456);
        int n = add(p);
        System.out.println(n);
        set(p,678,910);
        System.out.println(p.getFirst()+" " +p.getLast());

        System.out.println(isNull(p));

        Pair1<?> p2 = p; // 安全地向上转型
        System.out.println(p2.getFirst() + ", " + p2.getLast());
    }
    private int add(Pair1<? extends Number> p){
        Number first = p.getFirst();
        Number last = p.getLast();
        /*
        一个弊端，无法设置，因为当前类型是 ? extends Number，
        也正是因为如此，无法确定当前类型是哪个，只知道他是Number类或者他的子类
        那么说传入Number呢，也不成，因为Number是抽象类，本身需要重写方法外
        同时类型也是限制死了，唯一能做的就是set(null)

        调用的时候可以这样写，那么在写泛型类的啥时候也可以

        方法参数类型List<? extends Integer>表明了该方法内部只会读取List的元素，不会修改List的元素
        这是对一个参数进行只读的方法

        既然只读，那么也有只写的方法，但是只写方法也可以通过object来进行写入
        那就是super通配符
        因为确定了子类嘛，可以往父类中写入子类，就是向上强转
         */
//        p.setFirst(new Integer(first.intValue() +100));
//        p.setLast(new Integer(last.intValue() + 100));
        return first.intValue() + last.intValue();
    }

    private void set(Pair1<? super Integer >p,Integer first,Integer last){
        p.setLast(last);
        p.setFirst(first);
        /*
        所以这里面就是只写，但也可以读，不过是Object的读

        所以就有了PECS原则
        producer ,extends
        consumer , super
         */
        Object o = p.getFirst();
        System.out.println(p.getFirst());
    }
    /*
    另外还有一个无限定通配符
    <?>
    不允许调用set(T)方法并传入引用（null除外）；
    不允许调用T get()方法并获取T引用（只能获取Object引用）。

    不能读，也不能写，只能做判断
    当然这玩意儿也是可以用泛型参数，来消除无限定通配符

    Pair<?>是所有Pair<T>的超类：
    也就是可以用这玩意儿接受所有实现的子类
     */
    private boolean isNull(Pair1<?> p){
        return p.getFirst() == null || p.getLast() == null;
    }
}

