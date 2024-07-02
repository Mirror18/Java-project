package com.mirror.annotation;

/**
 * 首先我们先明确的是根据Retention的配置
 * source编译期间就被丢弃
 * Class仅仅被保存在class文件中，不会被加载进虚拟机
 * runtime类型才会被加载进虚拟机，在运行期间才被读取
 *
 * 所以目标很明确，只有runtime时期的才要用
 * @author mirror
 */
@Report(type=1,level = "warning",value = "class level")
public class ProcessAnnotation {
    @Report(type=2,level = "info",value = "method level")
    public void annotateMethod(){
        System.out.println("running");
    }
    @Report(type=3,level = "debug",value = "field level")
    @Report(type=4,level = "error",value = "field level")
    int n=0;
    @Report(type=5,level = "warning",value = "constructor level")
    @Report
    public ProcessAnnotation(){}
}
