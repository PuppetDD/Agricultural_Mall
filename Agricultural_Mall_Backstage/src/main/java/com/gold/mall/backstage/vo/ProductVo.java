package com.gold.mall.backstage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author GOLD
 * @date 2020/2/24 14:35
 * Description <商品Vo>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVo {

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
     * 上传的图片
     */
    private CommonsMultipartFile file;
    /**
     * 商品类型的id
     */
    private Integer productTypeId;
    /**
     * 商品的描述
     */
    private String info;

}
