package com.gold.mall.service;

import com.gold.mall.common.exception.ProductTypeExistException;
import com.gold.mall.pojo.ProductType;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 16:27
 * Description <商品类型Service>
 */
public interface ProductTypeService {

    List<ProductType> findAll();

    List<ProductType> findAllValidStatus();

    void addProductType(String name) throws ProductTypeExistException;

    ProductType findProductTypeById(Integer id);

    int modifyProductTypeName(int id, String name);

    int modifyProductTypeStatus(int id);

    int removeProductTypeById(int id);

    List<ProductType> findAllEnableProductTypes();

}
