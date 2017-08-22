package com.common.utils.manydatasource;

import java.lang.annotation.*;

/**
 * @description： 创建拦截设置数据源的注解
 * Created by wangji on 2017/8/21.
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicSwitchDataSource {

    String dataSource() default "";
}
