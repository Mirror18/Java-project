package com.mirror.annotation;

import com.mirror.annotation.bean.User;
import com.mirror.annotation.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Properties;

/**
 * @author mirror
 */
@Configuration
@ComponentScan
@PropertySource("app.properties")
public class AppConfig {
    //创建一个Bean
    @Bean(name = "dataSource")
    public HikariDataSource getDataSource() {
        Properties props = new Properties();
        props.setProperty("jdbcUrl", "jdbc:mysql://localhost:3306/springIoc?characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false");
        props.setProperty("username", "root");
        props.setProperty("password", "root");
        HikariConfig config = new HikariConfig(props);
        config.addDataSourceProperty("cachePrepStmts", "true");
        return new HikariDataSource(config);
    }

    @Value("${app.zone:Z}")
    String zoneId;

    @Bean("z")
    @Primary
    ZoneId createZoneOfZ() {
        return ZoneId.of("Z");
    }

    @Bean
    @Qualifier("utc8")
    ZoneId createZoneOfUTC8() {
        return ZoneId.of("UTC+08:00");
    }

    @Bean
    @Profile("!test")
    ZoneId createZoneId() {
        return ZoneId.systemDefault();
    }

    @Bean
    @Profile("test")
    ZoneId createZoneIdForTest() {
        return ZoneId.of("America/New_York");
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        User user = null;
        try {
            user = userService.login("bob@example.com", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert user != null;
        System.out.println(user.getName());
    }
}