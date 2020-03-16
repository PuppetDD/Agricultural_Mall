package com.gold.mall.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gold.mall.common.constant.PaginationConstant;
import com.gold.mall.common.exception.CustomerNotFoundException;
import com.gold.mall.common.utils.ResponseResult;
import com.gold.mall.params.CustomerParam;
import com.gold.mall.pojo.Customer;
import com.gold.mall.service.CustomerService;
import com.gold.mall.vo.CustomerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/24 15:02
 * Description <客户管理接口>
 */
@Controller
@RequestMapping("/admin/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * @param [pageNum, model]
     * @return {@link String}
     * Description <获取所有的用户信息>
     * @author GOLD
     * @date 2020/2/24 15:09
     */
    @RequestMapping("getAllCustomers")
    public String getAllCustomers(Integer pageNum, Model model) {
        if (ObjectUtils.isEmpty(pageNum)) {
            pageNum = PaginationConstant.PAGE_NUM;
        }
        PageHelper.startPage(pageNum, PaginationConstant.PAGE_SIZE);
        List<Customer> customerList = customerService.findAllCustomers();

        PageInfo<Customer> pageInfo = new PageInfo<>(customerList);
        model.addAttribute("pageInfo", pageInfo);
        return "customerManager";
    }

    /**
     * @param [customerParam, pageNum, model]
     * @return {@link String}
     * Description <通过多条件查询获取获取用户信息>
     * @author GOLD
     * @date 2020/2/24 15:10
     */
    @RequestMapping("getAllCustomersByParams")
    public String getAllCustomersByParams(CustomerParam customerParam, Integer pageNum, Model model) {
        if (ObjectUtils.isEmpty(pageNum)) {
            pageNum = PaginationConstant.PAGE_NUM;
        }
        PageHelper.startPage(pageNum, PaginationConstant.PAGE_SIZE);
        List<Customer> customerList = customerService.findCustomersByParams(customerParam);
        PageInfo<Customer> pageInfo = new PageInfo<>(customerList);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("params", customerParam);

        return "customerManager";
    }

    /**
     * @param [id]
     * @return {@link ResponseResult}
     * Description <展示一个用户的信息>
     * @author GOLD
     * @date 2020/2/24 15:10
     */
    @RequestMapping("showCustomer")
    @ResponseBody
    public ResponseResult showCustomer(int id) {
        try {
            Customer customer = customerService.findCustomerId(id);
            return ResponseResult.success("获取用户信息成功", customer);
        } catch (CustomerNotFoundException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    /**
     * @param [customerVo, pageNum, model]
     * @return {@link String}
     * Description <修改客户信息>
     * @author GOLD
     * @date 2020/2/24 15:10
     */
    @RequestMapping("modifyCustomer")
    public String modifyCustomer(CustomerVo customerVo, Integer pageNum, Model model) {
        if (ObjectUtils.isEmpty(pageNum)) {
            pageNum = PaginationConstant.PAGE_NUM;
        }
        if (customerService.modifyCustomer(customerVo)) {
            model.addAttribute("successMsg", "修改成功");
        } else {
            model.addAttribute("failMsg", "修改失败");
        }
        return "forward:getAllCustomers?pageNum=" + pageNum;
    }

    /**
     * @param [id]
     * @return {@link ResponseResult}
     * Description <禁用启用客户账户>
     * @author GOLD
     * @date 2020/2/24 15:10
     */
    @RequestMapping("modifyCustomerStatus")
    @ResponseBody
    public ResponseResult modifyCustomerStatus(int id) {
        if (customerService.modifyCustomerStatus(id)) {
            return ResponseResult.success("状态修改成功");
        } else {
            return ResponseResult.success("状态修改失败");
        }
    }

}
