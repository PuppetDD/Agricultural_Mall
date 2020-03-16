package com.gold.mall.dao;

import com.gold.mall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 13:18
 * Description <订单类目Dao>
 */
public interface OrderItemDao {

    List<OrderItem> selectOrderItemsByOrder(Integer orderId);

    List<OrderItem> selectOrderItemsByOrderIds();

    int insertOrderItem(OrderItem orderItem);

    int insertOrderItemByOrderItems(@Param("orderItemList") List<OrderItem> orderItemList);

}
