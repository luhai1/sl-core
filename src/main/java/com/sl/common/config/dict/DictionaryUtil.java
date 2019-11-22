package com.sl.common.config.dict;

import com.sl.common.util.JedisUtil;
import com.sl.common.util.JsonUtil;
import com.sl.entity.DictionaryEntity;
import com.sl.entity.DictionaryItem;
import com.sl.service.DictionaryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Configuration
@DependsOn("jedisUtil")
public class DictionaryUtil {
    private static final String DICT_REDIS_DICT_KEY = "dict.redis.dictKey";
    private static final String DICT_REDIS_DICT_ITEM_KEY = "dict.redis.dictItemKey";
    private static final String DICT_REDIS_DICT_SPLIT = ".";
    @Resource
    DictionaryService dictionaryService;
    public  DictionaryUtil(){}

    /**
     * 初始化字典放入redis
     */
    @PostConstruct
    public void  initDict(){
        List<DictionaryEntity> dictionaryEntityList = dictionaryService.getAllDict();
        for(DictionaryEntity dictionaryEntity : dictionaryEntityList){
            JedisUtil.set(DICT_REDIS_DICT_KEY+DICT_REDIS_DICT_SPLIT+dictionaryEntity.getDictCode(), JsonUtil.toJson(dictionaryEntity));
            if(CollectionUtils.isNotEmpty(dictionaryEntity.getDictionaryItemList())){
                initItemDict(dictionaryEntity.getDictionaryItemList());
            }
        }
    }

    /**
     * 初始化字典放入redis
     * @param dictionaryItemList
     */
    private void  initItemDict(List<DictionaryItem> dictionaryItemList){
        if(CollectionUtils.isNotEmpty(dictionaryItemList)){
            for(DictionaryItem dictionaryItem :dictionaryItemList){
                JedisUtil.set(DICT_REDIS_DICT_ITEM_KEY+DICT_REDIS_DICT_SPLIT+dictionaryItem.getItemCode(), JsonUtil.toJson(dictionaryItem));
                if(CollectionUtils.isNotEmpty(dictionaryItem.getDictionaryItemList())){
                    initItemDict(dictionaryItem.getDictionaryItemList());
                }
            }
        }

    }

    /**
     * 获取字典以及下面数据列表
     * @param dictCode
     * @return
     */
    public static DictionaryEntity getDictValue(String dictCode){
        DictionaryEntity dictionaryItem = null;
        if(StringUtils.isNotBlank(dictCode)){
            dictionaryItem = JsonUtil.fromJson(JedisUtil.get(DICT_REDIS_DICT_KEY+DICT_REDIS_DICT_SPLIT+dictCode),DictionaryEntity.class);
        }
        return dictionaryItem;
    }

    /**
     * 根据itemCode获取对应字典值
     * @param itemCode
     * @return
     */
    public static String getDictItemValue(String itemCode){
        if(StringUtils.isNotBlank(itemCode)){
            DictionaryItem dictionaryItem = JsonUtil.fromJson(JedisUtil.get(DICT_REDIS_DICT_ITEM_KEY+DICT_REDIS_DICT_SPLIT+itemCode),DictionaryItem.class);
            if(null !=dictionaryItem ){
                return dictionaryItem.getItemValue();
            }
        }
        return null;
    }


}
