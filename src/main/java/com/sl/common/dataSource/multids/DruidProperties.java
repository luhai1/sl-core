package com.sl.common.dataSource.multids;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Data
public class DruidProperties {
    private String validationQuery = "SELECT 'x' FROM DUAL";

    private int initialSize = 10;

    private int minIdle = 10;

    private int maxActive = 20;

    private int maxWait = 50000;

    private int minEvictableIdleTimeMillis = 300000;

    private int timeBetweenEvictionRunsMillis = 90000;


    private boolean testOnBorrow = false;


    private boolean testOnReturn = false;

    private boolean testWhileIdle = true;

    private boolean poolPreparedStatements = true;

    private int maxPoolPreparedStatementPerConnectionSize = 20;
    private String dsName;
    private boolean primary;
    private String driverClassName;
    private String url;

    public String getDsName() {
        return this.dsName;
    }

    private String username;
    private String password;
    private String filters;
    private String connectionProperties;

    public boolean isPrimary() {
        return this.primary;
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFilters() {
        return this.filters;
    }

    public String getConnectionProperties() {
        return this.connectionProperties;
    }

    public String getValidationQuery() {
        return this.validationQuery;
    }

    public int getInitialSize() {
        return this.initialSize;
    }

    public int getMinIdle() {
        return this.minIdle;
    }

    public int getMaxActive() {
        return this.maxActive;
    }

    public int getMaxWait() {
        return this.maxWait;
    }

    public int getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }

    public boolean isTestOnBorrow() {
        return this.testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return this.testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return this.testWhileIdle;
    }

    public boolean isPoolPreparedStatements() {
        return this.poolPreparedStatements;
    }

    public int getMaxPoolPreparedStatementPerConnectionSize() {
        return this.maxPoolPreparedStatementPerConnectionSize;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public void setConnectionProperties(String connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof DruidProperties)) return false;
        DruidProperties other = (DruidProperties) o;
        if (!other.canEqual(this)) return false;
        Object thisdsName = getDsName(), otherdsName = other.getDsName();
        if (!Objects.equals(thisdsName, otherdsName)) return false;
        if (isPrimary() != other.isPrimary()) return false;
        Object thisdriverClassName = getDriverClassName(), otherdriverClassName = other.getDriverClassName();
        if (!Objects.equals(thisdriverClassName, otherdriverClassName))
            return false;
        Object thisurl = getUrl(), otherurl = other.getUrl();
        if (!Objects.equals(thisurl, otherurl)) return false;
        Object thisusername = getUsername(), otherusername = other.getUsername();
        if (!Objects.equals(thisusername, otherusername)) return false;
        Object thispassword = getPassword(), otherpassword = other.getPassword();
        if (!Objects.equals(thispassword, otherpassword)) return false;
        Object thisfilters = getFilters(), otherfilters = other.getFilters();
        if (!Objects.equals(thisfilters, otherfilters)) return false;
        Object thisconnectionProperties = getConnectionProperties(), otherconnectionProperties = other.getConnectionProperties();
        if (!Objects.equals(thisconnectionProperties, otherconnectionProperties))
            return false;
        Object thisvalidationQuery = getValidationQuery(), othervalidationQuery = other.getValidationQuery();
        return (Objects.equals(thisvalidationQuery, othervalidationQuery)) && ((getInitialSize() != other.getInitialSize()) ? false : ((getMinIdle() != other.getMinIdle()) ? false : ((getMaxActive() != other.getMaxActive()) ? false : ((getMaxWait() != other.getMaxWait()) ? false : ((getMinEvictableIdleTimeMillis() != other.getMinEvictableIdleTimeMillis()) ? false : ((getTimeBetweenEvictionRunsMillis() != other.getTimeBetweenEvictionRunsMillis()) ? false : ((isTestOnBorrow() != other.isTestOnBorrow()) ? false : ((isTestOnReturn() != other.isTestOnReturn()) ? false : ((isTestWhileIdle() != other.isTestWhileIdle()) ? false : ((isPoolPreparedStatements() != other.isPoolPreparedStatements()) ? false : (!(getMaxPoolPreparedStatementPerConnectionSize() != other.getMaxPoolPreparedStatementPerConnectionSize()))))))))))));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DruidProperties;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object dsName = getDsName();
        result = result * 59 + ((dsName == null) ? 43 : dsName.hashCode());
        result = result * 59 + (isPrimary() ? 79 : 97);
        Object driverClassName = getDriverClassName();
        result = result * 59 + ((driverClassName == null) ? 43 : driverClassName.hashCode());
        Object url = getUrl();
        result = result * 59 + ((url == null) ? 43 : url.hashCode());
        Object username = getUsername();
        result = result * 59 + ((username == null) ? 43 : username.hashCode());
        Object password = getPassword();
        result = result * 59 + ((password == null) ? 43 : password.hashCode());
        Object filters = getFilters();
        result = result * 59 + ((filters == null) ? 43 : filters.hashCode());
        Object connectionProperties = getConnectionProperties();
        result = result * 59 + ((connectionProperties == null) ? 43 : connectionProperties.hashCode());
        Object validationQuery = getValidationQuery();
        result = result * 59 + ((validationQuery == null) ? 43 : validationQuery.hashCode());
        result = result * 59 + getInitialSize();
        result = result * 59 + getMinIdle();
        result = result * 59 + getMaxActive();
        result = result * 59 + getMaxWait();
        result = result * 59 + getMinEvictableIdleTimeMillis();
        result = result * 59 + getTimeBetweenEvictionRunsMillis();
        result = result * 59 + (isTestOnBorrow() ? 79 : 97);
        result = result * 59 + (isTestOnReturn() ? 79 : 97);
        result = result * 59 + (isTestWhileIdle() ? 79 : 97);
        result = result * 59 + (isPoolPreparedStatements() ? 79 : 97);
        return result * 59 + getMaxPoolPreparedStatementPerConnectionSize();
    }

    public String toString() {
        return "DruidProperties(dsName=" + getDsName() + ", primary=" + isPrimary() + ", driverClassName=" + getDriverClassName() + ", url=" + getUrl() + ", username=" + getUsername() + ", password=" + getPassword() + ", filters=" + getFilters() + ", connectionProperties=" + getConnectionProperties() + ", validationQuery=" + getValidationQuery() + ", initialSize=" + getInitialSize() + ", minIdle=" + getMinIdle() + ", maxActive=" + getMaxActive() + ", maxWait=" + getMaxWait() + ", minEvictableIdleTimeMillis=" + getMinEvictableIdleTimeMillis() + ", timeBetweenEvictionRunsMillis=" + getTimeBetweenEvictionRunsMillis() + ", testOnBorrow=" + isTestOnBorrow() + ", testOnReturn=" + isTestOnReturn() + ", testWhileIdle=" + isTestWhileIdle() + ", poolPreparedStatements=" + isPoolPreparedStatements() + ", maxPoolPreparedStatementPerConnectionSize=" + getMaxPoolPreparedStatementPerConnectionSize() + ")";
    }

}
