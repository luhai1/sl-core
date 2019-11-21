package com.sl.common.config.dict;

import com.sl.common.util.JedisUtil;
import com.sl.common.util.JsonUtil;
import com.sl.entity.DictionaryEntity;
import com.sl.service.DictionaryService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Configuration
@DependsOn("jedisUtil")
public class DictionaryConfig {
    private static final String DICT_REDIS_KEY = "dict.redis.key";
    @Resource
    DictionaryService dictionaryService;

    @PostConstruct
    public void  initDict(){
        List<DictionaryEntity> dictionaryEntityList = dictionaryService.getAllDict();
        JedisUtil.set(DICT_REDIS_KEY, JsonUtil.toJson(dictionaryEntityList));
    }


}
