package com.sl.entity;

import lombok.Data;

@Data
public class SysConfig extends BaseEntity{
    /**
     * 系统配置编码
     */
    private String configCode;
    /**
     * 系统配置名称
     */
    private String configName;
    /**
     * 系统配置值
     */
    private String configValue;
    /**
     * 系统配置描述
     */
    private String description;
}
