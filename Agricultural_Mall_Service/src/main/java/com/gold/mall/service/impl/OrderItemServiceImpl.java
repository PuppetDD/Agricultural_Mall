package com.gold.mall.service.impl;

import com.gold.mall.dao.OrderItemDao;
import com.gold.mall.pojo.OrderItem;
import com.gold.mall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 18:03
 * Description <订单类目impl>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemDao orderItemDao;

    /**
     * @param [orderItem]
     * @return {@link Boolean}
     * Description <保存订单明细>
     * @author GOLD
     * @date 2020/2/24 13:28
     */
    @Override
    public Boolean saveOrderItem(OrderItem orderItem) {
        int rows = orderItemDao.insertOrderItem(orderItem);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [id]
     * @return {@link List< OrderItem>}
     * Description <根据订单id 查询所属的所有订单明细>
     * @author GOLD
     * @date 2020/2/24 13:28
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<OrderItem> findOrderItemsByOrderId(Integer id) {
        List<OrderItem> orderItems = orderItemDao.selectOrderItemsByOrder(id);
        return orderItems;
    }

}
