package com.gold.mall.dao;

import com.gold.mall.pojo.Role;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 13:19
 * Description <后台权限Dao>
 */
public interface RoleDao {

    List<Role> selectAllRoles();

}
