package com.gold.mall.dto;

import com.gold.mall.pojo.Product;
import com.gold.mall.pojo.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.solr.client.solrj.beans.Field;

/**
 * @author GOLD
 * @date 2020/3/7 16:12
 * Description <Solr实体类封装>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSolrDto {

    /**
     * 商品id
     */
    @Field("id")
    private String id;
    /**
     * 商品名称
     */
    @Field("p_name")
    private String name;
    /**
     * 商品价格
     */
    @Field("p_price")
    private Double price;
    /**
     * 商品价格
     */
    @Field("p_stock")
    private Integer stock;
    /**
     * 商品类型的id
     */
    @Field("pt_id")
    private Integer productTypeId;
    /**
     * 图片的位置
     */
    @Field("p_image")
    private String image;
    /**
     * 商品信息的描述
     */
    @Field("p_info")
    private String info;

    public void copyProperties(Product product) {
        id = String.valueOf(product.getId());
        name = product.getName();
        price = product.getPrice();
        stock = product.getStock();
        info = product.getInfo();
        image = product.getImage();
        productTypeId = product.getProductType().getId();
    }

    public void giveProperties(Product product) {
        product.setId(Integer.valueOf(this.id));
        product.setName(this.name);
        product.setPrice(this.price);
        product.setStock(this.stock);
        product.setInfo(this.info);
        product.setImage(this.image);
        ProductType productType = new ProductType();
        productType.setId(this.productTypeId);
        product.setProductType(productType);
    }

}
