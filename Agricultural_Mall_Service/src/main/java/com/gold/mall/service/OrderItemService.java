package com.gold.mall.service;

import com.gold.mall.pojo.OrderItem;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 16:29
 * Description <订单类目Service>
 */
public interface OrderItemService {

    Boolean saveOrderItem(OrderItem orderItem);

    List<OrderItem> findOrderItemsByOrderId(Integer id);

}
