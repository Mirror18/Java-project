package com.mirror.appcontext;

import com.mirror.appcontext.bean.User;
import com.mirror.appcontext.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author mirror
 */
public class Main {
    public static void main(String[] args) {
        //创建一个spring的IOC容器实例
        //加载配置文件
        //Spring容器为我们创建并装配好配置文件中指定的所有Bean
        ApplicationContext context = new ClassPathXmlApplicationContext("com.mirror.appcontext/application.xml");
        //获取Bean
        UserService userService = context.getBean(UserService.class);
        //正常调用
        User user = userService.login("bob@example.com", "password");
        System.out.println(user.getName());

        //另外一种IOC容器
//        BeanFactory factory = new XmlBeanFactory(new ClassPathResource("application.xml"));
//        MailService mailService = factory.getBean(MailService.class);
    }
}