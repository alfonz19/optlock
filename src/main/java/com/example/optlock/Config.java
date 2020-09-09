package com.example.optlock;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.example.optlock")
@EntityScan("com.example.optlock")
public class Config {
    @Bean
    public DataSource customDataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties
                .initializeDataSourceBuilder().type(HikariDataSource.class).build();

        return ProxyDataSourceBuilder.create(dataSource)
                .name(properties.getName())
                .logQueryBySlf4j(SLF4JLogLevel.DEBUG)
                .build();
    }
}
