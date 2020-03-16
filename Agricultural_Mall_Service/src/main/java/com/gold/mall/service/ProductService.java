package com.gold.mall.service;

import com.gold.mall.dto.ProductDto;
import com.gold.mall.params.ProductParam;
import com.gold.mall.pojo.Product;
import org.apache.commons.fileupload.FileUploadException;

import java.io.OutputStream;
import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 16:27
 * Description <商品Service>
 */
public interface ProductService {

    int addProduct(ProductDto productDto) throws FileUploadException;

    Boolean checkProductName(String name);

    List<Product> findAllProducts();

    Product findProductById(int id);

    int modifyProduct(ProductDto productDto) throws FileUploadException;

    int removeProductById(int id);

    void getImage(String path, OutputStream outputStream);

    List<Product> findByProductParams(ProductParam productParam);

}
