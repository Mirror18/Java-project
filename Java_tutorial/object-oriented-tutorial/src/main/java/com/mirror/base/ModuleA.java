package com.mirror.base;

public class ModuleA {
    /*
    怎么说，这个项目就是一个模块
    有基础语法，面对对象
    如果是自己处理这些包的依赖的话，就会很麻烦
    例如
    java -cp app.jar:a.jar:b.jar:c.jar com.liaoxuefeng.sample.Main
    就是导入包，然后运行类

    同时又有个问题，那就是用了包，发送给别人，是该怎么用呢，不用设置就能直接运行
    因为有外部依赖，并且是Java的核心类，在Java9 之前就是rt.jar，也是直接打包进jre之中的

    在Java 9 之后舍弃了jre的概念。刚好现在电脑装的话都是1.8和21这两个版本。所以就很清晰的发现
    Java 8 还有一个jre的单独文件夹，但是在Java21就没有这个文件夹了。

    那运行环境就用模块代替了，也就不用在运行的时候需要加入那么多的classpath参数了

    那么模块是干了什么呢。
    首先是必要的，核心类库原先都是rt.jar文件包，那么第一步就是把rt.jar给划分成几十个模块
    模块的后缀是 .jmod 模块名就是文件名。分成模块主要就是把各个模块之间的依赖关系给放到了模块内的module-info.class之中了
    module-info.class卸了什么，下面会详细讲解。因为就是为了做这个模块

    编写模块，手先文件结构还是普通的文件结构，包括src && bin 文件夹
    但是在src文件夹下多了一个module-info.java文件
    module 是关键字，后面是模块名
    然后就是 requires 这是引入其他模块名（至于说为什么在这看不到，那是因为这是用的maven来打包之类的，主打一个方便和不用管）

    然后就是编译，把Java文件编译成二进制的class文件
javac -d bin src/module-info.java src/com/itranswarp/sample//*.java

然后是打包
jar --create --file hello.jar --main-class com.itranswarp.sample.Main -C bin
得到一个hello.jar文件。主类也确定了。最后是二进制文件的地址
          ps(这一步已经可以运行了 java -jar hello.jar),但我们是要创建模块

 jmod create --class-path hello.jar hello.jmod
 这样就确定一个模块
 运行模块就这样
 java --module-path hello.jar --module hello。world


 或许会吐槽这模块和jar包又有什么区别，只能说确实没区别，除了不用自己写依赖关系
 但mod出现的主要原因是为了瘦身，因为jre太大了。
 现在制作自己的jre

 jlink --module-path hello.jmod --add-modules java.base,java.xml,hello。world --output jre/

 首先是模块的路径，然后是需要哪些模块
 在然后就是输出目录

 至于怎么运行
 jre/bin.java --module hello。world

 因为引出了模块，所以又多了一种访问权限。
 于是就有了export ，也就是前面编写module-info.java 写的
 只要这里标注export，别人就可以用这个模块下的这个包
     */



}
