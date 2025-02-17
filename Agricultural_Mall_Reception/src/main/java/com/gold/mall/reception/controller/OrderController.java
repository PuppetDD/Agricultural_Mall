package com.gold.mall.reception.controller;

import com.gold.mall.common.constant.OrderConstant;
import com.gold.mall.common.exception.OrderCartNotFoundException;
import com.gold.mall.common.utils.ResponseResult;
import com.gold.mall.common.utils.StringUtil;
import com.gold.mall.pojo.*;
import com.gold.mall.service.AddressService;
import com.gold.mall.service.CartService;
import com.gold.mall.service.OrderItemService;
import com.gold.mall.service.OrderService;
import com.gold.mall.vo.CartVo;
import com.gold.mall.vo.OrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author GOLD
 * @date 2020/2/26 14:13
 * Description <订单接口>
 */
@Controller
@RequestMapping("/reception/order")
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    /**
     * @param [session, model]
     * @return {@link String}
     * Description <客户所有订单展示列表>
     * @author GOLD
     * @date 2020/2/26 17:08
     */
    @RequestMapping("myOrders")
    public String myOrders(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            //需要登录拦截器
            return "main";
        }
        //获取所有订单列表
        List<OrderVo> orderVoList = orderService.getCustomerAllOrders(customer.getId());
        model.addAttribute("orderVoList", orderVoList);
        return "myOrders";
    }

    /**
     * @param [session]
     * @return {@link List< Address>}
     * Description <加载地址>
     * @author GOLD
     * @date 2020/2/26 17:08
     */
    @ModelAttribute("addressList")
    public List<Address> loadAddress(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        List<Address> addressList = addressService.findCustomerAllAddress(customer.getId());
        return addressList;
    }

    /**
     * @param [session]
     * @return {@link Map< String, Object>}
     * Description <显示不同订单状态的数量>
     * @author GOLD
     * @date 2020/2/26 17:09
     */
    @ModelAttribute("map")
    public Map<String, Object> loadNumOrder(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        //获取未支付的订单
        List<OrderVo> orderVoList = orderService.getDifferenceStatusOrders(customer.getId(), OrderConstant.ORDER_STATUS_UNPAID_NOTSHIPPED);
        //获取待发货的订单列表
        List<OrderVo> orderVoList1 = orderService.getDifferenceStatusOrders(customer.getId(), OrderConstant.ORDER_STATUS_PAID_NOTSHIPPED);
        //获取待收货的订单列表
        List<OrderVo> orderVoList11 = orderService.getDifferenceStatusOrders(customer.getId(), OrderConstant.ORDER_STATUS_SHIPPED_UNRECEIVE);
        //获取待评价的订单列表
        List<OrderVo> orderVoList2 = orderService.getDifferenceStatusOrders(customer.getId(), OrderConstant.ORDER_STATUS_FINISH_DEAL);
        //获取已取消的订单列表
        List<OrderVo> orderVoList3 = orderService.getDifferenceStatusOrders(customer.getId(), OrderConstant.ORDER_STATUS_CANCEL_DEAL);
        //获取回收站的订单列表
        List<OrderVo> orderVoList4 = orderService.getDifferenceStatusOrders(customer.getId(), OrderConstant.ORDER_STATUS_DELETE);

        Map<String, Object> map = new HashMap<>();
        map.put("unpaid", orderVoList.size());
        map.put("notShipped", orderVoList1.size());
        map.put("unReceive", orderVoList11.size());
        map.put("finished", orderVoList2.size());
        map.put("cancel", orderVoList3.size());
        map.put("delete", orderVoList4.size());
        return map;
    }

    /**
     * @param [session, model]
     * @return {@link String}
     * Description <确认订单，此接口提供展示客户的确认订单数据>
     * @author GOLD
     * @date 2020/2/26 17:09
     */
    @RequestMapping("confirmOrder")
    public String confirmOrder(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");

        Double price = (Double) session.getAttribute("price");
        Integer count = (Integer) session.getAttribute("count");
        Integer[] orderCartIds = (Integer[]) session.getAttribute("orderCartIds");

        if (ObjectUtils.isEmpty(customer)) {
            model.addAttribute("failMsg", "客官，请先登录");
        }
        try {
            List<Cart> orderList = cartService.findCartByCartIdsAndCustomerId(orderCartIds, customer.getId());
            model.addAttribute("orderList", orderList);
            model.addAttribute("price", price);
            model.addAttribute("count", count);
        } catch (OrderCartNotFoundException e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "order";
    }

    /**
     * @param [addressId, status, session]
     * @return {@link ResponseResult}
     * Description <创建订单 和订单明细 ，另外订单生成成功，需要将对应购物车移除>
     * @author GOLD
     * @date 2020/2/26 17:09
     */
    @RequestMapping("addOrder")
    @ResponseBody
    public ResponseResult addOrder(Integer addressId, Integer status, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("登录超时，请重新登录");
        }
        Double price = (Double) session.getAttribute("price");
        Integer count = (Integer) session.getAttribute("count");
        Integer[] orderCartIds = (Integer[]) session.getAttribute("orderCartIds");

        //获取该笔订单中所有的明细
        List<Cart> cartList = null;
        if (status == 2) {
            cartList = cartService.findRedirectCartByCartIdsAndCustomerId(orderCartIds, customer.getId());
        } else {
            cartList = cartService.findCartByCartIdsAndCustomerId(orderCartIds, customer.getId());
        }
        //获取该笔订单的收获信息
        Address address = addressService.findAddressByCustomerIdAndAddressId(customer.getId(), addressId);

        //生成一个订单
        OrderVo orderVo = new OrderVo();
        Order order = new Order();

        String addressInfo = address.getReceiverProvince() + address.getReceiverCity()
                + address.getReceiverDistrict() + address.getAddressDetail();
        orderVo.setAddress(addressInfo);
        orderVo.setCreateDate(new Date());
        orderVo.setCustomer(customer);
        orderVo.setOrderNumber(StringUtil.getOrderIdByUUId());
        orderVo.setPrice(price);
        orderVo.setProductNumber(count);
        orderVo.setStatus(OrderConstant.ORDER_STATUS_UNPAID_NOTSHIPPED);
        //属性拷贝
        BeanUtils.copyProperties(orderVo, order);

        //订单明细
        List<OrderItem> orderItems = new ArrayList<>();
        //遍历购物车中的条目，对订单类目进行属性赋值
        for (Cart cart : cartList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setNum(cart.getProductNum());
            orderItem.setPrice(cart.getTotalPrice());
            orderItem.setProduct(cart.getProduct());
            //设置所属订单，这样会导致获取不到订单的id，会有问题。应该插入order后获取，这样让order插入自己生成主键并返回
            //orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        //将所有的订单条目加入到订单中
        orderVo.setOrderItemList(orderItems);
        String orderNo = orderService.saveOrder(orderVo);
        if (orderNo != null) {
            //将订单号放到 session 中去
            session.setAttribute("orderNo", orderNo);
            //将购物车对应的商品移除
            Boolean flag = cartService.modifyCartStatusByCartIdAndCustomerIds(orderCartIds, customer.getId());
            if (flag) {
                session.removeAttribute("orderCartIds");
            } else {
                return ResponseResult.fail("购物车清空失败");
            }
            return ResponseResult.success(orderNo);
        }
        return ResponseResult.fail("订单创建失败");
    }

    /**
     * @param [session, model]
     * @return {@link String}
     * Description <没有立即支付，则进入展示订单详情页面>
     * @author GOLD
     * @date 2020/2/26 17:09
     */
    @RequestMapping("showOrderDetails")
    public String showOrderDetails(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            //用户没有登录则让其登录，后续需要加上拦截器
            return "main";
        }
        String orderNo = (String) session.getAttribute("orderNo");
        Order order = orderService.findOrderIdByOrderNoAndCustomerId(orderNo, customer.getId());
        if (order != null) {
            List<OrderItem> orderItemList = orderItemService.findOrderItemsByOrderId(order.getId());
            model.addAttribute("orderItems", orderItemList);
            model.addAttribute("order", order);
        }
        return "orderDetail";
    }

    /**
     * @param [session, orderNo, model]
     * @return {@link String}
     * Description <客户所有订单页面，点击订单号可以查看详情>
     * @author GOLD
     * @date 2020/2/26 17:09
     */
    @RequestMapping("showOrderDetailInfo")
    public String showOrderDetailInfo(HttpSession session, String orderNo, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            //用户没有登录则让其登录，后续需要加上拦截器
            return "main";
        }
        Order order = orderService.findOrderIdByOrderNoAndCustomerId(orderNo, customer.getId());
        if (order != null) {
            List<OrderItem> orderItemList = orderItemService.findOrderItemsByOrderId(order.getId());
            model.addAttribute("orderItems", orderItemList);
            model.addAttribute("order", order);
        }
        return "orderDetail";
    }

    /**
     * @param [orderId, session]
     * @return {@link ResponseResult}
     * Description <取消订单>
     * @author GOLD
     * @date 2020/2/26 17:10
     */
    @RequestMapping("cancelOrder")
    @ResponseBody
    public ResponseResult cancelOrder(Integer orderId, HttpSession session) {

        Customer customer = (Customer) session.getAttribute("customer");

        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("请先登录");
        }

        Boolean flag = orderService.modifyOrderStatusByCustomerIdAndOrderId(customer.getId(), orderId);
        if (flag) {
            return ResponseResult.success("订单已取消");
        }
        return ResponseResult.fail("订单取消失败");
    }

    /**
     * @param [removeOrderId, session]
     * @return {@link ResponseResult}
     * Description <删除订单>
     * @author GOLD
     * @date 2020/2/26 17:10
     */
    @RequestMapping("removeOrder")
    @ResponseBody
    public ResponseResult removeOrder(Integer removeOrderId, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("请先登录");
        }
        if (orderService.removeOrderByCustomerIdAndOrderId(customer.getId(), removeOrderId)) {
            return ResponseResult.success("订单已删除");
        }
        return ResponseResult.fail("订单删除失败");
    }

    /**
     * @param [confirmOrderId, session]
     * @return {@link ResponseResult}
     * Description <确认收货>
     * @author GOLD
     * @date 2020/2/26 17:10
     */
    @RequestMapping("confirmReceiveOrder")
    @ResponseBody
    public ResponseResult confirmReceiveOrder(Integer confirmOrderId, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("请先登录");
        }

        if (orderService.confirmOrderByCustomerIdAndOrderId(customer.getId(), confirmOrderId)) {
            return ResponseResult.success("已确认收货，交易完成");
        }
        return ResponseResult.fail("确认收货失败");
    }

    /**
     * @param [session, model]
     * @return {@link String}
     * Description <展示没有支付的订单列表>
     * @author GOLD
     * @date 2020/2/26 17:10
     */
    @RequestMapping("showNotPaid")
    public String showNotPaid(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            //需要登录拦截器
            return "main";
        }
        //获取所有订单列表
        List<OrderVo> orderVoList = orderService.getDifferenceStatusOrders(customer.getId(), OrderConstant.ORDER_STATUS_UNPAID_NOTSHIPPED);
        model.addAttribute("orderVoList", orderVoList);
        return "myOrders";
    }

    /**
     * @param [session, model]
     * @return {@link String}
     * Description <展示没有发货的订单列表>
     * @author GOLD
     * @date 2020/2/26 17:11
     */
    @RequestMapping("showNotShipped")
    public String showNotShipped(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            //需要登录拦截器
            return "main";
        }
        //获取所有订单列表
        List<OrderVo> orderVoList = orderService.getDifferenceStatusOrders(customer.getId(), OrderConstant.ORDER_STATUS_PAID_NOTSHIPPED);
        model.addAttribute("orderVoList", orderVoList);
        return "myOrders";
    }

    /**
     * @param [session, model]
     * @return {@link String}
     * Description <展示未收货的订单列表>
     * @author GOLD
     * @date 2020/2/26 17:11
     */
    @RequestMapping("showReceive")
    public String showReceive(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            //需要登录拦截器
            return "main";
        }
        //获取所有订单列表
        List<OrderVo> orderVoList = orderService.getDifferenceStatusOrders(customer.getId(), OrderConstant.ORDER_STATUS_SHIPPED_UNRECEIVE);
        model.addAttribute("orderVoList", orderVoList);
        return "myOrders";
    }

    /**
     * @param [session, model]
     * @return {@link String}
     * Description <展示已完成交易的订单列表>
     * @author GOLD
     * @date 2020/2/26 17:11
     */
    @RequestMapping("showFinished")
    public String showFinished(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            //需要登录拦截器
            return "main";
        }
        //获取所有订单列表
        List<OrderVo> orderVoList = orderService.getDifferenceStatusOrders(customer.getId(), OrderConstant.ORDER_STATUS_FINISH_DEAL);
        model.addAttribute("orderVoList", orderVoList);
        return "myOrders";
    }

    /**
     * @param [session, model]
     * @return {@link String}
     * Description <展示已经取消的订单列表>
     * @author GOLD
     * @date 2020/2/26 17:11
     */
    @RequestMapping("showCancel")
    public String showCancel(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            //需要登录拦截器
            return "main";
        }
        //获取所有订单列表
        List<OrderVo> orderVoList = orderService.getDifferenceStatusOrders(customer.getId(), OrderConstant.ORDER_STATUS_CANCEL_DEAL);
        model.addAttribute("orderVoList", orderVoList);
        return "myOrders";
    }

    /**
     * @param [session, model]
     * @return {@link String}
     * Description <展示回收站的订单列表>
     * @author GOLD
     * @date 2020/2/26 17:12
     */
    @RequestMapping("showDelete")
    public String showDelete(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            //需要登录拦截器
            return "main";
        }
        //获取所有订单列表
        List<OrderVo> orderVoList = orderService.getDifferenceStatusOrders(customer.getId(), OrderConstant.ORDER_STATUS_DELETE);
        model.addAttribute("orderVoList", orderVoList);
        return "myOrders";
    }

    /**
     * @param [orderNumber, session, model]
     * @return {@link String}
     * Description <跳转到支付页面>
     * @author GOLD
     * @date 2020/2/26 17:12
     */
    @RequestMapping("showPayOrders")
    public String showPayOrders(String orderNumber, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return "redirect:/reception/product/searchAllProducts";
        }
        Order order = orderService.findOrderIdByOrderNoAndCustomerId(orderNumber, customer.getId());
        model.addAttribute("order", order);
        return "pay";
    }

    /**
     * @param [count, productId, session]
     * @return {@link ResponseResult}
     * Description <点击直接购买，先将物品放入购物车中>
     * @author GOLD
     * @date 2020/2/26 17:12
     */
    @RequestMapping("redirectBuyToCart")
    @ResponseBody
    public ResponseResult redirectBuyToCart(Integer count, Integer productId, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("请先登录");
        } else {

            //保存到购物车
            //Cart cart = new Cart();
            //cart.setStatus(CartConstant.CART_PRODUCT_STATUS_VALID);
            //cart.setTotalPrice(price);
            //cart.setProduct(product);
            //cart.setProductNum(count);
            //cart.setCustomerId(customer.getId());
            //cart.setCreateTime(new Date());

            CartVo cartVo = new CartVo();
            cartVo.setProductId(productId);
            cartVo.setCustomerId(customer.getId());
            cartVo.setProductNum(count);

            int cartId = cartService.redirectToCart(cartVo);
            if (cartId == 0) {
                return ResponseResult.fail("商品放入购物车失败");
            }
            return ResponseResult.success(cartId);
        }
    }

    /**
     * @param [cartId, session, model]
     * @return {@link String}
     * Description <直接购买，进入订单确认页面>
     * @author GOLD
     * @date 2020/2/26 17:12
     */
    @RequestMapping("redirectConfirmOrder")
    public String redirectConfirmOrder(Integer cartId, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");

        if (ObjectUtils.isEmpty(customer)) {
            return "redirect:/reception/product/searchAllProducts";
        }

        Integer[] orderCartIds = {cartId};

        try {
            List<Cart> orderList = cartService.findRedirectCartByCartIdsAndCustomerId(orderCartIds, customer.getId());
            model.addAttribute("orderList", orderList);
            model.addAttribute("price", orderList.get(0).getTotalPrice());
            model.addAttribute("count", orderList.get(0).getProductNum());

            //保存到session中 这里可以考虑使用其他方式处理，为了不修改原来的生成订单逻辑，这里暂且先这样处理
            session.setAttribute("price", orderList.get(0).getTotalPrice());
            session.setAttribute("count", orderList.get(0).getProductNum());
            session.setAttribute("orderCartIds", orderCartIds);
        } catch (OrderCartNotFoundException e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "order";
    }

}
