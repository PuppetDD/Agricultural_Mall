package com.gold.mall.service;

import com.gold.mall.pojo.Cart;
import com.gold.mall.vo.CartVo;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 16:26
 * Description <购物车Service>
 */
public interface CartService {

    Boolean saveToCart(CartVo cartVo);

    List<Cart> findCustomerAllCarts(Integer customerId);

    Boolean modifyCartStatus(Integer id);

    Boolean modifyCartStatusByCartIdAndCustomerId(Integer cartId, Integer id);

    Boolean modifyCartStatusByCartIdAndCustomerIds(Integer[] cartIds, Integer customerId);

    Boolean modifyNumAndPriceByCartIdAndCustomerIdAndStatus(Integer cartId, Integer productNum, Integer id);

    List<Cart> findCartByCartIdsAndCustomerId(Integer[] orderCartIds, Integer id);

    List<Cart> findRedirectCartByCartIdsAndCustomerId(Integer[] orderCartIds, Integer id);

    int redirectToCart(CartVo cartVo);

}
