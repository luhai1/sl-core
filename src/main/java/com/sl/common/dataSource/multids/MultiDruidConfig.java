package com.sl.common.dataSource.multids;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class MultiDruidConfig {
    @Autowired
    private MultiDruidProperties properties;

    @Bean
    public DataSource dataSource() {
        if (this.properties.getMulti() == null || this.properties.getMulti().isEmpty()) {
            throw new BeanCreationException("sl-boot-starter-multids" );
        }
        MultiDataSource multipleDataSource = new MultiDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        DruidDataSource defaultDataSource = null;
        for (DruidProperties p : this.properties.getMulti()) {
            DruidDataSource datasource = new DruidDataSource();
            datasource.setUrl(p.getUrl());
            datasource.setUsername(p.getUsername());
            datasource.setPassword(p.getPassword());
            datasource.setDriverClassName(p.getDriverClassName());
            datasource.setInitialSize(p.getInitialSize());
            datasource.setMinIdle(p.getMinIdle());
            datasource.setMaxActive(p.getMaxActive());
            datasource.setMaxWait(p.getMaxWait());
            datasource.setTimeBetweenEvictionRunsMillis(p.getTimeBetweenEvictionRunsMillis());
            datasource.setMinEvictableIdleTimeMillis(p.getMinEvictableIdleTimeMillis());
            datasource.setValidationQuery(p.getValidationQuery());
            datasource.setTestWhileIdle(p.isTestWhileIdle());
            datasource.setTestOnBorrow(p.isTestOnBorrow());
            datasource.setTestOnReturn(p.isTestOnReturn());
            datasource.setPoolPreparedStatements(p.isPoolPreparedStatements());
            datasource.setMaxPoolPreparedStatementPerConnectionSize(p.getMaxPoolPreparedStatementPerConnectionSize());
            if (p.getFilters() != null && !"".equals(p.getFilters())) {
                try {
                    datasource.setFilters(p.getFilters());
                } catch (SQLException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            if (p.getConnectionProperties() != null && !"".equals(p.getConnectionProperties())) {
                datasource.setConnectionProperties(p.getConnectionProperties());
            }
            if (p.isPrimary()) {
                defaultDataSource = datasource;
            }
            String dsName = p.getDsName();
            if (Objects.isNull(dsName) || "".equals(dsName)) {
                dsName = p.getUrl();
            }
            targetDataSources.put(dsName, datasource);
        }
        if (defaultDataSource == null) {
            throw new BeanCreationException("请设置一个数据源作为主数据源");
        }

        multipleDataSource.setTargetDataSources(targetDataSources);
        multipleDataSource.setDefaultTargetDataSource(defaultDataSource);
        return (DataSource)multipleDataSource;
    }


    @Bean
    public SwitchDataSourceAspect switchDataSourceAspect() { return new SwitchDataSourceAspect(); }
}
