package com.mirror.reflect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author mirror
 */
public class FieldA {
    public static void main(String[] args) {
        /*
        对于任意一个Object实例，只要我们获取到了Class
        就可以获取到他的一切信息

        一共四个
        Field getField(name)：根据字段名获取某个public的field（包括父类）
        Field getDeclaredField(name)：根据字段名获取当前类的某个field（不包括父类）
        Field[] getFields()：获取所有public的field（包括父类）
        Field[] getDeclaredFields()：获取当前类的所有field（不包括父类）
         */
        Class stdClass = Student.class;
        try {
            System.out.println(stdClass.getDeclaredField("score"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(stdClass.getField("name"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        //获取到私有变量
        try {
            System.out.println(stdClass.getField("name"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(stdClass.getFields()));

        /*
        一个Field对象包含了一个字段的所有信息
        getName()返回字段名称
        getType()返回字段类型
        getModifiers()返回字段的修饰符
         */
        Field[] f = String.class .getDeclaredFields();
        System.out.println(Arrays.toString(f));
        //或许会吐槽这怎么输出的名字不对。，原因也打印出来了，因为继承的是Object
        System.out.println(f[0].getName());
        System.out.println(f[1].getType());
        int m = f[1].getModifiers();
        System.out.println(Modifier.isPrivate(m));
        //获取字段值
        /*
        也就是说，我们通过反射，可以获取到所有的字段
        而普通情况下，我们无法访问其他类private修饰的字段

        另外setAccessible(true)并不是万能的，
        如果有SecurityManager，那么会根据规则进行检查，会阻止
         */
        Object o = new Person("xiao Ming");
        Class c = o.getClass();
        Field f1 = null;
        try {
            f1 = c.getDeclaredField("name");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Object value = null;
        try {
            assert f1 != null;
            //这个是因为字段可能会有private，外部无法访问。。
            //这段代码的意思是，不关这个字段是不是public,一律允许访问
            f1.setAccessible(true);
            //Field.get(Object)获取指定实例的指定字段的值
             value =f1.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(value);

        //设置字段值
        //通过Field.set(Object,Object)，第一个是实例，第二个是修改的值
        System.out.println(((Person) o).getName());
        try {
            f1.set(o,"xiao");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(f1.get(o));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
@Data
@AllArgsConstructor
class Person{
    private String name;
    public Person(){}
}
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
class Student extends Person{
    public int score;
    private int grade;

    public Student(String name) {
        super(name);
    }
}

/*
所以我们通过这一个的调用
明白了字段的获取
字段的信息
字段的设置
虽然下面还有构造器和方法的获取和设置
但已经隐隐感觉到，所谓的反射，其实就是把这些信息当做一个普通的对象去访问和设置
一个模板，万能的

通过反射读写字段是一种非常规的方法，会破坏对象的封装
 */