package com.gold.mall.dao;

import com.gold.mall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 13:12
 * Description <购物车Dao>
 */
public interface CartDao {

    List<Cart> selectAllCartByCustomerId(Integer customerId);

    Cart selectCartByCustomerIdAndCartId(@Param("customerId") Integer customerId,
                                         @Param("cartId") Integer cartId);

    Cart selectCartByCustomerIdAndProductId(@Param("customerId") Integer customerId,
                                            @Param("productId") Integer productId);

    Cart selectRedirectCartByCustomerIdAndProductId(@Param("customerId") Integer customerId,
                                                    @Param("productId") Integer productId);

    List<Cart> selectCartByCartIdsAndCustomerId(@Param("cartIds") Integer[] orderCartIds,
                                                @Param("customerId") Integer id,
                                                @Param("status") int status);

    List<Cart> selectRedirectCartByCartIdsAndCustomerId(@Param("cartIds") Integer[] orderCartIds,
                                                        @Param("customerId") Integer id,
                                                        @Param("status") int status);

    int insertCart(Cart cart);

    int updateCartNumAndTotalPriceById(@Param("id") Integer id,
                                       @Param("productNum") Integer num,
                                       @Param("totalPrice") Double price);

    int updateCartStatusByCustomerId(@Param("customerId") Integer id,
                                     @Param("status") Integer status);

    int updateCartStatusByCartIdAndCustomerId(@Param("cartId") Integer cartId,
                                              @Param("customerId") Integer id,
                                              @Param("status") Integer status);

    int updateCartStatusByCartIdAndCustomerIds(@Param("cartIds") Integer[] cartIds,
                                               @Param("customerId") Integer customerId,
                                               @Param("status") Integer status);

    int updateProductNumAndPriceByCartIdAndCustomerIdAndStatus(@Param("cartId") Integer cartId,
                                                               @Param("productNum") Integer productNum,
                                                               @Param("customerId") Integer id,
                                                               @Param("status") int status,
                                                               @Param("totalPrice") Double totalPrice);

    int deleteCartById(Integer id);

}
