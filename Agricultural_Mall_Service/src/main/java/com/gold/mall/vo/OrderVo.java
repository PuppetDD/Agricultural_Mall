package com.gold.mall.vo;

import com.gold.mall.pojo.Order;
import com.gold.mall.pojo.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 16:19
 * Description <订单Vo>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo extends Order {

    /**
     * 订单中包含的类目
     */
    private List<OrderItem> orderItemList;

}
