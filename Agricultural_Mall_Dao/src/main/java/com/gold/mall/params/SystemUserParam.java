package com.gold.mall.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author GOLD
 * @date 2020/2/23 14:10
 * Description <描述>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserParam implements Serializable {

    /**
     * 姓名
     */
    private String name;
    /**
     * 用户名
     */
    private String loginName;
    /**
     * 电话
     */
    private String phone;
    /**
     * 角色id
     */
    private Integer roleId;
    /**
     * 状态
     */
    private Integer isValid;

}
