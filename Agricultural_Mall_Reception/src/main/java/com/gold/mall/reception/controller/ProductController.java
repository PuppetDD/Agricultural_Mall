package com.gold.mall.reception.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gold.mall.common.constant.PaginationConstant;
import com.gold.mall.params.ProductParam;
import com.gold.mall.pojo.Product;
import com.gold.mall.pojo.ProductType;
import com.gold.mall.service.ProductService;
import com.gold.mall.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/26 14:13
 * Description <商品接口>
 */
@Controller
@RequestMapping("/reception/product")
public class ProductController {

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private ProductService productService;

    /**
     * @param [productParam, pageNum, model]
     * @return {@link String}
     * Description <加载所有商品列表>
     * @author GOLD
     * @date 2020/2/26 17:13
     */
    @RequestMapping("/searchAllProducts")
    public String searchAllProducts(ProductParam productParam, Integer pageNum, Model model) {
        if (ObjectUtils.isEmpty(pageNum)) {
            pageNum = PaginationConstant.PAGE_NUM;
        }
        PageHelper.startPage(pageNum, PaginationConstant.FRONT_PAGE_SIZE);
        List<Product> productList = productService.findByProductParams(productParam);
        PageInfo<Product> pageInfo = new PageInfo<>(productList);
        model.addAttribute("pageInfo", pageInfo);
        return "main";
    }

    /**
     * @param []
     * @return {@link List< ProductType>}
     * Description <页面初始化>
     * @author GOLD
     * @date 2020/2/26 17:13
     */
    @ModelAttribute("productTypes")
    public List<ProductType> loadProductTypes() {
        List<ProductType> productTypes = productTypeService.findAllEnableProductTypes();
        return productTypes;
    }

    /**
     * @param [model, id]
     * @return {@link String}
     * Description <展示商品详情>
     * @author GOLD
     * @date 2020/2/26 17:13
     */
    @RequestMapping("showProductDetail")
    public String showProductDetail(Model model, Integer id) {

        Product product = productService.findProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
        }
        return "productDetail";
    }

}
