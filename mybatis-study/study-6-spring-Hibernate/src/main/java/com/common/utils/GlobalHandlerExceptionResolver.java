package com.common.utils;

import com.common.utils.Hibernatevalidatedemo.ValidateParameter.ParamValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * descrption: 全局异常处理
 * authohr: wangji
 * date: 2017-08-01 8:54
 */
@Slf4j
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {

    private MappingJackson2JsonView  jsonView = new MappingJackson2JsonView();
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(jsonView);
        modelAndView.addObject("error",e.getMessage());
        if(e instanceof ConstraintViolationException){
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) e).getConstraintViolations();
            for(ConstraintViolation item : constraintViolations){
                log.info("Item:"+item.getPropertyPath().toString()+"  message:"+item.getMessage());
            }
        }else if(e instanceof ParamValidException){
           List<FieldError> fieldErrorList =  ((ParamValidException) e).getFieldErrors();
           for(FieldError fieldError:fieldErrorList){
               log.info("参数:"+fieldError.getField()+"\t错误原因:"+fieldError.getDefaultMessage());
           }
        }
        modelAndView.addObject("result",false);
        return modelAndView;
    }
}
