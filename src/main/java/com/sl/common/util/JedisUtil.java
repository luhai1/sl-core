package com.sl.common.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.crazycake.shiro.RedisManager;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Set;

@Component
@DependsOn("redisManager")
public class JedisUtil {
    private JedisUtil(){}

    @Resource
    private   RedisManager autoRedisManager;

    private  static RedisManager redisManager;



    @PostConstruct
    public void init() {
        JedisPool P=  autoRedisManager.getJedisPool();
        redisManager = autoRedisManager;
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
        set(key,value,Integer.MAX_VALUE);
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
        redisManager.set(key.getBytes(),value.getBytes(),expire);
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
        byte[] bytes = redisManager.get(key.getBytes());
        if(null !=bytes && bytes.length >0){
          return   new String(bytes);
        }
        return null;
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

        return  redisManager.getJedisPool().getResource().keys(pattern);
    }

    public static Set<String> getKeys(){
        return getKeys("*");
    }


    public static void deleteKey(String key){
        if(StringUtils.isEmpty(key)){
            return;
        }
        redisManager.del(key.getBytes());
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
        return redisManager.getJedisPool().getResource().flushAll();
    }
}
