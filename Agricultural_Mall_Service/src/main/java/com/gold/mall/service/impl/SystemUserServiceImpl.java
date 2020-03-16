package com.gold.mall.service.impl;

import com.gold.mall.common.constant.SystemUserConstant;
import com.gold.mall.common.exception.SystemUserLoginException;
import com.gold.mall.dao.SystemUserDao;
import com.gold.mall.params.SystemUserParam;
import com.gold.mall.pojo.Role;
import com.gold.mall.pojo.SystemUser;
import com.gold.mall.service.SystemUserService;
import com.gold.mall.vo.SystemUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 18:06
 * Description <系统管理员impl>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SystemUserServiceImpl implements SystemUserService {

    @Autowired
    private SystemUserDao systemUserDao;

    /**
     * @param []
     * @return {@link List< SystemUser>}
     * Description <查询所有系统用户>
     * @author GOLD
     * @date 2020/2/24 13:40
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<SystemUser> findAllSystemUsers() {
        return systemUserDao.selectAllSystemUsers();
    }

    /**
     * @param [id]
     * @return {@link SystemUser}
     * Description <根据 id查询某一具体系统用户信息>
     * @author GOLD
     * @date 2020/2/24 13:40
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public SystemUser findSystemUserById(int id) {
        return systemUserDao.selectSystemUserById(id);
    }

    /**
     * @param [systemUserVo]
     * @return {@link int}
     * Description <插入一条系统用户信息>
     * @author GOLD
     * @date 2020/2/24 13:41
     */
    @Override
    public int addSystemUser(SystemUserVo systemUserVo) {

        SystemUser systemUser = new SystemUser();
        Role role = new Role();

        BeanUtils.copyProperties(systemUserVo, systemUser);

        //默认新创建的用户有效
        systemUser.setIsValid(SystemUserConstant.SYSTEM_USER_IS_VALIDAT);

        role.setId(systemUserVo.getRoleId());
        systemUser.setRole(role);
        //当前时间即为创建时间
        systemUser.setCreateDate(new Date());

        return systemUserDao.insertSystemUser(systemUser);
    }

    /**
     * @param [systemUserVo]
     * @return {@link int}
     * Description <修改系统用户信息>
     * @author GOLD
     * @date 2020/2/24 13:41
     */
    @Override
    public int modifySystemUser(SystemUserVo systemUserVo) {

        SystemUser systemUser = systemUserDao.selectSystemUserById(systemUserVo.getId());
        if (systemUser != null) {
            Role role = new Role();
            role.setId(systemUserVo.getRoleId());
            systemUser.setRole(role);
            systemUser.setName(systemUserVo.getName());
            systemUser.setEmail(systemUserVo.getEmail());
            systemUser.setPhone(systemUserVo.getPhone());
        }
        return systemUserDao.updateSystemUser(systemUser);
    }

    /**
     * @param [id]
     * @return {@link int}
     * Description <启用 禁用系统用户状态>
     * @author GOLD
     * @date 2020/2/24 13:41
     */
    @Override
    public int modifySystemUserStatus(int id) {
        SystemUser systemUser = systemUserDao.selectSystemUserById(id);
        int isValid = systemUser.getIsValid();
        if (systemUser != null) {
            if (isValid == SystemUserConstant.SYSTEM_USER_IS_VALIDAT) {
                //如果是启用状态，则改为禁用
                isValid = SystemUserConstant.SYSTEM_USER_IS_INVALIDAT;
            } else {
                isValid = SystemUserConstant.SYSTEM_USER_IS_VALIDAT;
            }
        }
        return systemUserDao.updateSystemUserStatus(id, isValid);
    }

    /**
     * @param [systemUserParam]
     * @return {@link List< SystemUser>}
     * Description <多条件查询系统用户列表>
     * @author GOLD
     * @date 2020/2/24 13:41
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<SystemUser> findSystemUsersByParams(SystemUserParam systemUserParam) {
        return systemUserDao.selectSystemUserByParams(systemUserParam);
    }

    /**
     * @param [loginName]
     * @return {@link SystemUser}
     * Description <查询登录名是否可用>
     * @author GOLD
     * @date 2020/2/24 13:41
     */
    @Override
    public SystemUser findSystemUserByLoginName(String loginName) {
        return systemUserDao.selectSystemUserByLoginName(loginName);
    }

    /**
     * @param [loginName, password]
     * @return {@link SystemUser}
     * Description <实现登录功能>
     * @author GOLD
     * @date 2020/2/24 13:42
     */
    @Override
    public SystemUser login(String loginName, String password) throws SystemUserLoginException {
        SystemUser systemUser = systemUserDao.selectSystemUserByLoginNameAndPassword(loginName,
                password, SystemUserConstant.SYSTEM_USER_IS_VALIDAT);
        if (systemUser != null) {
            return systemUser;
        }
        throw new SystemUserLoginException("用户名或密码不正确");
    }


}
