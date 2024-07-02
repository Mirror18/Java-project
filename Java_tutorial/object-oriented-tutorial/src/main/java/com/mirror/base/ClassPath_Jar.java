package com.mirror.base;

public class ClassPath_Jar {
    /*
    这里差不多是纯文档了
    classpath就是环境变量，用于搜索Java代码文件的路径
    一般是这样指示
    C:\work\project1\bin;C:\shared;"D:\My Documents\project1\bin"
    ;是分割符号，用于输入多条路径，例如上面就有三个。带有双引号是因为路径中有空格

    关于classpath的设定，一个是放到环境变量，虽然没有明写吧，不过设置Java的环境变量的时候
    就顺便把lang包的给包含进去了
    就在Java环境下的lib文件夹下，idea就包含进去了
    在启动JVM时设置
    java -cp .;C:\work\project1\bin;C:\shared abc.xyz.Hello
    平常默认的直接运行Java xxx其实就是在当前环境下

    至于jar就是压缩包，前面的核心类就是一个压缩包
    本质上就是一个zip格式的压缩文件
    运行就是
    java -cp ./hello.jar abc.xyz.Hello
    执行hello压缩包下 abc.xyz.Hello类

    如何创建jar包
    当然可以直接压缩
    至于自动打包，可以用maven，那等到之后去学。
    虽然现在这个项目就是用maven构建的吧
    里面也有一个特殊的文件
    /META-INF/MANIFEST.MF是纯文本。里面可以指定启动类，就可以直接运行简单类名
    java -jar hello.jar

     */
}
