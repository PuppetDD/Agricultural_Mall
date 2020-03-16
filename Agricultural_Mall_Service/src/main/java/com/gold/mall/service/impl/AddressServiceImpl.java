package com.gold.mall.service.impl;

import com.gold.mall.common.constant.AddressConstant;
import com.gold.mall.common.exception.AddressException;
import com.gold.mall.dao.AddressDao;
import com.gold.mall.pojo.Address;
import com.gold.mall.service.AddressService;
import com.gold.mall.vo.AddressVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 18:00
 * Description <收货地址impl>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    /**
     * @param [customerId, addressId]
     * @return {@link Address}
     * Description <查找某个用户的某个收货地址>
     * @author GOLD
     * @date 2020/2/24 13:11
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Address findAddressByCustomerIdAndAddressId(Integer customerId, Integer addressId) throws AddressException {
        Address address = addressDao.selectAddressByCustomerIdAndAddressId(customerId, addressId);
        if (address == null) {
            throw new AddressException("该地址不存在");
        }
        return address;
    }

    /**
     * @param [customerId]
     * @return {@link List< Address>}
     * Description <查询用户的所有收货地址>
     * @author GOLD
     * @date 2020/2/24 13:11
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<Address> findCustomerAllAddress(Integer customerId) {
        List<Address> addressList = addressDao.selectAllAddress(customerId, AddressConstant.SHIPPING_COMMON_STATUS);
        if (addressList.size() == 0) {
            return null;
        }
        return addressList;
    }

    /**
     * @param [addressVo, customerId]
     * @return {@link int}
     * Description <保存收货地址>
     * @author GOLD
     * @date 2020/2/24 13:12
     */
    @Override
    public int saveAddress(AddressVo addressVo, Integer customerId) throws AddressException {
        Address address = new Address();
        BeanUtils.copyProperties(addressVo, address);
        //设置客户id
        address.setCustomerId(customerId);
        //创建时间
        address.setCreateTime(new Date());
        //更新时间，开始默认和创建时间一致
        address.setUpdateTime(new Date());
        /*普通状态是0*/
        address.setStatus(AddressConstant.SHIPPING_COMMON_STATUS);
        int rows = addressDao.insertAddress(address);
        if (rows >= 1) {
            return address.getId();
        }
        throw new AddressException("收货地址添加失败");
    }

    /**
     * @param [addressVo, customerId]
     * @return {@link Boolean}
     * Description <修改地址信息>
     * @author GOLD
     * @date 2020/2/24 13:12
     */
    @Override
    public Boolean modifyAddress(AddressVo addressVo, Integer customerId) {
        Address address = addressDao.selectAddressByCustomerIdAndAddressId(customerId, addressVo.getId());
        if (address == null) {
            //这里可以设计抛出有一个异常
            throw new AddressException("改地址信息不存在");
        }
        //属性拷贝
        BeanUtils.copyProperties(addressVo, address);
        //更新更新时间
        address.setUpdateTime(new Date());
        int rows = addressDao.updateByAddress(address);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [addressId, customerId]
     * @return {@link Boolean}
     * Description <删除一个地址信息，这里实际上不删除，改变一下地址状态为status 0>
     * @author GOLD
     * @date 2020/2/24 13:12
     */
    @Override
    public Boolean removeAddress(Integer addressId, Integer customerId) {
        //删除时间也要更新一下
        Date updateTime = new Date();
        int rows = addressDao.deleteAddressByIdAndCustomerId(addressId, customerId, AddressConstant.SHIPPING_ISVALID_STATUS, updateTime);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

}
