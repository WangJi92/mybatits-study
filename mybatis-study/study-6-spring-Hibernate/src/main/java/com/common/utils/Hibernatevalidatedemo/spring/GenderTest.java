package com.common.utils.Hibernatevalidatedemo.spring;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * descrption: 自定义鉴别器测试
 * authohr: wangji
 * date: 2017-08-12 15:48
 */
@Slf4j
public class GenderTest {
    public static void main(String[] args) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
       Person person = new Person();
       person.setGender(Gender.Man);
       pringValidateStr(validator.validate(person));
    }
    public static void pringValidateStr(Set<ConstraintViolation<Person>> set2) {
        for (ConstraintViolation<Person> constraintViolation : set2){
            log.info("错误：" + constraintViolation.getMessage());
            log.info("字段："+constraintViolation.getPropertyPath().toString());

        }
    }
}
