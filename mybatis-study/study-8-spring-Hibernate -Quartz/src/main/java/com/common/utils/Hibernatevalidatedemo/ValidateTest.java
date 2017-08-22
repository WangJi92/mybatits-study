package com.common.utils.Hibernatevalidatedemo;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

/**
 * descrption: 使用Hibernate的实现类，进行校验
 * authohr: wangji
 * date: 2017-08-12 10:35
 *  http://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#preface
 *  http://jinnianshilongnian.iteye.com/blog/1990081
 */
@Slf4j
public class ValidateTest {
    public static void main(String[] args) {
        ValidateInfoBean infoBean = new ValidateInfoBean();
        infoBean.setId(-1);
        infoBean.setUserName("中国wj");
        infoBean.setPassWord("12345");
        infoBean.setEmail("testt.com");
        infoBean.setBirthday(new Date(2015,3,2));

          /* 创建效验工厂 */
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

      /* validate，validateProperty，validateValue。
        其中validate会将所有的属性进行约束校验
        而validateProperty是针对某一个具体的属性进行校验，
        validateValue是对具体的某一个属性和特定的值进行校验*/


        //针对所有的进行校验
        Set<ConstraintViolation<ValidateInfoBean>> set = validator.validate(infoBean);
        //pringValidateStr(set);

        //针对某个类中的字段，特定的属性进行校验
        Set<ConstraintViolation<ValidateInfoBean>> set2 =validator.validateValue(ValidateInfoBean.class,"userName","中国4kk");
        pringValidateStr(set2);

        //针对某个实例的特定的属性进行校验
        Set<ConstraintViolation<ValidateInfoBean>> set3  =validator.validateProperty(infoBean,"userName");
        //pringValidateStr(set3);
    }

    public static void pringValidateStr(Set<ConstraintViolation<ValidateInfoBean>> set2) {
        for (ConstraintViolation<ValidateInfoBean> constraintViolation : set2){
            log.info("错误：" + constraintViolation.getMessage());
            log.info("字段："+constraintViolation.getPropertyPath().toString());

        }
    }
}
