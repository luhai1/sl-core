package com.sl.entity;

import com.sl.common.excel.annotation.Excel;
import com.sl.common.excel.annotation.ExcelColumn;
import lombok.Data;

import java.util.List;

@Data
@Excel(name="用户信息")
public class LoginUser extends BaseEntity {
    /**
     * 登录生成token
     */
    private String token;
    /**
     * 用户名
     */
    @ExcelColumn(name = "用户名",index = 3)
    private String userName;
    /**
     * 用户密码
     */
    @ExcelColumn(name = "用户密码",index = 2)
    private String password;
    /**
     * 真实姓名
     */
    @ExcelColumn(name = "真实姓名",index = 0)
    private String realName;
    /**
     * 省份证
     */
    @ExcelColumn(name = "省份证",index = 1)
    private String idCard;
    /**
     * 性别
     */
    @ExcelColumn(name = "性别",exEl="sex==1?'男':'女'",index = 6)
    private Integer sex;
    /**
     * 手机号
     */
    @ExcelColumn(name = "手机号",index = 4)
    private String mobilePhone;
    /**
     * 邮箱
     */
    @ExcelColumn(name = "邮箱",index = 5)
    private String email;
    /**
     * 用户对应角色
     */
    private List<SysRole> roleList;
}
