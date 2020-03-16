package com.gold.mall.reception.controller;

import com.gold.mall.common.exception.AddressException;
import com.gold.mall.common.utils.ResponseResult;
import com.gold.mall.pojo.Address;
import com.gold.mall.pojo.Customer;
import com.gold.mall.service.AddressService;
import com.gold.mall.vo.AddressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GOLD
 * @date 2020/2/26 14:14
 * Description <收货地址接口>
 */
@Controller
@RequestMapping("/reception/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * @param [addressVo, session, model]
     * @return {@link ResponseResult}
     * Description <添加收货地址>
     * @author GOLD
     * @date 2020/2/26 16:54
     */
    @RequestMapping("saveAddress")
    @ResponseBody
    public ResponseResult saveAddress(AddressVo addressVo, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("请先登录");
        }
        try {
            int addressId = addressService.saveAddress(addressVo, customer.getId());
            model.addAttribute("addressId", addressId);
            return ResponseResult.success("地址新增成功");
        } catch (AddressException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    /**
     * @param [session, model]
     * @return {@link ResponseResult}
     * Description <获取客户所有的收货地址>
     * @author GOLD
     * @date 2020/2/26 16:54
     */
    @RequestMapping("findAllAddress")
    @ResponseBody
    public ResponseResult findAllAddress(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("请先登录");
        }
        List<Address> addressList = addressService.findCustomerAllAddress(customer.getId());
        model.addAttribute("addressList", addressList);
        return ResponseResult.success();
    }

    /**对于三级联动地址，这里时候ajax异步然后后端来校验是否用户选了地址信息,需要考虑下前端如何校验好*/

    /**
     * @param [receiverProvince]
     * @return {@link Map< String, Object>}
     * Description <省份校验，校验是否已经选择过省份，默认值是省份>
     * @author GOLD
     * @date 2020/2/26 16:56
     */
    @RequestMapping("checkProvince")
    @ResponseBody
    public Map<String, Object> checkProvince(String receiverProvince) {
        Map<String, Object> map = new HashMap<>();
        if (receiverProvince == null || "省份".equals(receiverProvince)) {
            map.put("valid", false);
            map.put("message", "请选择省份");
            return map;
        }
        map.put("valid", true);
        return map;
    }

    /**
     * @param [receiverCity]
     * @return {@link Map< String, Object>}
     * Description <城市校验>
     * @author GOLD
     * @date 2020/2/26 16:57
     */
    @RequestMapping("checkCity")
    @ResponseBody
    public Map<String, Object> checkCity(String receiverCity) {
        Map<String, Object> map = new HashMap<>();
        if (receiverCity == null || "地级市".equals(receiverCity)) {
            map.put("valid", false);
            map.put("message", "请选择地级市");
            return map;
        }
        map.put("valid", true);
        return map;
    }

    /**
     * @param [receiverDistrict]
     * @return {@link Map< String, Object>}
     * Description <区县校验>
     * @author GOLD
     * @date 2020/2/26 16:57
     */
    @RequestMapping("checkDistrict")
    @ResponseBody
    public Map<String, Object> checkDistrict(String receiverDistrict) {
        Map<String, Object> map = new HashMap<>();
        if (receiverDistrict == null || "县级市".equals(receiverDistrict)) {
            map.put("valid", false);
            map.put("message", "请选择县级市");
            return map;
        }
        map.put("valid", true);
        return map;
    }

    /**
     * @param [addressId, session]
     * @return {@link ResponseResult}
     * Description <移除地址卡片>
     * @author GOLD
     * @date 2020/2/26 16:57
     */
    @RequestMapping("removeAddress")
    @ResponseBody
    public ResponseResult removeAddress(Integer addressId, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("请先登录");
        }
        if (addressService.removeAddress(addressId, customer.getId())) {
            return ResponseResult.success("改地址已经成功移除");
        }
        return ResponseResult.fail("地址移除失败");
    }

    /**
     * @param [addressId, session]
     * @return {@link ResponseResult}
     * Description <显示某一条地址详情>
     * @author GOLD
     * @date 2020/2/26 16:58
     */
    @RequestMapping("showOneAddress")
    @ResponseBody
    public ResponseResult showOneAddress(Integer addressId, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("请先登录");
        }
        try {
            Address address = addressService.findAddressByCustomerIdAndAddressId(customer.getId(), addressId);
            return ResponseResult.success(address);
        } catch (AddressException e) {
            return ResponseResult.fail(e.getMessage());
        }
    }

    /**
     * @param [addressVo, session]
     * @return {@link ResponseResult}
     * Description <修改地址>
     * @author GOLD
     * @date 2020/2/26 16:58
     */
    @RequestMapping("modifyAddress")
    @ResponseBody
    public ResponseResult modifyAddress(AddressVo addressVo, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (ObjectUtils.isEmpty(customer)) {
            return ResponseResult.fail("请先登录");
        }
        if (addressService.modifyAddress(addressVo, customer.getId())) {
            return ResponseResult.success("地址修改成功");
        }
        return ResponseResult.fail("地址修改失败");
    }

}
