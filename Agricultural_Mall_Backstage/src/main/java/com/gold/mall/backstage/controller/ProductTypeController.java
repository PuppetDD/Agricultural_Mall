package com.gold.mall.backstage.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gold.mall.common.constant.PaginationConstant;
import com.gold.mall.common.exception.ProductTypeExistException;
import com.gold.mall.common.utils.ResponseResult;
import com.gold.mall.pojo.ProductType;
import com.gold.mall.service.ProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/24 15:03
 * Description <商品类型管理接口>
 */
@Slf4j
@Controller
@RequestMapping("/admin/product_type")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    /**
     * @param [pageNum, model]
     * @return {@link String}
     * Description <查询所有商品类型类表>
     * @author GOLD
     * @date 2020/2/24 15:16
     */
    @GetMapping("find_all")
    public String findAllType(Integer pageNum, Model model) {

        if (ObjectUtils.isEmpty(pageNum)) {
            //设置默认值
            pageNum = PaginationConstant.PAGE_NUM;
        }

        //设置分页
        PageHelper.startPage(pageNum, PaginationConstant.PAGE_SIZE);
        //查询所有的数据
        List<ProductType> productTypeList = productTypeService.findAll();
        /*将查询的结果进行封装，封装到pageHelper中*/
        PageInfo<ProductType> pageInfo = new PageInfo<>(productTypeList);

        log.info("pageInfo= {}", pageInfo);

        model.addAttribute("pageInfo", pageInfo);
        return "productTypeManager";
    }

    /**
     * @param [name]
     * @return {@link ResponseResult}
     * Description <添加商品类型>
     * @author GOLD
     * @date 2020/2/24 15:16
     */
    @PostMapping("add")
    @ResponseBody
    public ResponseResult addType(String name) {
        try {
            productTypeService.addProductType(name);
            return ResponseResult.success("添加成功");
        } catch (ProductTypeExistException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    /**
     * @param [id]
     * @return {@link ResponseResult}
     * Description <获取某一商品类型的信息>
     * @author GOLD
     * @date 2020/2/24 15:17
     */
    @GetMapping("findProductTypeById")
    @ResponseBody
    public ResponseResult findProductTypeById(int id) {
        ProductType productType = productTypeService.findProductTypeById(id);
        return ResponseResult.success(productType);
    }

    /**
     * @param [id, name]
     * @return {@link ResponseResult}
     * Description <修改商品类型名称>
     * @author GOLD
     * @date 2020/2/24 15:17
     */
    @RequestMapping("modifyProductTypeName")
    @ResponseBody
    public ResponseResult modifyProductTypeName(int id, String name) {

        try {
            int rows = productTypeService.modifyProductTypeName(id, name);
            if (rows >= 1) {
                return ResponseResult.success("修改商品类型成功");
            } else {
                return ResponseResult.fail("修改商品类型失败");
            }
        } catch (ProductTypeExistException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    /**
     * @param [id]
     * @return {@link ResponseResult}
     * Description <删除某一商品类型>
     * @author GOLD
     * @date 2020/2/24 15:17
     */
    @RequestMapping("removeProductType")
    @ResponseBody
    public ResponseResult removeProductType(int id) {
        int rows = productTypeService.removeProductTypeById(id);
        if (rows >= 1) {
            return ResponseResult.success("删除成功");
        } else {
            return ResponseResult.fail("删除失败");
        }
    }

    /**
     * @param [id]
     * @return {@link ResponseResult}
     * Description <修改商品类型状态，是否启用该类型商品>
     * @author GOLD
     * @date 2020/2/24 15:17
     */
    @RequestMapping("modifyProductTypeStatus")
    @ResponseBody
    public ResponseResult modifyProductTypeStatus(int id) {
        int rows = productTypeService.modifyProductTypeStatus(id);
        if (rows >= 1) {
            return ResponseResult.success("状态修改成功");
        } else {
            return ResponseResult.fail("状态修改失败");
        }
    }

}
