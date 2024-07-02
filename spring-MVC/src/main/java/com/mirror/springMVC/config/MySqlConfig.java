package com.mirror.springMVC.config;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author mirror
 */
@Component
@PropertySource("classpath:jdbc.properties")
public class MySqlConfig {
    @Value("${account}")
    String username;
    @Value("${jdbcUrl}")
    String jdbcUrl;
    @Value("${password}")
    String password;
    @Value("${connectionTimeout}")
    String connectionTimeout;
    @Value("${idleTimeout}")
    String idleTimeout;
    @Value("${maximumPoolSize}")
    String maximumPoolSize;
    @Value("${autoCommit}")
    String autoCommit;


    public HikariConfig getConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("connectionTimeout", connectionTimeout); // 连接超时
        config.addDataSourceProperty("idleTimeout", idleTimeout); // 空闲超时
        config.addDataSourceProperty("maximumPoolSize", maximumPoolSize); // 最大连接数
        config.addDataSourceProperty("autoCommit", autoCommit); // 自动提交
        return config;
    }

    public String getUsername() {
        return username;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getPassword() {
        return password;
    }

    public long getConnectionTimeout() {
        return Long.valueOf(connectionTimeout);

    }

    public String getAutoCommit() {
        return autoCommit;
    }

    public long getIdleTimeout() {
        return Long.valueOf(idleTimeout);
    }

    public int getMaximumPoolSize() {
        return Integer.valueOf(maximumPoolSize);
    }
}