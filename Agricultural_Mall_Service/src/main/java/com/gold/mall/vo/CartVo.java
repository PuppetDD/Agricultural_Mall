package com.gold.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GOLD
 * @date 2020/2/23 16:17
 * Description <购物车Vo>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartVo {

    /**
     * 购物车主键id
     */
    private Integer id;
    /**
     * 客户id
     */
    private Integer customerId;
    /**
     * 商品的id
     */
    private Integer productId;
    /**
     * 商品数量
     */
    private Integer productNum;
    /**
     * 商品总价
     */
    private Double totalPrice;

}
