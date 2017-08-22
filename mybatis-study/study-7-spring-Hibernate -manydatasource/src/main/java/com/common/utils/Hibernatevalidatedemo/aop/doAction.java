package com.common.utils.Hibernatevalidatedemo.aop;

import java.lang.annotation.*;

/**
 * Created by JetWang on 2017/8/13.
 *
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface doAction {
    String doSome() default "";
}
