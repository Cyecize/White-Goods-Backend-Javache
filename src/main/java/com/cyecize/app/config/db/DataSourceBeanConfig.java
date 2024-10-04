package com.cyecize.app.config.db;

import com.cyecize.ioc.annotations.Nullable;
import com.cyecize.summer.common.annotations.Bean;
import com.cyecize.summer.common.annotations.BeanConfig;
import com.cyecize.summer.common.annotations.Configuration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;

@BeanConfig
@RequiredArgsConstructor
public class DataSourceBeanConfig {

    @Configuration("persistence.unit.name")
    private final String persistenceUnitName;

    @Configuration("database.connection.url")
    private final String connectionUrl;

    @Configuration("database.username")
    private final String databaseUsername;

    @Nullable
    @Configuration("database.password")
    private final String databasePassword;

    @Configuration("database.driver.class")
    private final String databaseDriverClass;

    @Configuration("database.connection.timeout")
    private final int databaseConnectionTimeout;

    @Configuration("database.pool.max.size")
    private final int databasePoolMaxSize;

    @Bean
    public DataSource dataSource() {
        final HikariConfig config = new HikariConfig();
        config.setPoolName(this.persistenceUnitName + "_hikariPool");
        config.setJdbcUrl(this.connectionUrl);
        config.setUsername(this.databaseUsername);
        config.setPassword(this.databasePassword);
        config.setDriverClassName(this.databaseDriverClass);
        config.setConnectionTimeout(this.databaseConnectionTimeout);
        config.setAutoCommit(false);
        config.setMaximumPoolSize(this.databasePoolMaxSize);

        return new HikariDataSource(config);
    }
}
