package com.common.utils;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * descrption: 全局异常处理
 * authohr: wangji
 * date: 2017-08-01 8:54
 */
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {

    private MappingJackson2JsonView  jsonView = new MappingJackson2JsonView();
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(jsonView);
        modelAndView.addObject("error",e.getMessage());
        modelAndView.addObject("result",false);
        return modelAndView;
    }
}
