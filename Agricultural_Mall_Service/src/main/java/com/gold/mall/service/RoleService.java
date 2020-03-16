package com.gold.mall.service;

import com.gold.mall.pojo.Role;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 16:28
 * Description <后台权限Service>
 */
public interface RoleService {

    List<Role> findAllRoles();

}
