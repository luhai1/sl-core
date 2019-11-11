package com.sl.entity;

import lombok.Data;

@Data
public class SysResources extends BaseEntity {
    /**
     * 资源编码
     */
    private String resourceCode;
    /**
     * 资源名称
     */
    private String display;
    /**
     * 资源描述
     */
    private String description;
    /**
     * 父资源code
     */
    private String parentCode;
    /**
     * 资源类型^1菜单，2URL，3按钮
     */
    private Integer resourceType;
    /**
     * 访问地址/按钮编码
     */
    private String url;
    /**
     * 图标
     */
    private String icon;
    /**
     * 排序
     */
    private Integer sortBy;

}
