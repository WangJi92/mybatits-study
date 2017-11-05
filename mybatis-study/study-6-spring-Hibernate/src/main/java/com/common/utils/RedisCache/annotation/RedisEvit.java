package com.common.utils.RedisCache.annotation;

import java.lang.annotation.*;

/**
 * 清除过期缓存注解，放置于update delete insert
 * Created by JetWang on 2017/11/4.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisEvit {
    Class type();
}
