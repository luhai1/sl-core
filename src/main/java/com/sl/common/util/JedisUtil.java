package com.sl.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Set;

@Component
public class JedisUtil {
    private JedisUtil(){}
    @Autowired
    private static Jedis jedis;

    /**
     * 设置值
     * @param key
     * @param value
     */
    public static void set(String key,String value){
        if(StringUtils.isEmpty(key)){
            return;
        }
        jedis.set(key,value);
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
        return jedis.get(key);
    }

    /**
     * 正则匹配所有key
     * @param pattern
     * @return
     */
    public static Set<String> getKeys(String pattern){
        if(StringUtils.isEmpty(pattern)){
            pattern = "**";
        }
        return jedis.keys(pattern);
    }

    public static Set<String> getKeys(){
        return getKeys("**");
    }


    public static void deleteKey(String key){
        if(StringUtils.isEmpty(key)){
            return;
        }
    }

}
