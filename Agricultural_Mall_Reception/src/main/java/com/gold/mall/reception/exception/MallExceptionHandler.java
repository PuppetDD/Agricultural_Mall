package com.gold.mall.reception.exception;

import com.gold.mall.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author GOLD
 * @date 2020/2/26 14:16
 * Description <描述>
 */
@Slf4j
@ControllerAdvice
public class MallExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView mallException(HttpServletRequest request, Exception e) {

        ModelAndView mv = null;
        log.info("=====================异常开始=====================");
        log(e, request);

        CustomerLoginNameIsExist customerLoginNameIsExist = null;
        CustomerNotFoundException customerNotFoundException = null;
        LoginErrorException loginErrorException = null;
        OrderCartNotFoundException orderCartNotFoundException = null;
        PhoneNotRegisterException phoneNotRegistException = null;
        ProductTypeExistException productTypeExistException = null;
        AddressException addressException = null;

        //判断是否是自定义异常,这里自定义异常其实开始设计一个就可以，传入不同的message 和code 即可
        if (e instanceof CustomerLoginNameIsExist) {
            customerLoginNameIsExist = (CustomerLoginNameIsExist) e;
            log.error("异常信息：{}", customerLoginNameIsExist.getMessage());
        } else if (e instanceof CustomerNotFoundException) {
            customerNotFoundException = (CustomerNotFoundException) e;
            log.error("异常信息：{}", customerNotFoundException.getMessage());
        } else if (e instanceof LoginErrorException) {
            loginErrorException = (LoginErrorException) e;
            log.error("异常信息：{}", loginErrorException.getMessage());
        } else if (e instanceof OrderCartNotFoundException) {
            orderCartNotFoundException = (OrderCartNotFoundException) e;
            log.error("异常信息：{}", orderCartNotFoundException.getMessage());
        } else if (e instanceof PhoneNotRegisterException) {
            phoneNotRegistException = (PhoneNotRegisterException) e;
            log.error("异常信息：{}", phoneNotRegistException.getMessage());
        } else if (e instanceof ProductTypeExistException) {
            productTypeExistException = (ProductTypeExistException) e;
            log.error("异常信息：{}", productTypeExistException.getMessage());
        } else if (e instanceof AddressException) {
            addressException = (AddressException) e;
            log.error("异常信息：{}", addressException.getMessage());
        } else if (e instanceof NoHandlerFoundException) {
            //4xx页面
            log.info("4xx,页面找不到：{}", e.getMessage());
            mv = new ModelAndView("/error/4xx");
            mv.addObject("errorMsg", "404,页面找不到!");
        } else {
            log.info("5xx,系统未知错误：{}", e.getMessage());
            //除了以上情况，其它定为系统内部异常，返回5xx页面
            //5xx页面
            mv = new ModelAndView("/error/5xx");
            mv.addObject("errorMsg", "服务器内部错误！");
        }
        return mv;
    }

    /**
     * @param [ex, request]
     *             Description <异常信息记录>
     * @author GOLD
     * @date 2020/2/26 17:17
     */
    private void log(Exception ex, HttpServletRequest request) {
        log.error("************************异常开始*******************************");
        log.error("请求地址：" + request.getRequestURL());
        Enumeration enumeration = request.getParameterNames();
        log.error("请求参数");
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement().toString();
            log.error(name + "---" + request.getParameter(name));
        }

        StackTraceElement[] error = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : error) {
            log.error(stackTraceElement.toString());
        }
        log.error("************************异常结束*******************************");
    }

}
