package com.sl.common.util;

import org.apache.commons.lang3.StringUtils;
import org.crazycake.shiro.RedisManager;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Component
public class JedisUtil {
    private JedisUtil(){}

    @Resource
    private   RedisManager autoRedisManager;

    private  static RedisManager redisManager;
    private  static JedisPool jedisPool;



    @PostConstruct
    public void init() {
        JedisPool P=  autoRedisManager.getJedisPool();
        redisManager = autoRedisManager;
        jedisPool =  redisManager.getJedisPool();
    }
    public static Jedis getJedis(){

        return jedisPool.getResource();
    }

    /**
     * 设置值
     * @param key
     * @param value
     */
    public static void set(String key,String value){
        if(StringUtils.isEmpty(key)){
            return;
        }
        getJedis().set(key,value);
    }

    /**
     * 设置值、有效时间
     * @param key
     * @param value
     * @param expire
     */
    public static void set(String key,String value,Integer expire){
        if(StringUtils.isEmpty(key)){
            return;
        }
        Jedis jedis = getJedis();
        if(null != expire){
            jedis.expire(key,expire);
        }
        jedis.set(key,value);
    }

    /**
     * 根据key获取value
     * @param key
     * @return
     */
    public static String get(String key){
        if(StringUtils.isEmpty(key)){
            return null;
        }
        return getJedis().get(key);
    }

    /**
     * 正则匹配所有key
     * @param pattern
     * @return
     */
    public static Set<String> getKeys(String pattern){
        if(StringUtils.isEmpty(pattern)){
            pattern = "*";
        }


        return getJedis().keys(pattern);
    }

    public static Set<String> getKeys(){
        return getKeys("*");
    }


    public static void deleteKey(String key){
        if(StringUtils.isEmpty(key)){
            return;
        }
        getJedis().del(key);
    }

    public static void deleteKeys(String[] keys){
        if(keys.length<1){
            return;
        }
        for (String key : keys){
            deleteKey(key);
        }
    }

    public static String clear(){
        return getJedis().flushAll();
    }
}
