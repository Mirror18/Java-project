package com.mirror.base;

public class VersionS {
    /*
    依旧是纯文本
    只是平常查看本地的Java版本
    在cmd中运行java -version，可以出现现在安装的是那个文本

    低版本不能执行高版本编译的class文件。
    平常是要查class的文件版本号的

    怎么班一低版本
    javac --release 11 Main.java
    在idea中设置也比较简单就是了

    或者说这样
    javac --source 9 --target 11 Main.java
    就是把源码当做Java 9 兼容版本，并输出class为 Java 11 的兼容版本

    一般情况下使用--release ，因为这里会检查源码中是否有超出这个版本的语法

     */
}
