package com.mirror.base;
/*
这里就是包名，没啥用，只是用于区分，还有打包要放的位置
 */

/**
 * @author mirror
 */
public class PackageA {
}
/*
或者换句话说，类名叫PackageA
但是完整的类名 应该是com.mirror.base.PackageA
 */

/*
为什么会单独出一个这个，主要还是因为
经常使用的import，这玩意儿写的就是完整的类名
在同一个包的不同class中就不需要import导入包名
还有一个
import static 导入一个类的静态字段和静态方法，
就是很少用就是了。因为平常就是直接用

那或许有没有好奇为什么使用System.out.println的时候，这个只是个静态方法
但是当前包也没有这个类啊，
那是因为这个事java.lang包下的，是属于默认自定义导入的
 */

/*
实话说这个包还是有很多理论知识的，或者说IDE默认做了很多的事
1. 同名类，不同包
    只能通过一个import 导入，一个写完整类名
2.编译
     每一个接触Java的，都会被这种文件的存放方式感到震惊
     首先是object，项目名
     项目名下是模块名
     模块名下又是 src 源码区域
     src 下又分为 main包，也就是主要代码。test，测试包
     然后这俩都同时拥有Java源代码区域，resource，资源区域
     Java包下才是我们常见的Java软件包，package包名也是这里的
 */

/*
src 又和target，也就是编译的结果，也就是存放class结尾的文件同一个层次
 */
/*
那么自然也就好奇这玩意儿是怎么编译的了
毕竟平常编译也就是
javac xxx.java
java xxx.class

虽然没法截图吧
但是去看看编辑运行配置
首先这玩意儿实在object目录下执行的命令
其实说白了就是把命令拆分了。各个路径自己选
不过一般的还可以演示下

例如编译到特定文件夹
javac -d ./bin src/**//*.java
这里就显示了，编译当前文件下的src子文件夹下的所有Java到当前路径下的bin文件夹下
不支持Windows，Windows需要把名字写全。当然也无所谓就是了，运行配置就能看懂

运行就看运行配置
 */