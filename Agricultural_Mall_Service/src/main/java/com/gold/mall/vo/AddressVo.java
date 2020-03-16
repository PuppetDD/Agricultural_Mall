package com.gold.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GOLD
 * @date 2020/2/23 16:19
 * Description <收货地址Vo>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressVo {

    /**
     * id
     */
    private Integer id;
    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 收货人的座机号码
     */
    private String receiverPhone;
    /**
     * 收货人的手机号码
     */
    private String receiverMobile;
    /**
     * 省份名称
     */
    private String receiverProvince;
    /**
     * 城市名称
     */
    private String receiverCity;
    /**
     * 区 县
     */
    private String receiverDistrict;
    /**
     * 邮政编码
     */
    private String zipCode;
    /**
     * 详细地址内容
     */
    private String addressDetail;

}
