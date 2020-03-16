package com.gold.mall.dao;

import com.gold.mall.params.CustomerParam;
import com.gold.mall.pojo.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 13:17
 * Description <客户Dao>
 */
public interface CustomerDao {

    Customer selectCustomerById(int id);

    Customer selectByLoginNameAndPassword(@Param("loginName") String loginName,
                                          @Param("password") String password,
                                          @Param("isValid") Integer isValid);

    Customer selectByPhone(String phone);

    Customer selectByLoginName(String loginName);

    List<Customer> selectAllCustomers();

    List<Customer> selectCustomersByParams(CustomerParam customerParam);

    int insertCustomer(Customer customer);

    int updateCustomerPassword(Customer customer);

    int updateCustomer(Customer customer);

    int updateCustomerStatus(@Param("id") int id,
                             @Param("isValid") int isValid);

}
