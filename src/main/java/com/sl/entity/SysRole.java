package com.sl.entity;

import lombok.Data;

import java.util.List;

@Data
public class SysRole extends BaseEntity {
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色对应资源
     */
    private List<SysResources> resourcesList;

}
