package com.gold.mall.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gold.mall.common.constant.PaginationConstant;
import com.gold.mall.common.exception.OrderCartNotFoundException;
import com.gold.mall.common.utils.ResponseResult;
import com.gold.mall.params.OrderParam;
import com.gold.mall.pojo.Order;
import com.gold.mall.service.OrderService;
import com.gold.mall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/24 15:05
 * Description <描述>
 */
@Controller
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * @param [pageNum, model]
     * @return {@link String}
     * Description <后台显示所有订单>
     * @author GOLD
     * @date 2020/2/25 0:42
     */
    @RequestMapping("getAllOrders")
    public String getAllOrders(Integer pageNum, Model model) {
        if (ObjectUtils.isEmpty(pageNum)) {
            pageNum = PaginationConstant.PAGE_NUM;
        }
        PageHelper.startPage(pageNum, PaginationConstant.PAGE_SIZE);
        List<Order> orderList = orderService.findAllOrders();

        PageInfo<Order> pageInfo = new PageInfo<>(orderList);
        model.addAttribute("pageInfo", pageInfo);
        return "orderManager";
    }

    /**
     * @param [orderParam, pageNum, model]
     * @return {@link String}
     * Description <通过多条件查询获取获取用户信息>
     * @author GOLD
     * @date 2020/2/24 15:10
     */
    @RequestMapping("getAllOrdersByParams")
    public String getAllOrdersByParams(OrderParam orderParam, Integer pageNum, Model model) {
        if (ObjectUtils.isEmpty(pageNum)) {
            pageNum = PaginationConstant.PAGE_NUM;
        }

        PageHelper.startPage(pageNum, PaginationConstant.PAGE_SIZE);
        List<Order> orderList = orderService.findOrdersByParams(orderParam);
        PageInfo<Order> pageInfo = new PageInfo<>(orderList);

        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("params", orderParam);
        return "orderManager";
    }

    /**
     * @param [id]
     * @return {@link ResponseResult}
     * Description <展示一个订单的信息>
     * @author GOLD
     * @date 2020/2/24 15:10
     */
    @RequestMapping("findOrderById")
    @ResponseBody
    public ResponseResult findOrderById(int id) {
        try {
            Order order = orderService.findOrderByID(id);
            System.out.println(order.getCustomer() + "  " + order.getCreateDate());
            return ResponseResult.success("获取订单信息成功", order);
        } catch (OrderCartNotFoundException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    /**
     * @param [productVo, pageNum, session, model]
     * @return {@link String}
     * Description <修改订单信息>
     * @author GOLD
     * @date 2020/2/24 15:14
     */
    @RequestMapping("modifyOrder")
    public String modifyOrder(OrderVo orderVo, Integer pageNum, Model model) {
        if (ObjectUtils.isEmpty(pageNum)) {
            pageNum = PaginationConstant.PAGE_NUM;
        }
        if (orderService.modifyOrder(orderVo)) {
            model.addAttribute("successMsg", "修改成功");
        } else {
            model.addAttribute("failMsg", "修改失败");
        }
        return "forward:getAllOrders?pageNum=" + pageNum;
    }

    /**
     * @param [id]
     * @return {@link ResponseResult}
     * Description <根据 id 删除订单>
     * @author GOLD
     * @date 2020/2/24 15:14
     */
    @RequestMapping("removeOrderById")
    @ResponseBody
    public ResponseResult removeOrderById(int id) {
        int rows = orderService.removeOrderById(id);
        System.out.println(id);
        if (rows >= 1) {
            return ResponseResult.success("订单删除成功");
        } else {
            return ResponseResult.fail("订单删除失败");
        }
    }

}
