package com.gold.mall.backstage.exception;

import com.gold.mall.common.exception.SystemUserLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author GOLD
 * @date 2020/2/24 14:37
 * Description <描述>
 */
@ControllerAdvice
@Slf4j
public class MallExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView mallException(HttpServletRequest request, Exception e) {

        ModelAndView mv = null;
        log.info("=====================异常开始=====================");
        log(e, request);

        SystemUserLoginException systemUserLoginException = null;

        //判断是否是自定义异常,这里自定义异常其实开始设计一个就可以，传入不同的message 和code 即可
        if (e instanceof SystemUserLoginException) {
            systemUserLoginException = (SystemUserLoginException) e;
            log.error("异常信息：{}", systemUserLoginException.getMessage());
        } else if (e instanceof NoHandlerFoundException) {
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
     * @date 2020/2/24 14:39
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
