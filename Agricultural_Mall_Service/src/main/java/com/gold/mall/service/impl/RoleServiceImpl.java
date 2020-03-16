package com.gold.mall.service.impl;

import com.gold.mall.dao.RoleDao;
import com.gold.mall.pojo.Role;
import com.gold.mall.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 18:06
 * Description <后台权限impl>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    /**
     * @param []
     * @return {@link List< Role>}
     * Description <显示所有权限>
     * @author GOLD
     * @date 2020/2/24 13:40
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<Role> findAllRoles() {
        return roleDao.selectAllRoles();
    }

}
