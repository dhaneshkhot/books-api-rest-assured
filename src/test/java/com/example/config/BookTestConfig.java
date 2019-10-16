package com.example.config;

import com.example.utils.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class BookTestConfig {
    @Bean
    public static DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Properties.getDriverClassName());
        dataSource.setUrl(Properties.getMySQlConnectionString());
        dataSource.setUsername(Properties.getDbUsername());
        dataSource.setPassword(Properties.getDbPassword());
        return  dataSource;
    }

    @Bean
    public static JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

}
