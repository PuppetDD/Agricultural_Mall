package com.gold.mall.service;

import com.gold.mall.params.CustomerParam;
import com.gold.mall.pojo.Customer;
import com.gold.mall.vo.CustomerVo;

import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 16:27
 * Description <客户Service>
 */
public interface CustomerService {

    Customer login(String loginName, String password);

    Customer findByPhone(String phone);

    Customer register(CustomerVo customerVo);

    Boolean modifyCustomerPassword(Customer customer);

    Boolean findByLoginName(String loginName);

    List<Customer> findAllCustomers();

    List<Customer> findCustomersByParams(CustomerParam customerParam);

    Customer findCustomerId(int id);

    Boolean modifyCustomer(CustomerVo customerVo);

    Boolean modifyCustomerStatus(int id);

    Boolean modifyCenterCustomer(Integer customerId, String mobile, String address);

}
