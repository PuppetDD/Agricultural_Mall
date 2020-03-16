package com.gold.mall.dao;

import com.gold.mall.params.SystemUserParam;
import com.gold.mall.pojo.SystemUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 13:19
 * Description <系统管理员Dao>
 */
public interface SystemUserDao {

    List<SystemUser> selectAllSystemUsers();

    SystemUser selectSystemUserById(int id);

    SystemUser selectSystemUserByLoginName(String loginName);

    List<SystemUser> selectSystemUserByParams(SystemUserParam systemUserParam);

    SystemUser selectSystemUserByLoginNameAndPassword(@Param("loginName") String loginName,
                                                      @Param("password") String password,
                                                      @Param("isValid") int isValid);

    int insertSystemUser(SystemUser systemUser);

    int updateSystemUser(SystemUser systemUser);

    int updateSystemUserStatus(@Param("id") int id, @Param("isValid") int isValid);

}
