package com.gold.mall.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gold.mall.common.constant.PaginationConstant;
import com.gold.mall.common.utils.CommonUtils;
import com.gold.mall.common.utils.ResponseResult;
import com.gold.mall.params.SystemUserParam;
import com.gold.mall.pojo.Role;
import com.gold.mall.pojo.SystemUser;
import com.gold.mall.service.RoleService;
import com.gold.mall.service.SystemUserService;
import com.gold.mall.vo.SystemUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GOLD
 * @date 2020/2/24 15:03
 * Description <后台系统管理员接口>
 */
@Slf4j
@Controller
@RequestMapping("/admin/system_user/manager")
public class SystemUserController {

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private RoleService roleService;

    /**
     * @param [loginName, password, session, model]
     * @return {@link String}
     * Description <需要实现登录>
     * @author GOLD
     * @date 2020/2/24 15:20
     */
    @RequestMapping("login")
    public String login(String loginName, String password, HttpSession session, Model model) {
        //实现登录
        try {
            password = CommonUtils.MD5(password);
            SystemUser systemUser = systemUserService.login(loginName, password);
            systemUser.setPassword(null);
            session.setAttribute("systemUser", systemUser);
            return "main";
        } catch (Exception e) {
            model.addAttribute("failMsg", e.getMessage());
            return "login";
        }
    }

    @RequestMapping("systemUserLogout")
    @ResponseBody
    public ResponseResult systemUserLogout(HttpSession session) {
        session.invalidate();
        return ResponseResult.success();
    }

    /**
     * @param [pageNum, model]
     * @return {@link String}
     * Description <获取所有系统用户类表，分页查询>
     * @author GOLD
     * @date 2020/2/24 15:20
     */
    @RequestMapping("getAllSystemUsers")
    public String getAllSystemUsers(Integer pageNum, Model model) {
        if (ObjectUtils.isEmpty(pageNum)) {
            pageNum = PaginationConstant.PAGE_NUM;
        }
        PageHelper.startPage(pageNum, PaginationConstant.PAGE_SIZE);
        List<SystemUser> systemUserList = systemUserService.findAllSystemUsers();

        PageInfo<SystemUser> pageInfo = new PageInfo<>(systemUserList);

        model.addAttribute("pageInfo", pageInfo);
        return "systemUserManager";
    }

    /**
     * @param [systemUserParam, pageNum, model]
     * @return {@link String}
     * Description <通过多个条件查询系统用户的列表>
     * @author GOLD
     * @date 2020/2/24 15:20
     */
    @RequestMapping("findSystemUserByParams")
    public String findSystemUserByParams(SystemUserParam systemUserParam, Integer pageNum, Model model) {
        if (ObjectUtils.isEmpty(pageNum)) {
            pageNum = PaginationConstant.PAGE_NUM;
        }

        PageHelper.startPage(pageNum, PaginationConstant.PAGE_SIZE);
        List<SystemUser> systemUserList = systemUserService.findSystemUsersByParams(systemUserParam);
        //数据封装分页
        PageInfo<SystemUser> pageInfo = new PageInfo<>(systemUserList);

        //实现数据的回显，将数据放到 model中
        model.addAttribute("params", systemUserParam);
        model.addAttribute("pageInfo", pageInfo);

        return "systemUserManager";
    }

    /**
     * @param []
     * @return {@link List< Role>}
     * Description <页面初始化，把角色的数据加载到页面中>
     * @author GOLD
     * @date 2020/2/24 15:21
     */
    @ModelAttribute("roles")
    public List<Role> loadRoles() {
        List<Role> roles = roleService.findAllRoles();
        return roles;
    }

    /**
     * @param [systemUserVo]
     * @return {@link ResponseResult}
     * Description <添加一个系统用户>
     * @author GOLD
     * @date 2020/2/24 15:21
     */
    @RequestMapping("addSystemUser")
    @ResponseBody
    public ResponseResult addSystemUser(SystemUserVo systemUserVo) {

        try {
            String md5Pwd = CommonUtils.MD5(systemUserVo.getPassword());
            systemUserVo.setPassword(md5Pwd);
        } catch (Exception e) {
            log.info("加密失败");
            return ResponseResult.fail("用户添加失败");
        }

        int rows = systemUserService.addSystemUser(systemUserVo);
        if (rows >= 1) {
            return ResponseResult.success("用户添加成功");
        } else {
            return ResponseResult.fail("用户添加失败");
        }
    }

    /**
     * @param [id]
     * @return {@link ResponseResult}
     * Description <修改系统用户的状态，启用或禁用账户>
     * @author GOLD
     * @date 2020/2/24 15:21
     */
    @RequestMapping("modifySystemUserStatus")
    @ResponseBody
    public ResponseResult modifySystemUserStatus(int id) {
        int rows = systemUserService.modifySystemUserStatus(id);
        if (rows >= 1) {
            return ResponseResult.success("操作成功");
        } else {
            return ResponseResult.success("操作失败");
        }
    }

    /**
     * @param [id]
     * @return {@link ResponseResult}
     * Description <查询某个系统用户信息>
     * @author GOLD
     * @date 2020/2/24 15:21
     */
    @RequestMapping("findSystemUser")
    @ResponseBody
    public ResponseResult findSystemUser(int id) {
        SystemUser systemUser = systemUserService.findSystemUserById(id);
        if (systemUser != null) {
            return ResponseResult.success(systemUser);
        } else {
            return ResponseResult.fail("该系统用户不存在");
        }
    }

    /**
     * @param [systemUserVo, pageNum, model]
     * @return {@link String}
     * Description <更新系统用户信息>
     * @author GOLD
     * @date 2020/2/24 15:22
     */
    @RequestMapping("modifySystemUser")
    public String modifySystemUser(SystemUserVo systemUserVo, Integer pageNum, Model model) {
        int rows = systemUserService.modifySystemUser(systemUserVo);
        if (rows >= 1) {
            model.addAttribute("successMsg", "修改成功");
        } else {
            model.addAttribute("failMsg", "修改失败");
        }

        return "forward:getAllSystemUsers?pageNum=" + pageNum;
    }

    /**
     * @param [loginName]
     * @return {@link Map< String, Object>}
     * Description <校验登录账户名是否可用>
     * @author GOLD
     * @date 2020/2/24 15:22
     */
    @RequestMapping("checkSystemUserLoginName")
    @ResponseBody
    public Map<String, Object> checkSystemUserLoginName(String loginName) {
        SystemUser systemUser = systemUserService.findSystemUserByLoginName(loginName);
        Map<String, Object> map = new HashMap<>();
        if (systemUser != null) {
            map.put("valid", false);
            map.put("message", "该账号已经存在");
        } else {
            map.put("valid", true);
        }
        return map;
    }

    /**
     * @param [attributes]
     * @return {@link String}
     * Description <session 超时>
     * @author GOLD
     * @date 2020/2/24 15:22
     */
    @RequestMapping("sessionTimeOut")
    public String sessionTimeOut(RedirectAttributes attributes) {
        attributes.addFlashAttribute("sessionTimeOut", "session超时");
        return "redirect:/admin/system_user/manager/login";
    }

}
