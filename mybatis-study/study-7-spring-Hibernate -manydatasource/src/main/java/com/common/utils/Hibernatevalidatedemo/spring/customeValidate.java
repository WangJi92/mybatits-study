package com.common.utils.Hibernatevalidatedemo.spring;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * descrption: spring中自定义约束其
 * authohr: wangji
 * date: 2017-08-12 15:26
 */
public class customeValidate implements ConstraintValidator<GenderAnnotation,Gender>{

    //As you can see, a ConstraintValidator implementation may have its dependencies @Autowired like any other Spring bean.
    //在spring 你可以直接在这里面注入@Resource 自己需要的Bean处理器
    Gender value;

    public void initialize(GenderAnnotation constraintAnnotation) {
        value= constraintAnnotation.value();//获取注解的值
    }
    public boolean isValid(Gender value, ConstraintValidatorContext context) {
        if(!(value instanceof  Gender) || value ==null){
            return false;
        }else{
            if(value.getValue().equals(2) || value.getValue().equals(3)){
                return false;
            }
            return true;//必须是女的
        }
    }
}
