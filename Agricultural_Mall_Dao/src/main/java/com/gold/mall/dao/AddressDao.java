package com.gold.mall.dao;

import com.gold.mall.pojo.Address;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 13:21
 * Description <收货地址Dao>
 */
public interface AddressDao {

    Address selectAddressByCustomerIdAndAddressId(@Param("customerId") Integer customerId,
                                                  @Param("addressId") Integer addressId);

    List<Address> selectAllAddress(@Param("customerId") Integer customerId,
                                   @Param("status") Integer status);

    int insertAddress(Address address);

    int deleteAddressByIdAndCustomerId(@Param("addressId") Integer addressId,
                                       @Param("customerId") Integer customerId,
                                       @Param("status") int status,
                                       @Param("updateTime") Date updateTime);

    int updateByAddress(Address address);

}
