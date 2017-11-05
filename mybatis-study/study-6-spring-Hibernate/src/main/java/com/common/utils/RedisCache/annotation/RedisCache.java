package com.common.utils.RedisCache.annotation;

import java.lang.annotation.*;

/**
 * 缓存注解
 * Created by JetWang on 2017/11/4.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisCache {
    Class type();//被代理类的全类名，在之后会做为redis hash 的key
}
