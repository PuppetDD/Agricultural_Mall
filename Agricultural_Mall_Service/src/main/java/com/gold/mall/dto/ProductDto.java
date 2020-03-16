package com.gold.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

/**
 * @author GOLD
 * @date 2020/2/23 16:38
 * Description <商品数据传输实体类封装>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

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
     * 商品类型的id
     */
    private Integer productTypeId;
    /**
     * 文件输入流
     */
    private InputStream inputStream;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件上传的位置
     */
    private String uploadPath;
    /**
     * 商品信息的描述
     */
    private String info;

}
