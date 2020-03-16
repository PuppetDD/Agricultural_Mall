package com.gold.mall.service.impl;

import com.gold.mall.common.constant.CartConstant;
import com.gold.mall.common.exception.OrderCartNotFoundException;
import com.gold.mall.dao.CartDao;
import com.gold.mall.dao.ProductDao;
import com.gold.mall.pojo.Cart;
import com.gold.mall.pojo.Product;
import com.gold.mall.service.CartService;
import com.gold.mall.vo.CartVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 18:01
 * Description <购物车impl>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDao cartDao;
    @Autowired
    private ProductDao productDao;

    /**
     * @param [cartVo]
     * @return {@link Boolean}
     * Description <加入购物车>
     * @author GOLD
     * @date 2020/2/24 13:13
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Boolean saveToCart(CartVo cartVo) {
        Cart cartResult = cartDao.selectCartByCustomerIdAndProductId(cartVo.getCustomerId(), cartVo.getProductId());
        //先查询一下购物车中是否有此商品，没有则插入保存，有的话就更新购物车的商品数量就可以了
        if (cartResult == null) {
            Cart cart = new Cart();
            Product product = productDao.selectProductById(cartVo.getProductId());
            //计算总价
            Double totalPrice = product.getPrice() * cartVo.getProductNum();
            DecimalFormat df = new DecimalFormat("#0.00");
            totalPrice = Double.parseDouble(df.format(totalPrice));

            BeanUtils.copyProperties(cartVo, cart);
            cart.setTotalPrice(totalPrice);
            cart.setProduct(product);
            //设置状态，默认是有效的
            cart.setStatus(CartConstant.CART_PRODUCT_STATUS_VALID);
            cart.setCreateTime(new Date());

            int rows = cartDao.insertCart(cart);
            if (rows >= 1) {
                return true;
            } else {
                return false;
            }
        }

        //更新购物车的商品数量
        int productSums = cartResult.getProductNum() + cartVo.getProductNum();
        Product pd = productDao.selectProductById(cartVo.getProductId());

        //更新商品总价格
        Double priceSum = pd.getPrice() * productSums;
        DecimalFormat df = new DecimalFormat("#0.00");
        priceSum = Double.parseDouble(df.format(priceSum));

        int rows = cartDao.updateCartNumAndTotalPriceById(cartResult.getId(), productSums, priceSum);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [customerId]
     * @return {@link List< Cart>}
     * Description <显示购物车商品>
     * @author GOLD
     * @date 2020/2/24 13:14
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<Cart> findCustomerAllCarts(Integer customerId) {
        return cartDao.selectAllCartByCustomerId(customerId);
    }

    /**
     * @param [id]
     * @return {@link Boolean}
     * Description <禁用商品>
     * @author GOLD
     * @date 2020/2/24 13:14
     */
    @Override
    public Boolean modifyCartStatus(Integer id) {
        int rows = cartDao.updateCartStatusByCustomerId(id, CartConstant.CART_PRODUCT_STATUS_ISVALID);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [cartId, id]
     * @return {@link Boolean}
     * Description <禁用商品>
     * @author GOLD
     * @date 2020/2/24 13:14
     */
    @Override
    public Boolean modifyCartStatusByCartIdAndCustomerId(Integer cartId, Integer id) {
        int rows = cartDao.updateCartStatusByCartIdAndCustomerId(cartId, id, CartConstant.CART_PRODUCT_STATUS_ISVALID);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [cartIds, customerId]
     * @return {@link Boolean}
     * Description <禁用商品>
     * @author GOLD
     * @date 2020/2/24 13:14
     */
    @Override
    public Boolean modifyCartStatusByCartIdAndCustomerIds(Integer[] cartIds, Integer customerId) {
        int rows = cartDao.updateCartStatusByCartIdAndCustomerIds(cartIds, customerId, CartConstant.CART_PRODUCT_STATUS_ISVALID);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [cartId, productNum, id]
     * @return {@link Boolean}
     * Description <修改商品数量和总价格>
     * @author GOLD
     * @date 2020/2/24 13:15
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Boolean modifyNumAndPriceByCartIdAndCustomerIdAndStatus(Integer cartId, Integer productNum, Integer id) {
        //拿到该商品信息，计算修改数量后的总价格
        Cart cart = cartDao.selectCartByCustomerIdAndCartId(id, cartId);
        Double totalPrice = (cart.getProduct().getPrice()) * productNum;
        DecimalFormat df = new DecimalFormat("#0.00");
        totalPrice = Double.parseDouble(df.format(totalPrice));

        int rows = cartDao.updateProductNumAndPriceByCartIdAndCustomerIdAndStatus(cartId, productNum,
                id, CartConstant.CART_PRODUCT_STATUS_VALID, totalPrice);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [orderCartIds, id]
     * @return {@link List< Cart>}
     * Description <显示购物车>
     * @author GOLD
     * @date 2020/2/24 13:15
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<Cart> findCartByCartIdsAndCustomerId(Integer[] orderCartIds, Integer id) {
        List<Cart> cartList = cartDao.selectCartByCartIdsAndCustomerId(orderCartIds, id, CartConstant.CART_PRODUCT_STATUS_VALID);
        if (cartList.size() == 0) {
            throw new OrderCartNotFoundException("购物车信息不存在");
        }
        return cartList;
    }

    /**
     * @param [orderCartIds, id]
     * @return {@link List< Cart>}
     * Description <显示购物车>
     * @author GOLD
     * @date 2020/2/24 13:15
     */
    @Override
    public List<Cart> findRedirectCartByCartIdsAndCustomerId(Integer[] orderCartIds, Integer id) {
        List<Cart> cartList = cartDao.selectRedirectCartByCartIdsAndCustomerId(orderCartIds, id, CartConstant.CART_PRODUCT_REDIRECT_TO_CART);
        if (cartList.size() == 0) {
            throw new OrderCartNotFoundException("购物车信息不存在");
        }
        return cartList;
    }

    /**
     * @param [cartVo]
     * @return {@link int}
     * Description <加入购物车>
     * @author GOLD
     * @date 2020/2/24 13:15
     */
    @Override
    public int redirectToCart(CartVo cartVo) {
        Cart cartResult = cartDao.selectRedirectCartByCustomerIdAndProductId(cartVo.getCustomerId(), cartVo.getProductId());
        //先查询一下购物车中是否有此商品，没有则插入保存，有的话就更新购物车的商品数量就可以了
        if (cartResult == null) {
            Cart cart = new Cart();
            Product product = productDao.selectProductById(cartVo.getProductId());
            //计算总价
            Double totalPrice = product.getPrice() * cartVo.getProductNum();
            DecimalFormat df = new DecimalFormat("#0.00");
            totalPrice = Double.parseDouble(df.format(totalPrice));

            BeanUtils.copyProperties(cartVo, cart);
            cart.setTotalPrice(totalPrice);
            cart.setProduct(product);
            //直接购买放入购物车
            cart.setStatus(CartConstant.CART_PRODUCT_REDIRECT_TO_CART);
            cart.setCreateTime(new Date());

            int rows = cartDao.insertCart(cart);
            if (rows >= 1) {
                return cart.getId();
            } else {
                return 0;
            }
        }

        //直接购买，这里没有叠加，更新购物车的商品数量
        int productSums = cartVo.getProductNum();
        Product pd = productDao.selectProductById(cartVo.getProductId());

        //更新商品总价格
        Double priceSum = pd.getPrice() * productSums;
        DecimalFormat df = new DecimalFormat("#0.00");
        priceSum = Double.parseDouble(df.format(priceSum));

        int rows = cartDao.updateCartNumAndTotalPriceById(cartResult.getId(), productSums, priceSum);
        if (rows >= 1) {
            return cartResult.getId();
        }
        return 0;
    }

}
