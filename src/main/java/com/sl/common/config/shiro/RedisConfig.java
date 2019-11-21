package com.sl.common.config.shiro;

import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.database}")
    private Integer database;

    @Value("${spring.redis.password}")
    private String password;
    @Value("${shiro.redis.expire}")
    private int expire;
    @Value("${shiro.redis.timeout}")
    private int timeout;

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean(name="redisManager")
    public  RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setDatabase(database);
//        redisManager.setPassword(password);
        redisManager.setTimeout(timeout);
        return redisManager;
    }
}
