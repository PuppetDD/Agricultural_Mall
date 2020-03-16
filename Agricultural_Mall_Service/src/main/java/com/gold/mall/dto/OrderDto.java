package com.gold.mall.dto;

import com.gold.mall.pojo.Order;
import com.gold.mall.pojo.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 16:38
 * Description <订单类封装>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private List<Order> orderList;
    private List<OrderItem> orderItemList;

}
