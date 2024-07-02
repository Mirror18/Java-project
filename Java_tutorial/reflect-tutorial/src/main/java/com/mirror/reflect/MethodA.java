package com.mirror.reflect;


import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * @author mirror
 */
public class MethodA {
    public static void main(String[] args) {
        /*
        Method getMethod(name, Class...)：获取某个public的Method（包括父类）
        Method getDeclaredMethod(name, Class...)：获取当前类的某个Method（不包括父类）
        Method[] getMethods()：获取所有public的Method（包括父类）
        Method[] getDeclaredMethods()：获取当前类的所有Method（不包括父类）
         */
        /*
        跟获取字段的方式一样
         */
        Class stdClass = Student.class;


        try {
            System.out.println(stdClass.getMethod("getScore"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            //后面的class指的是入参
            System.out.println(stdClass.getDeclaredMethod("setGrade", int.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(stdClass.getMethod("getName").getName());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Method[] methods = stdClass.getMethods();
        System.out.println(Arrays.toString(methods));
        Method mm = null;
        for (Method method : methods
        ) {
            if ("getName".equals(method.getName())) {
                System.out.println(method);
                mm = method;
            }


        }
        assert mm != null;
        System.out.println(mm.getName());
        System.out.println(mm.getReturnType());
        System.out.println(Arrays.toString(mm.getParameterTypes()));
        System.out.println(mm.getModifiers());

        String s = "hello,world";
        Method m = null;
        try {
             m = String.class.getMethod("substring",int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert m !=null;
        String r="";
        try {
             r = (String) m.invoke(s,6);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //这是获取到方法该如何调用。注意到invoke,因为动态代理也是用这个调用
        System.out.println(r);

        //调用静态方法
        Method intA = null;
        try {
             intA = Integer.class.getMethod("parseInt",String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert intA!=null;
        try {
            Integer n = (Integer) intA.invoke(null,"123456");
            System.out.println(n);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //调用非public方法
        Person p = new Person();
        Method person = null;
        try {
             person = p.getClass().getMethod("setName", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert person != null;
        person.setAccessible(true);
        try {
            person.invoke(p,"mirror");
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(p.getName());
        /*
        同理，setAccessible(true)可能会失败，一个道理
        至于因为继承的问题，所以有多态，有个例子，就是不想写了
        以上面的解释person是方法，p是实例
        关键不在于方法是谁的，而在于传入的实例是谁
        也就说主动权是交在person这个实例上的
         */
    }
}
//这里没有写其他类，因为是在同一个包下，所以可以直接访问
