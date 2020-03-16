package com.gold.mall.service;

import com.gold.mall.pojo.Address;
import com.gold.mall.vo.AddressVo;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 16:30
 * Description <收货地址Service>
 */
public interface AddressService {

    Address findAddressByCustomerIdAndAddressId(Integer customerId, Integer addressId);

    List<Address> findCustomerAllAddress(Integer customerId);

    int saveAddress(AddressVo addressVo, Integer customerId);

    Boolean modifyAddress(AddressVo addressVo, Integer customerId);

    Boolean removeAddress(Integer addressId, Integer customerId);

}
