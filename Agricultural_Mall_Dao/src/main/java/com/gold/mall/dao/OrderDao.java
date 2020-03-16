package com.gold.mall.dao;

import com.gold.mall.params.OrderParam;
import com.gold.mall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 13:17
 * Description <订单Dao>
 */
public interface OrderDao {

    List<Order> selectAllOrders();

    Order selectOrderById(int id);

    List<Order> selectAllOrderByCustomerId(@Param("customerId") Integer id);

    List<Order> selectOrdersByCustomerId(@Param("customerId") Integer id,
                                         @Param("status") Integer status);

    Order selectOrderIdByOrderNoAndCustomerId(@Param("orderNo") String orderNo,
                                              @Param("customerId") Integer id);

    List<Order> selectOrdersByParams(OrderParam orderParam);

    Order selectOrderByOutTradeNo(String outTradeNo);

    int insertOrder(Order order);

    int updateOrder(Order order);

    int updateOrderStatusByCustomerIdAndOrderId(@Param("customerId") Integer id,
                                                @Param("orderId") Integer orderId,
                                                @Param("status") Integer status);

    int updateOrderStatusByCustomerIdAndOrderNo(@Param("customerId") Integer id,
                                                @Param("orderNumber") String out_trade_no,
                                                @Param("status") Integer status);

    int updateOrderStatusByOrderNo(@Param("orderNumber") String out_trade_no,
                                   @Param("status") Integer status);

    int deleteOrderById(Integer id);

}
