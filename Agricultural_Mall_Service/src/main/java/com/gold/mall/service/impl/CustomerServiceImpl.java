package com.gold.mall.service.impl;

import com.gold.mall.common.constant.CustomerConstant;
import com.gold.mall.common.exception.CustomerLoginNameIsExist;
import com.gold.mall.common.exception.CustomerNotFoundException;
import com.gold.mall.common.exception.LoginErrorException;
import com.gold.mall.common.exception.PhoneNotRegisterException;
import com.gold.mall.dao.CustomerDao;
import com.gold.mall.params.CustomerParam;
import com.gold.mall.pojo.Customer;
import com.gold.mall.service.CustomerService;
import com.gold.mall.vo.CustomerVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 18:02
 * Description <客户impl>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    /**
     * @param [loginName, password]
     * @return {@link Customer}
     * Description <用户登录>
     * @author GOLD
     * @date 2020/2/24 13:15
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Customer login(String loginName, String password) throws LoginErrorException {
        Customer customer = customerDao.selectByLoginNameAndPassword(loginName, password, CustomerConstant.CUSTOMER_IS_VALID);
        if (customer == null) {
            throw new LoginErrorException("登录失败，用户名或密码错误");
        }
        return customer;
    }

    /**
     * @param [phone]
     * @return {@link Customer}
     * Description <手机验证码快速登录>
     * @author GOLD
     * @date 2020/2/24 13:15
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Customer findByPhone(String phone) throws PhoneNotRegisterException {
        Customer customer = customerDao.selectByPhone(phone);
        if (customer == null) {
            throw new PhoneNotRegisterException("该手机号码尚未注册");
        }
        return customer;
    }

    /**
     * @param [customerVo]
     * @return {@link Customer}
     * Description <用户注册>
     * @author GOLD
     * @date 2020/2/24 13:16
     */
    @Override
    public Customer register(CustomerVo customerVo) {
        Customer customer = new Customer();

        BeanUtils.copyProperties(customerVo, customer);
        //刚开始注册，默认账户激活状态
        customer.setIsValid(CustomerConstant.CUSTOMER_IS_VALID);
        customer.setRegisterDate(new Date());

        int rows = customerDao.insertCustomer(customer);
        if (rows >= 1) {
            return customer;
        }
        return null;
    }

    /**
     * @param [customer]
     * @return {@link Boolean}
     * Description <用户修改密码>
     * @author GOLD
     * @date 2020/2/24 13:17
     */
    @Override
    public Boolean modifyCustomerPassword(Customer customer) {
        int rows = customerDao.updateCustomerPassword(customer);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [loginName]
     * @return {@link Boolean}
     * Description <根据用户登录名查找用户,可用返回true>
     * @author GOLD
     * @date 2020/2/24 13:17
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public Boolean findByLoginName(String loginName) throws CustomerLoginNameIsExist {
        Customer customer = customerDao.selectByLoginName(loginName);
        if (customer == null) {
            return true;
        }
        throw new CustomerLoginNameIsExist("该登录名已经存在");
    }

    /**
     * @param []
     * @return {@link List< Customer>}
     * Description <获取所有的用户列表>
     * @author GOLD
     * @date 2020/2/24 13:17
     */
    @Override
    public List<Customer> findAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    /**
     * @param [customerParam]
     * @return {@link List< Customer>}
     * Description <参数查找客户>
     * @author GOLD
     * @date 2020/2/24 13:17
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<Customer> findCustomersByParams(CustomerParam customerParam) {
        return customerDao.selectCustomersByParams(customerParam);
    }

    /**
     * @param [id]
     * @return {@link Customer}
     * Description <根据id显示用户信息>
     * @author GOLD
     * @date 2020/2/24 13:18
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Customer findCustomerId(int id) {
        Customer customer = customerDao.selectCustomerById(id);
        if (ObjectUtils.isEmpty(customer)) {
            throw new CustomerNotFoundException("该用户不存在");
        }
        return customer;
    }

    /**
     * @param [customerVo]
     * @return {@link Boolean}
     * Description <修改客户的信息>
     * @author GOLD
     * @date 2020/2/24 13:19
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Boolean modifyCustomer(CustomerVo customerVo) {
        Customer customer = customerDao.selectCustomerById(customerVo.getId());
        BeanUtils.copyProperties(customerVo, customer);
        int rows = customerDao.updateCustomer(customer);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [id]
     * @return {@link Boolean}
     * Description <修改客户的状态>
     * @author GOLD
     * @date 2020/2/24 13:20
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Boolean modifyCustomerStatus(int id) {
        Customer customer = customerDao.selectCustomerById(id);
        int isValid = customer.getIsValid();
        if (isValid == CustomerConstant.CUSTOMER_IS_VALID) {
            isValid = CustomerConstant.CUSTOMER_IS_INVALID;
        } else {
            isValid = CustomerConstant.CUSTOMER_IS_VALID;
        }

        int rows = customerDao.updateCustomerStatus(id, isValid);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * @param [customerId, mobile, address]
     * @return {@link Boolean}
     * Description <修改用户信息>
     * @author GOLD
     * @date 2020/2/24 13:20
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Boolean modifyCenterCustomer(Integer customerId, String mobile, String address) {
        Customer customer = customerDao.selectCustomerById(customerId);
        if (customer != null) {
            customer.setAddress(address);
            customer.setPhone(mobile);
            int rows = customerDao.updateCustomer(customer);
            if (rows >= 1) {
                return true;
            }
        }
        return false;
    }

}
