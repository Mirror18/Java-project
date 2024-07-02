package com.mirror.exception;


import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.util.logging.Logger;

/**
 * @author mirror
 */
@Slf4j
public class Logging {
    public static void main(String[] args) {
        /*
        这一步很重要，虽然是基操吧
        就是关于信息打印的事，在这里，因为就一个程序，所以用sout输出各个点的信息
        所以就要使用日志
        在框架中常用
         */

//        Logger logger = Logger.getGlobal();
//        logger.info("start process...");
//        logger.warning("memory is running out...");
//        logger.fine("ignored.");
//        logger.severe("process will be terminated ...");
        /*
        上述其实就是设置日志输出的设定集别
        但这个内置的并不常用，因为不方便呗
         */

        /*
        所以我们用三方组件
        log4j2，除了用的时候比较麻烦，需要配置log4j2.xml

         */
        // 记录不同级别的日志
        log.trace("This is a TRACE level message.");
        log.debug("This is a DEBUG level message.");
        log.info("This is an INFO level message.");
        log.warn("This is a WARN level message.");
        log.error("This is an ERROR level message.");

        // 示例：记录异常信息
        try {
            int result = 10 / 0;
        } catch (Exception e) {
            log.error("An exception occurred: ", e);
        }

        /*
        以后有时间再玩吧
        日志输出也主要用于框架，
        配置就是这样一个配置

         */
    }
}
