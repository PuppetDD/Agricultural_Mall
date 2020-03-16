package com.gold.mall.service;

import com.gold.mall.common.exception.SystemUserLoginException;
import com.gold.mall.params.SystemUserParam;
import com.gold.mall.pojo.SystemUser;
import com.gold.mall.vo.SystemUserVo;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 16:28
 * Description <系统管理员Service>
 */
public interface SystemUserService {

    List<SystemUser> findAllSystemUsers();

    SystemUser findSystemUserById(int id);

    int addSystemUser(SystemUserVo systemUserVo);

    int modifySystemUser(SystemUserVo systemUserVo);

    int modifySystemUserStatus(int id);

    List<SystemUser> findSystemUsersByParams(SystemUserParam systemUserParam);

    SystemUser findSystemUserByLoginName(String loginName);

    SystemUser login(String loginName, String password) throws SystemUserLoginException;

}
