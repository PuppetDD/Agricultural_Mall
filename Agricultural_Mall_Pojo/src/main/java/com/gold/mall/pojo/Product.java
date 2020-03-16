package com.gold.mall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GOLD
 * @date 2020/2/23 12:19
 * Description <商品>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    /**
     * 商品id
     */
    private Integer id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品价格
     */
    private Double price;
    /**
     * 商品简介
     */
    private String info;
    /**
     * 商品的图片
     */
    private String image;
    /**
     * 商品类型
     */
    private ProductType productType;

}
