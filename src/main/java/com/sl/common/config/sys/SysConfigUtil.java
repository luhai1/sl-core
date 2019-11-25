package com.sl.common.config.sys;

import com.sl.common.util.JedisUtil;
import com.sl.common.util.JsonUtil;
import com.sl.entity.DictionaryEntity;
import com.sl.entity.SysConfig;
import com.sl.service.SysConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Configuration
@DependsOn("jedisUtil")
public class SysConfigUtil {
    private static final String SYS_CONFIG_KEY = "sys.config";
    private static final String SYS_CONFIG_KEY_SPLIT = ".";

    @Resource
    SysConfigService sysConfigService;


    /**
     * 初始化系统配置放入redis
     */
    @PostConstruct
    public void  initDict(){
       List<SysConfig> sysConfigList = sysConfigService.getAllSysConfig();
       if(CollectionUtils.isNotEmpty(sysConfigList)){
           for(SysConfig sysConfig : sysConfigList){
               JedisUtil.set(SYS_CONFIG_KEY+SYS_CONFIG_KEY_SPLIT+sysConfig.getConfigCode(),sysConfig.getConfigValue());
           }
       }
    }

    public static String getConfigValue(String configCode){
        if(StringUtils.isBlank(configCode)){
            return null;
        }
        return JedisUtil.get(SYS_CONFIG_KEY+SYS_CONFIG_KEY_SPLIT+configCode);
    }
}
