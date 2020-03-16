package com.gold.mall.service.impl;

import com.gold.mall.common.constant.ProductTypeConstant;
import com.gold.mall.common.exception.ProductTypeExistException;
import com.gold.mall.dao.ProductTypeDao;
import com.gold.mall.pojo.ProductType;
import com.gold.mall.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 18:05
 * Description <商品类型impl>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeDao productTypeDao;

    /**
     * @param []
     * @return {@link List< ProductType>}
     * Description <显示所有商品类型>
     * @author GOLD
     * @date 2020/2/24 13:34
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<ProductType> findAll() {
        return productTypeDao.selectAll();
    }

    /**
     * @param []
     * @return {@link List< ProductType>}
     * Description <显示所有有效类型>
     * @author GOLD
     * @date 2020/2/24 13:35
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<ProductType> findAllValidStatus() {
        return productTypeDao.selectAllValidByStatus(ProductTypeConstant.PRODUCT_TYPE_ENABLE);
    }

    /**
     * @param [name] Description <新建类型>
     * @author GOLD
     * @date 2020/2/24 13:35
     */
    @Override
    public void addProductType(String name) throws ProductTypeExistException {
        ProductType productType = productTypeDao.selectProductTypeByName(name);
        if (productType != null) {
            throw new ProductTypeExistException("商品类型已存在");
        }
        productTypeDao.insertProductType(name, ProductTypeConstant.PRODUCT_TYPE_ENABLE);
    }

    /**
     * @param [id]
     * @return {@link ProductType}
     * Description <根据id查找类型>
     * @author GOLD
     * @date 2020/2/24 13:35
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ProductType findProductTypeById(Integer id) {
        return productTypeDao.selectProductTypeById(id);
    }

    /**
     * @param [id, name]
     * @return {@link int}
     * Description <修改类型名>
     * @author GOLD
     * @date 2020/2/24 13:35
     */
    @Override
    public int modifyProductTypeName(int id, String name) throws ProductTypeExistException {
        //判断该商品类型的名称是否存在
        ProductType productType = productTypeDao.selectProductTypeByName(name);
        if (productType != null) {
            throw new ProductTypeExistException("商品类型名称已存在");
        }
        return productTypeDao.updateName(id, name);
    }

    /**
     * @param [id]
     * @return {@link int}
     * Description <修改类型状态>
     * @author GOLD
     * @date 2020/2/24 13:35
     */
    @Override
    public int modifyProductTypeStatus(int id) {
        ProductType productType = productTypeDao.selectProductTypeById(id);
        int status = productType.getStatus();
        if (status == ProductTypeConstant.PRODUCT_TYPE_ENABLE) {
            status = ProductTypeConstant.PRODUCT_TYPE_DISENABLE;
        } else {
            status = ProductTypeConstant.PRODUCT_TYPE_ENABLE;
        }
        return productTypeDao.updateStatus(id, status);
    }

    /**
     * @param [id]
     * @return {@link int}
     * Description <根据id删除类型>
     * @author GOLD
     * @date 2020/2/24 13:35
     */
    @Override
    public int removeProductTypeById(int id) {
        return productTypeDao.deleteProductTypeById(id);
    }

    /**
     * @param []
     * @return {@link List< ProductType>}
     * Description <查找所有无效类型>
     * @author GOLD
     * @date 2020/2/24 13:35
     */
    @Override
    public List<ProductType> findAllEnableProductTypes() {
        return productTypeDao.findAllEnableProductTypes(ProductTypeConstant.PRODUCT_TYPE_ENABLE);
    }

}
