package com.gold.mall.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author GOLD
 * @date 2020/2/23 14:09
 * Description <描述>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductParam implements Serializable {

    /**
     * 商品名称
     */
    private String name;
    /**
     * 最低价格
     */
    private Double minPrice;
    /**
     * 最高价格
     */
    private Double maxPrice;
    /**
     * 商品类型的id
     */
    private Integer productTypeId;

    @Override
    public String toString() {
        StringBuilder params = new StringBuilder("id:*");
        if (name != null && name != "") {
            params.append(" AND p_name:" + name);
        }
        if (minPrice != null && maxPrice != null) {
            params.append(" AND p_price:[" + minPrice + " TO " + maxPrice + "]");
        } else if (minPrice != null) {
            params.append(" AND p_price:[" + minPrice + " TO *]");
        } else if (maxPrice != null) {
            params.append(" AND p_price:[* TO " + maxPrice + "]");
        }
        if (productTypeId != null && productTypeId != -1) {
            params.append(" AND pt_id:" + productTypeId);
        }
        return params.toString();
    }

}
