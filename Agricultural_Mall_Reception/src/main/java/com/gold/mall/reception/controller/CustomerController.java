package com.gold.mall.reception.controller;

import com.gold.mall.common.exception.CustomerLoginNameIsExist;
import com.gold.mall.common.exception.PhoneNotRegisterException;
import com.gold.mall.common.utils.CommonUtils;
import com.gold.mall.common.utils.RedisUtil;
import com.gold.mall.common.utils.ResponseResult;
import com.gold.mall.pojo.Customer;
import com.gold.mall.service.CustomerService;
import com.gold.mall.vo.CustomerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GOLD
 * @date 2020/2/26 14:13
 * Description <客户接口>
 */
@Controller
@RequestMapping("/reception/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * @param [loginName, password, session, model]
     * @return {@link ResponseResult}
     * Description <通过账户名密码登录>
     * @author GOLD
     * @date 2020/2/26 17:03
     */
    @RequestMapping("loginByAccount")
    @ResponseBody
    public ResponseResult loginByAccount(String loginName, String password, HttpSession session) {

        try {
            password = CommonUtils.MD5(password);
            Customer customer = customerService.login(loginName, password);
            //将密码设置为空，存到session中
            customer.setPassword(null);
            session.setAttribute("customer", customer);
            return ResponseResult.success(customer);
        } catch (Exception e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    /**
     * @param [session]
     * @return {@link ResponseResult}
     * Description <退出登录>
     * @author GOLD
     * @date 2020/2/26 17:04
     */
    @RequestMapping("logout")
    @ResponseBody
    public ResponseResult logout(HttpSession session) {
        //设置session失效
        session.invalidate();
        return ResponseResult.success();
    }

    /**
     * @param [phone, verifyCode, session]
     * @return {@link ResponseResult}
     * Description <通过短信快捷登录>
     * @author GOLD
     * @date 2020/2/26 17:04
     */
    @RequestMapping("loginBySms")
    @ResponseBody
    public ResponseResult loginBySms(String phone, int verifyCode, HttpSession session) {
        try {
            //判断手机号是否注册
            Customer customer = customerService.findByPhone(phone);

            //判断验证码是否存在
            //Object code = session.getAttribute("smsVerifyCode");
            //从redis中获取验证码
            String code = RedisUtil.get(session.getId());
            if (!ObjectUtils.isEmpty(code)) {
                //判断验证码是否正确
                if (Integer.parseInt(code) == verifyCode) {
                    /*将用户信息放到session中*/
                    customer.setPassword(null);
                    session.setAttribute("customer", customer);
                    //返回数据给前端渲染
                    return ResponseResult.success(customer);
                } else {
                    return ResponseResult.fail("验证码不正确");
                }
            } else {
                return ResponseResult.fail("验证码不存在或已过期，请重新输入");
            }
        } catch (PhoneNotRegisterException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    /**
     * @param [customerVo, session]
     * @return {@link ResponseResult}
     * Description <用户注册>
     * @author GOLD
     * @date 2020/2/26 17:04
     */
    @RequestMapping("register")
    @ResponseBody
    public ResponseResult register(CustomerVo customerVo, HttpSession session) {
        try {
            String md5Pwd = CommonUtils.MD5(customerVo.getPassword());
            customerVo.setPassword(md5Pwd);
        } catch (Exception e) {
            return ResponseResult.fail("注册失败");
        }
        Customer customer = customerService.register(customerVo);
        if (customer != null) {
            customer.setPassword(null);
            session.setAttribute("customer", customer);
            return ResponseResult.success("注册成功", customer);
        } else {
            return ResponseResult.fail("注册失败");
        }
    }

    /**
     * @param [password, session]
     * @return {@link Map< String, Object>}
     * Description <校验原始密码是否存在>
     * @author GOLD
     * @date 2020/2/26 17:06
     */
    @RequestMapping("checkPassword")
    @ResponseBody
    public Map<String, Object> checkPassword(String password, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        Customer customer = (Customer) session.getAttribute("customer");
        try {
            password = CommonUtils.MD5(password);
            Customer result = customerService.login(customer.getLoginName(), password);
            if (result != null) {
                map.put("valid", true);
            }
        } catch (Exception e) {
            map.put("valid", false);
            map.put("message", "原始密码不正确");
        }
        return map;
    }

    /**
     * @param [password, newPassword, session]
     * @return {@link ResponseResult}
     * Description <修改密码>
     * @author GOLD
     * @date 2020/2/26 17:06
     */
    @RequestMapping("modifyPassword")
    @ResponseBody
    public ResponseResult modifyPassword(String newPassword, HttpSession session) {

        Customer customer = (Customer) session.getAttribute("customer");
        try {
            newPassword = CommonUtils.MD5(newPassword);
            customer.setPassword(newPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.fail("密码修改失败");
        }

        //设置session失效
        if (customerService.modifyCustomerPassword(customer)) {
            session.invalidate();
            customer.setPassword(null);
            return ResponseResult.success("密码修改成功,请重新登录", customer);
        } else {
            //这里有点啰嗦，看后面有什么好的方式
            //如果失败了，换回原来的密码
            customer.setPassword(null);
            session.setAttribute("customer", customer);
            return ResponseResult.fail("密码修改失败");
        }
    }

    /**
     * @param [loginName]
     * @return {@link Map< String, Object>}
     * Description <校验用户登录名是否存在，登录名是唯一的>
     * @author GOLD
     * @date 2020/2/26 17:07
     */
    @RequestMapping("checkLoginName")
    @ResponseBody
    public Map<String, Object> checkLoginName(String loginName) {
        Map<String, Object> map = new HashMap<>();
        try {
            Boolean isvariable = customerService.findByLoginName(loginName);
            if (isvariable) {
                map.put("valid", true);
            }
        } catch (CustomerLoginNameIsExist e) {
            map.put("valid", false);
            map.put("message", e.getMessage());
        }
        return map;
    }


    //个人中心模块

    /**
     * @param [session, model]
     * @return {@link String}
     * Description <展示用户个人中心>
     * @author GOLD
     * @date 2020/2/26 17:07
     */
    @RequestMapping("customerCenter")
    public String customerCenter(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return "main";
        }
        Customer user = customerService.findCustomerId(customer.getId());
        user.setPassword(null);
        model.addAttribute("user", user);
        return "center";
    }

    /**
     * @param [attributes]
     * @return {@link String}
     * Description <session 超时>
     * @author GOLD
     * @date 2020/2/26 17:07
     */
    @RequestMapping("sessionTimeOut")
    public String sessionTimeOut(RedirectAttributes attributes) {
        attributes.addFlashAttribute("sessionTimeOut", "session超时");
        return "redirect:/reception/product/searchAllProducts";
    }

    /**
     * @param [customerId, mobile, address, session]
     * @return {@link ResponseResult}
     * Description <个人中心修改信息>
     * @author GOLD
     * @date 2020/2/26 17:07
     */
    @RequestMapping("modifyCenterCustomer")
    @ResponseBody
    public ResponseResult modifyCenterCustomer(Integer customerId, String mobile, String address, HttpSession session) {
        Object customer = session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("请先登录");
        }

        if (customerService.modifyCenterCustomer(customerId, mobile, address)) {
            return ResponseResult.success("信息修改成功");
        }
        return ResponseResult.fail("信息修改失败");
    }

}
