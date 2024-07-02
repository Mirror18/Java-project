package com.mirror.annotation;

import java.lang.annotation.*;

/**
 * @author mirror
 */
@Target({//在注解之上可以修饰其他注解，这被称为元注解
        //这个是定义annotation能够被应用于源码的哪些位置
        ElementType.TYPE,//定义在类或者接口
ElementType.METHOD,//可以放到方法上
ElementType.FIELD,//字段
ElementType.CONSTRUCTOR,//构造方法
ElementType.PARAMETER})//参数
@Retention(//这个定义了生命周期
        //RetentionPolicy.CLASS//仅仅CLASS文件，同时也是默认的
        //RetentionPolicy.SOURCE 仅仅编译期
        RetentionPolicy.RUNTIME //运行期。也是我们自定义时常用的
)
@Repeatable(Reports.class)
public @interface Report {
    /*
    类似于无参数方法，Default设置一个默认值
     */
    int type() default 0;
    String level() default "info";
    String value() default "";
}
/*
@Inherited
使用@Inherited定义子类是否可继承父类定义的Annotation。
@Inherited仅针对@Target(ElementType.TYPE)类型的annotation有效，
并且仅针对class的继承，对interface的继承无效：

使用的时候一个类用到了，则它的子类默认也定义了该注解
 */