package com.mdata.thirdparty.dianli.frontend.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by administrator on 16/5/14.
 */
@Configuration
public class MainConfiguration {
    @Bean(name="mysqlDS")
    @Primary
    @ConfigurationProperties(prefix="datasource.mysql")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name="sqliteDS")
    @ConfigurationProperties(prefix="datasource.sqlite")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    @Bean
    @Primary
    public JdbcTemplate mysqlTemplate(@Qualifier("mysqlDS") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    @Bean(name="sqliteTemplate")
    public JdbcTemplate sqliteTemplate(@Qualifier("sqliteDS") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
