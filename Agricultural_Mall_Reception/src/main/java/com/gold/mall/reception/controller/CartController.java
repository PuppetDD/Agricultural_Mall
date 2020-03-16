package com.gold.mall.reception.controller;

import com.gold.mall.common.utils.ResponseResult;
import com.gold.mall.pojo.Cart;
import com.gold.mall.pojo.Customer;
import com.gold.mall.service.CartService;
import com.gold.mall.vo.CartVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/26 14:12
 * Description <购物车接口>
 */
@Slf4j
@Controller
@RequestMapping("/reception/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * @param []
     * @return {@link String}
     * Description <清空购物车后展示此页面>
     * @author GOLD
     * @date 2020/2/26 17:01
     */
    @RequestMapping("showEmptyCart")
    public String showEmptyCart() {
        return "emptyCart";
    }

    /**
     * @param [session, model]
     * @return {@link String}
     * Description <购物车展示>
     * @author GOLD
     * @date 2020/2/26 17:01
     */
    @RequestMapping("myCarts")
    public String myCars(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer != null) {
            List<Cart> cartList = cartService.findCustomerAllCarts(customer.getId());
            model.addAttribute("cartList", cartList);
        }
        return "cart";
    }

    /**
     * @param [id, productNum, session]
     * @return {@link ResponseResult}
     * Description <添加商品到购物车>
     * @author GOLD
     * @date 2020/2/26 17:01
     */
    @RequestMapping("addToCart")
    @ResponseBody
    public ResponseResult addToCart(Integer id, Integer productNum, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        System.out.println("coming");
        if (ObjectUtils.isEmpty(customer)) {
            //用户没有登录，则提示让他登录
            return ResponseResult.deny("还请客官先登录哦~");
        } else {

            CartVo cartVo = new CartVo();
            cartVo.setCustomerId(customer.getId());
            cartVo.setProductId(id);
            cartVo.setProductNum(productNum);

            if (cartService.saveToCart(cartVo)) {
                //此session用于标志购物车非空
                //session.setAttribute("emptyCart",0);
                return ResponseResult.success("商品成功加入购物车");
            } else {
                return ResponseResult.fail("商品加入购物车失败");
            }
        }
    }

    /**
     * @param [session]
     * @return {@link ResponseResult}
     * Description <清空购物车操作>
     * @author GOLD
     * @date 2020/2/26 17:02
     */
    @RequestMapping("clearAllProductFromCart")
    @ResponseBody
    public ResponseResult clearAllProductFromCart(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (!ObjectUtils.isEmpty(customer)) {
            if (cartService.modifyCartStatus(customer.getId())) {
                //此session用于标志购物车为空
                //session.setAttribute("emptyCart",null);
                return ResponseResult.success("购物车已清空");
            }
        } else {
            return ResponseResult.fail("请您先登录");
        }
        return ResponseResult.fail("商品移除失败");
    }

    /**
     * @param [cartId, session]
     * @return {@link ResponseResult}
     * Description <从购物车中移除某一商品>
     * @author GOLD
     * @date 2020/2/26 17:02
     */
    @RequestMapping("removeOneProduct")
    @ResponseBody
    public ResponseResult removeOneProduct(Integer cartId, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("请您先登录");
        }
        if (cartService.modifyCartStatusByCartIdAndCustomerId(cartId, customer.getId())) {
            return ResponseResult.success("该商品移除成功");
        }
        return ResponseResult.fail();
    }

    /**
     * @param [cartIds, session]
     * @return {@link ResponseResult}
     * Description <从购物车中移除选中的商品>
     * @author GOLD
     * @date 2020/2/26 17:02
     */
    @RequestMapping("removeMoreProductFromCart")
    @ResponseBody
    public ResponseResult removeMoreProductFromCart(Integer[] cartIds, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("请您先登录");
        }
        if (cartService.modifyCartStatusByCartIdAndCustomerIds(cartIds, customer.getId())) {
            return ResponseResult.success("商品移除成功");
        }
        return ResponseResult.fail("商品移除失败");
    }

    /**
     * @param [cartId, productNum, session]
     * @return {@link ResponseResult}
     * Description <购物车页面修改商品的数量>
     * @author GOLD
     * @date 2020/2/26 17:02
     */
    @RequestMapping("inputModifyProductNum")
    @ResponseBody
    public ResponseResult inputModifyProductNum(Integer cartId, Integer productNum, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("客官，还请先登录");
        }
        if (cartService.modifyNumAndPriceByCartIdAndCustomerIdAndStatus(cartId, productNum, customer.getId())) {
            return ResponseResult.success("商品数量已修改");
        }
        return ResponseResult.fail("商品数量修改失败");
    }

    /**
     * @param [count, price, orderCartIds, session]
     * @return {@link ResponseResult}
     * Description <临时将前端发送过来的数据存到session中去>
     * @author GOLD
     * @date 2020/2/26 17:03
     */
    @RequestMapping("addTempOrderItem")
    @ResponseBody
    public ResponseResult addTempOrderItem(Integer count, String price, Integer[] orderCartIds, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("客官还请先登录");
        }
        session.setAttribute("count", count);

        String[] strings = price.split("¥");

        double newPrice = Double.parseDouble(strings[1]);

        session.setAttribute("price", newPrice);
        session.setAttribute("orderCartIds", orderCartIds);
        return ResponseResult.success(session);
    }

}
