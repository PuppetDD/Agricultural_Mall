package com.gold.mall.dao;

import com.gold.mall.params.ProductParam;
import com.gold.mall.pojo.Product;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 13:17
 * Description <商品Dao>
 */
public interface ProductDao {

    Product selectByProductName(String name);

    List<Product> selectAllProducts();

    Product selectProductById(int id);

    List<Product> selectByProductParams(ProductParam productParam);

    int insertProduct(Product product);

    int updateProduct(Product product);

    int deleteProductById(int id);

}
