package com.gold.mall.service.impl;

import com.gold.mall.common.constant.OrderConstant;
import com.gold.mall.common.config.WxPayConfig;
import com.gold.mall.common.utils.CommonUtils;
import com.gold.mall.common.utils.HttpUtils;
import com.gold.mall.common.utils.WxPayUtils;
import com.gold.mall.dao.OrderDao;
import com.gold.mall.dao.OrderItemDao;
import com.gold.mall.params.OrderParam;
import com.gold.mall.pojo.Order;
import com.gold.mall.pojo.OrderItem;
import com.gold.mall.service.OrderService;
import com.gold.mall.vo.OrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author GOLD
 * @date 2020/2/23 18:03
 * Description <订单impl>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    /**
     * @param []
     * @return {@link List<Order>}
     * Description <后台显示所有订单>
     * @author GOLD
     * @date 2020/2/24 22:56
     */
    @Override
    public List<Order> findAllOrders() {
        return orderDao.selectAllOrders();
    }

    /**
     * @param [orderVo]
     * @return {@link String}
     * Description <提交订单创建订单以及对应的订单明细>
     * @author GOLD
     * @date 2020/2/24 13:29
     */
    @Override
    public String saveOrder(OrderVo orderVo) {
        //保存订单
        Order order = new Order();
        BeanUtils.copyProperties(orderVo, order);
        //将订单写入数据库
        int row = orderDao.insertOrder(order);
        //将订单明细插入到数据库中
        List<OrderItem> orderItemList = orderVo.getOrderItemList();
        List<OrderItem> newOrderItemList = new ArrayList<>();

        if (row >= 1) {
            for (OrderItem orderItem : orderItemList) {
                //int rows = orderItemDao.insertOrderItem(orderItem);
                //都属于同一个订单
                orderItem.setOrder(order);
                newOrderItemList.add(orderItem);
            }
        }
        //插入订单明细
        int rows = orderItemDao.insertOrderItemByOrderItems(newOrderItemList);

        if (rows >= 1) {
            return order.getOrderNumber();
        }
        return null;
    }

    /**
     * @param [id]
     * @return {@link Order}
     * Description <查找订单>
     * @author GOLD
     * @date 2020/2/25 11:07
     */
    @Override
    public Order findOrderByID(Integer id) {
        return orderDao.selectOrderById(id);
    }

    /**
     * @param [orderNo, id]
     * @return {@link Order}
     * Description <根据客户id 和订单号查询一条订单>
     * @author GOLD
     * @date 2020/2/24 13:29
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Order findOrderIdByOrderNoAndCustomerId(String orderNo, Integer id) {
        Order order = orderDao.selectOrderIdByOrderNoAndCustomerId(orderNo, id);
        if (ObjectUtils.isEmpty(order)) {
            return null;
        }
        return order;
    }

    /**
     * @param [outTradeNo]
     * @return {@link Order}
     * Description <根据微信支付服务器响应的内容，拿到订单号查询是否存在>
     * @author GOLD
     * @date 2020/2/24 13:29
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Order findOrderByOutTradeNo(String outTradeNo) {
        return orderDao.selectOrderByOutTradeNo(outTradeNo);
    }

    @Override
    public List<Order> findOrdersByParams(OrderParam orderParam) {
        return orderDao.selectOrdersByParams(orderParam);
    }

    /**
     * @param [id]
     * @return {@link List< OrderVo>}
     * Description <获取某个用户的所有订单以及明细信息>
     * @author GOLD
     * @date 2020/2/24 13:29
     */
    @Override
    public List<OrderVo> getCustomerAllOrders(Integer id) {
        //先获取该用户的所有订单id
        List<Order> orderList = orderDao.selectAllOrderByCustomerId(id);

        List<OrderVo> orderVoList = new ArrayList<>();
        //循环遍历出每个订单对应的明细，每个封装到OrderVo中，最终返回一个orderVo的集合
        for (Order order : orderList) {
            //通过订单id 查询出该笔订单的明细
            OrderVo orderVo = new OrderVo();
            List<OrderItem> orderItemList = orderItemDao.selectOrderItemsByOrder(order.getId());
            BeanUtils.copyProperties(order, orderVo);
            orderVo.setOrderItemList(orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

    /**
     * @param [id, orderId]
     * @return {@link Boolean}
     * Description <取消订单操作>
     * @author GOLD
     * @date 2020/2/24 13:30
     */
    @Override
    public Boolean modifyOrderStatusByCustomerIdAndOrderId(Integer id, Integer orderId) {
        int rows = orderDao.updateOrderStatusByCustomerIdAndOrderId(id, orderId, OrderConstant.ORDER_STATUS_CANCEL_DEAL);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [id]
     * @return {@link int}
     * Description <后台删除订单>
     * @author GOLD
     * @date 2020/2/24 23:03
     */
    @Override
    public int removeOrderById(Integer id) {
        return orderDao.deleteOrderById(id);
    }

    /**
     * @param [id, removeOrderId]
     * @return {@link Boolean}
     * Description <删除订单操作>
     * @author GOLD
     * @date 2020/2/24 13:30
     */
    @Override
    public Boolean removeOrderByCustomerIdAndOrderId(Integer id, Integer removeOrderId) {
        int rows = orderDao.updateOrderStatusByCustomerIdAndOrderId(id, removeOrderId, OrderConstant.ORDER_STATUS_DELETE);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [id, confirmOrderId]
     * @return {@link Boolean}
     * Description <确认收货>
     * @author GOLD
     * @date 2020/2/24 13:30
     */
    @Override
    public Boolean confirmOrderByCustomerIdAndOrderId(Integer id, Integer confirmOrderId) {
        int rows = orderDao.updateOrderStatusByCustomerIdAndOrderId(id, confirmOrderId, OrderConstant.ORDER_STATUS_FINISH_DEAL);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [id, status]
     * @return {@link List< OrderVo>}
     * Description <获取不同状态的订单列表>
     * @author GOLD
     * @date 2020/2/24 13:30
     */
    @Override
    public List<OrderVo> getDifferenceStatusOrders(Integer id, Integer status) {
        //先获取该用户的所有订单id
        List<Order> orderList = orderDao.selectOrdersByCustomerId(id, status);

        List<OrderVo> orderVoList = new ArrayList<>();
        //循环遍历出每个订单对应的明细，每个封装到OrderVo中，最终返回一个orderVo的集合
        for (Order order : orderList) {
            //通过订单id 查询出该笔订单的明细
            OrderVo orderVo = new OrderVo();
            Integer orderId = order.getId();
            List<OrderItem> orderItemList = orderItemDao.selectOrderItemsByOrder(orderId);
            BeanUtils.copyProperties(order, orderVo);
            orderVo.setOrderItemList(orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

    /**
     * @param [customerVo]
     * @return {@link Boolean}
     * Description <后台修改订单的信息>
     * @author GOLD
     * @date 2020/2/24 13:19
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Boolean modifyOrder(OrderVo orderVo) {
        Order order = orderDao.selectOrderById(orderVo.getId());
        BeanUtils.copyProperties(orderVo, order);
        int rows = orderDao.updateOrder(order);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [id, out_trade_no]
     * @return {@link Boolean}
     * Description <支付成功，修改订单状态>
     * @author GOLD
     * @date 2020/2/24 13:31
     */
    @Override
    public Boolean modifyOrderStatusByCustomerIdAndOrderNo(Integer id, String out_trade_no) {
        int rows = orderDao.updateOrderStatusByCustomerIdAndOrderNo(id, out_trade_no, OrderConstant.ORDER_STATUS_PAID_NOTSHIPPED);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [out_trade_no]
     * @return {@link Boolean}
     * Description <支付宝异步通知，修改订单状态，异步通知session失效>
     * @author GOLD
     * @date 2020/2/24 13:31
     */
    @Override
    public Boolean modifyOrderStatusByOrderNo(String out_trade_no) {
        int rows = orderDao.updateOrderStatusByOrderNo(out_trade_no, OrderConstant.ORDER_STATUS_PAID_NOTSHIPPED);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [order, ip]
     * @return {@link String}
     * Description <微信支付业务处理，返回微信支付二维码>
     * @author GOLD
     * @date 2020/2/24 13:31
     */
    @Override
    public String getWxPayUrl(Order order, String ip) throws Exception {
        //生成签名
        SortedMap<String, String> params = new TreeMap<>();
        /*必传 微信分配的公众账号ID（企业号corpid即为此appId）*/
        params.put("appid", WxPayConfig.wxpay_appId);
        /*必传 微信支付分配的商户号*/
        params.put("mch_id", WxPayConfig.wxpay_mer_id);
        //随机字符串
        params.put("nonce_str", CommonUtils.generateUUID());
        /*商品描述，必传*/
        params.put("body", "A-Mall");
        /*商户订单号*/
        params.put("out_trade_no", order.getOrderNumber());

        /*总金额,单位为分,由元转化为分,*/
        int price = (int) (order.getPrice() * 100);

        params.put("total_fee", String.valueOf(price));

        /*终端IP,必传*/
        params.put("spbill_create_ip", ip);
        /*微信回调地址*/
        params.put("notify_url", WxPayConfig.wxpay_callback);
        /*trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义*/
        params.put("trade_type", "NATIVE");

        //sign 签名
        String sign = WxPayUtils.createSign(params, WxPayConfig.wxpay_key);
        params.put("sign", sign);

        //map转xml
        String mapToXml = WxPayUtils.mapToXml(params);

        System.out.println("mapToxml= " + mapToXml);
        //统一下单

        String payResult = HttpUtils.doPost(WxPayConfig.UNIFIED_ORDER_URL, mapToXml, 4000);

        if (null == payResult) {
            return null;
        }

        Map<String, String> payResultMap = WxPayUtils.xmlToMap(payResult);
        System.out.println("payResult= " + payResultMap.toString());
        if (payResultMap != null) {
            return payResultMap.get("code_url");
        }
        return null;
    }

    /**
     * @param [order, ip]
     * @return {@link Map< String, String>}
     * Description <微信支付>
     * @author GOLD
     * @date 2020/2/24 13:31
     */
    @Override
    public Map<String, String> getWxPayResultMap(Order order, String ip) throws Exception {
        //生成签名
        SortedMap<String, String> params = new TreeMap<>();
        /*必传 微信分配的公众账号ID（企业号corpid即为此appId）*/
        params.put("appid", WxPayConfig.wxpay_appId);
        /*必传 微信支付分配的商户号*/
        params.put("mch_id", WxPayConfig.wxpay_mer_id);
        //随机字符串
        params.put("nonce_str", CommonUtils.generateUUID());
        /*商品描述，必传*/
        params.put("body", "A-Mall");
        /*商户订单号*/
        params.put("out_trade_no", order.getOrderNumber());

        /*总金额,单位为分,由元转化为分,*/
        int price = (int) (order.getPrice() * 100);

        params.put("total_fee", String.valueOf(price));

        /*终端IP,必传*/
        params.put("spbill_create_ip", ip);
        /*微信回调地址*/
        params.put("notify_url", WxPayConfig.wxpay_callback);
        /*trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义*/
        params.put("trade_type", "NATIVE");

        //sign 签名
        String sign = WxPayUtils.createSign(params, WxPayConfig.wxpay_key);
        params.put("sign", sign);

        //map转xml
        String mapToXml = WxPayUtils.mapToXml(params);

        System.out.println("mapToxml= " + mapToXml);
        //统一下单

        String payResult = HttpUtils.doPost(WxPayConfig.UNIFIED_ORDER_URL, mapToXml, 4000);

        if (null == payResult) {
            return null;
        }

        Map<String, String> payResultMap = WxPayUtils.xmlToMap(payResult);
        if (payResultMap != null) {
            return payResultMap;
        }
        return null;
    }

}
