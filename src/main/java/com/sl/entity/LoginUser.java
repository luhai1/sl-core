package com.sl.entity;

import lombok.Data;

import java.util.List;

@Data
public class LoginUser extends BaseEntity {
    /**
     * 登录生成token
     */
    private String token;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 省份证
     */
    private String idCard;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 手机号
     */
    private String mobilePhone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户对应角色
     */
    private List<SysRole> roleList;
}
