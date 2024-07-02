package com.mirror;

import com.mirror.config.MySqlConfig;
import com.mirror.entity.AbstractEntity;
import com.mirror.entity.User;
import com.mirror.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author mirror
 */
@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {
    @Autowired
    MySqlConfig mySqlConfig;

    @Bean
    DataSource createDataSource() throws IOException {
        HikariConfig config = mySqlConfig.getConfig();
        return new HikariDataSource(config);
    }

//    @Bean
//    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }


//    @Bean
//    DataSource createDataSource(
//            // JDBC URL:
//            @Value("${jdbc.url}") String jdbcUrl,
//            // JDBC username:
//            @Value("${jdbc.username}") String jdbcUsername,
//            // JDBC password:
//            @Value("${jdbc.password}") String jdbcPassword) {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(jdbcUrl);
//        config.setUsername(jdbcUsername);
//        config.setPassword(jdbcPassword);
//        config.addDataSourceProperty("autoCommit", "false");
//        config.addDataSourceProperty("connectionTimeout", "5");
//        config.addDataSourceProperty("idleTimeout", "60");
//        return new HikariDataSource(config);
//    }


//    //启动Hibernate
//    @Bean
//    LocalSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource) {
//        var props = new Properties();
//        props.setProperty("hibernate.hbm2ddl.auto", "update"); // 生产环境不要使用,自动创建数据库的表结构
//        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");//说明用的数据库是哪个
//        props.setProperty("hibernate.show_sql", "true");//打印sql语句
//        var sessionFactoryBean = new LocalSessionFactoryBean();//封装了DataSource的实例
//        sessionFactoryBean.setDataSource(dataSource);//持有连接池
//        // 扫描指定的package获取所有entity class:
//        sessionFactoryBean.setPackagesToScan("com.mirror.entity");
//        sessionFactoryBean.setHibernateProperties(props);
//        return sessionFactoryBean;
//    }
//
//    //配合hibernate使用声明式事务使用的
//    @Bean
//    PlatformTransactionManager createTxManager(@Autowired SessionFactory sessionFactory) {
//        return new HibernateTransactionManager(sessionFactory);
//    }

    @Bean
    public LocalContainerEntityManagerFactoryBean createEntityManagerFactory(@Autowired DataSource dataSource) {
        var emFactory = new LocalContainerEntityManagerFactoryBean();
        // 注入DataSource:
        emFactory.setDataSource(dataSource);
        // 扫描指定的package获取所有entity class:
        emFactory.setPackagesToScan(AbstractEntity.class.getPackageName());
        // 使用Hibernate作为JPA实现:
        emFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        // 其他配置项:
        var props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", "update"); // 生产环境不要使用
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        props.setProperty("hibernate.show_sql", "true");
        emFactory.setJpaProperties(props);
        return emFactory;
    }

    @Bean
    PlatformTransactionManager createTxManager(@Autowired EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        if (userService.fetchUserByEmail("bob@example.com") == null) {
            User bob = userService.register("bob@example.com", "bob123", "Bob");
            System.out.println("Registered ok: " + bob);
        }
        if (userService.fetchUserByEmail("alice@example.com") == null) {
            User alice = userService.register("alice@example.com", "helloalice", "Bob");
            System.out.println("Registered ok: " + alice);
        }
        // 查询所有用户:
        for (User u : userService.getUsers(1)) {
            System.out.println(u);
        }
        User bob = userService.login("bob@example.com", "bob123");
        System.out.println(bob);
        ((ConfigurableApplicationContext) context).close();
    }


}