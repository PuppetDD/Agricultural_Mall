package com.gold.mall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GOLD
 * @date 2020/2/23 12:21
 * Description <后台权限>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    /**
     * 权限id
     */
    private Integer id;
    /**
     * 权限名称
     */
    private String roleName;

}
