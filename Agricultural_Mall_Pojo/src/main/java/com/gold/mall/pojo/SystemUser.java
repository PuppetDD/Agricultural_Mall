package com.gold.mall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author GOLD
 * @date 2020/2/23 12:20
 * Description <系统管理员>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemUser {

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 是否禁用 0表示禁用 1表示启用
     */
    private Integer isValid;
    /**
     * 用户创建时间
     */
    private Date createDate;
    /**
     * 角色 这里也直接可以放角色id
     */
    private Role role;

}
