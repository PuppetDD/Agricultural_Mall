package com.gold.mall.service;

import com.gold.mall.params.OrderParam;
import com.gold.mall.pojo.Order;
import com.gold.mall.vo.OrderVo;

import java.util.List;
import java.util.Map;

/**
 * @author GOLD
 * @date 2020/2/23 16:29
 * Description <订单Service>
 */
public interface OrderService {

    public List<Order> findAllOrders();

    String saveOrder(OrderVo orderVo);

    Order findOrderByID(Integer id);

    Order findOrderIdByOrderNoAndCustomerId(String orderNo, Integer id);

    Order findOrderByOutTradeNo(String orderNo);

    List<Order> findOrdersByParams(OrderParam orderParam);

    List<OrderVo> getCustomerAllOrders(Integer id);

    Boolean modifyOrderStatusByCustomerIdAndOrderId(Integer id, Integer orderId);

    int removeOrderById(Integer id);

    Boolean removeOrderByCustomerIdAndOrderId(Integer id, Integer removeOrderId);

    Boolean confirmOrderByCustomerIdAndOrderId(Integer id, Integer confirmOrderId);

    List<OrderVo> getDifferenceStatusOrders(Integer id, Integer status);

    Boolean modifyOrder(OrderVo orderVo);

    Boolean modifyOrderStatusByCustomerIdAndOrderNo(Integer id, String out_trade_no);

    Boolean modifyOrderStatusByOrderNo(String out_trade_no);

    String getWxPayUrl(Order order, String ip) throws Exception;

    Map<String, String> getWxPayResultMap(Order order, String ip) throws Exception;

}
