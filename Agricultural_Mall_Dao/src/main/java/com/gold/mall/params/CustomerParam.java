package com.gold.mall.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author GOLD
 * @date 2020/2/23 14:06
 * Description <描述>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerParam implements Serializable {

    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户登录账户名
     */
    private String loginName;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 用户地址
     */
    private String address;
    /**
     * 用户状态
     */
    private Integer isValid;

}
