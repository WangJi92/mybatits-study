package com.common.utils.Hibernatevalidatedemo.spring;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wangji on 2017/8/12.
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = customeValidate.class)//处理的类
public @interface GenderAnnotation {

    String message() default "必须是女人";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Gender value() default Gender.Neutrality;
}
