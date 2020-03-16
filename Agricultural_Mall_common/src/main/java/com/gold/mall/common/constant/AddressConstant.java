package com.gold.mall.common.constant;

/**
 * @author GOLD
 * @date 2020/2/24 11:37
 * Description <收货地址常量>
 */
public interface AddressConstant {

    /**
     * 0 表示被删除的地址
     */
    int SHIPPING_ISVALID_STATUS = 0;
    /**
     * 新建一个地址默认为 1
     */
    int SHIPPING_COMMON_STATUS = 1;
    /**
     * 2 表示默认的地址
     */
    int SHIPPING_DEFAULT_STATUS = 2;

}
