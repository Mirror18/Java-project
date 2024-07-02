package com.mirror.base_tuorial;

/**
 * @author mirror
 */
public class CommandLineArguments {
    public static void main(String[] args) {
        //这里需要运行的时候添加参数 -version
        for (String arg : args) {
            if ("-version".equals(arg)) {
                System.out.println("V 1.0");
                break;
            }
        }

    }
}
