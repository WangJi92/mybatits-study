package com.common.utils.Hibernatevalidatedemo.ValidateParameter;

import org.springframework.validation.FieldError;

import java.util.List;

/**
 * descrption: 参数异常，传递给上层应用进行处理
 * authohr: wangji
 * date: 2017-08-14 16:44
 */
public class ParamValidException  extends RuntimeException{

    private List<FieldError> fieldErrors;
    public ParamValidException(List<FieldError> errors) {
        this.fieldErrors = errors;
    }
    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
