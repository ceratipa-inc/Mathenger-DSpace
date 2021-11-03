package com.example.mathengerapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;

@TestConfiguration
public class MathengerTestConfiguration {
    @Value("${docker.mysql.username}")
    private String mySqlUserName;
    @Value("{docker.mysql.password}")
    private String mySqlPassword;

    @Bean
    public MySQLContainer mySQLContainer() {
        var container = new MySQLContainer<>("mysql:8.0.19")
                .withUsername(mySqlUserName)
                .withPassword(mySqlPassword)
                .withDatabaseName("mathenger")
                .withExposedPorts(3306);
        container.start();
        return container;
    }

    @Bean
    public DataSource dataSource(MySQLContainer mySQLContainer) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(mySQLContainer.getJdbcUrl());
        config.setUsername(mySqlUserName);
        config.setPassword(mySqlPassword);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return new HikariDataSource(config);
    }
}
