package com.gold.mall.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GOLD
 * @date 2020/2/25 11:56
 * Description <后台查询订单参数>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderParam {

    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 客户账号
     */
    private Integer customerId;
    /**
     * 订单状态
     */
    private Integer status;

}
