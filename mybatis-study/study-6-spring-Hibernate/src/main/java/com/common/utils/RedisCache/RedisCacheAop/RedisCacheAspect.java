package com.common.utils.RedisCache.RedisCacheAop;

import com.common.utils.RedisCache.annotation.RedisCache;
import com.common.utils.RedisCache.annotation.RedisEvit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by JetWang on 2017/11/4.
 */
@Component
@Aspect
@Slf4j
public class RedisCacheAspect {

    /**
     * 分隔符 生成key 格式为 类全类名|方法名|参数所属类全类名
     **/
    private static final String DELIMITER = "|";

    /**
     * spring-redis.xml配置连接池、连接工厂、Redis模板
     **/
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;


    @Pointcut("@annotation(com.common.utils.RedisCache.annotation.RedisCache)")
    public void redisCacheAspect() {
    }

    @Pointcut("@annotation(com.common.utils.RedisCache.annotation.RedisEvit)")
    public void redisCacheEvict() {
    }

    @Around("redisCacheAspect()")
    public Object cache(ProceedingJoinPoint joinPoint) {
        // 得到类名、方法名和参数
        String clazzName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        if(log.isDebugEnabled()){
            log.info("key参数: " + clazzName + "." + methodName);
        }
        String key = getKey(clazzName, methodName, args);
        if (log.isInfoEnabled()) {
            log.info("生成key: " + key);
        }

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Class modelType = method.getAnnotation(RedisCache.class).type();

        // 检查Redis中是否有缓存
        Object value = redisTemplate.opsForHash().get(modelType.getName(), key);

        // 得到被代理方法的返回值类型
        Class returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();

        // result是方法的最终返回结果
        Object result = null;
        try {
            if (null == value) {
                if (log.isInfoEnabled()) {
                    log.info("缓存未命中");
                }
                // 调用数据库查询方法
                result = joinPoint.proceed(args);
                redisTemplate.opsForHash().put(modelType.getName(), key, result);

            } else {

                // 缓存命中
                if (log.isInfoEnabled()) {
                    log.info("缓存命中, value = " + value);
                }
                result = value;
                if (log.isInfoEnabled()) {
                    log.info(result.toString());
                }
            }
        } catch (Throwable e) {
            log.error("解析异常", e);
        }
        return result;
    }

    /**
     *      * 在方法调用前清除缓存，然后调用业务方法
     *      * @param joinPoint
     *      * @return
     *      * @throws Throwable
     *
     */
    @Around("redisCacheEvict()")
    public Object evictCache(ProceedingJoinPoint joinPoint) throws Throwable {
        String clazzName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String key =getKey(clazzName,methodName,args);
        // 得到被代理的方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 得到被代理的方法上的注解
        Class modelType = method.getAnnotation(RedisEvit.class).type();
        if (log.isInfoEnabled()) {
            log.info("清空缓存 = " + modelType.getName());
        }
        // 清除对应缓存
        redisTemplate.opsForHash().delete(modelType.getName(),key);
        return joinPoint.proceed(joinPoint.getArgs());
    }


    /**
     * * 根据类名、方法名和参数生成Key
     * * @param clazzName
     * * @param methodName
     * * @param args
     * * @return key格式：全类名｜方法名｜参数类型
     */
    private String getKey(String clazzName, String methodName, Object[] args) {
        StringBuilder key = new StringBuilder(clazzName);
        key.append(DELIMITER);
        key.append(methodName);
        key.append(DELIMITER);

        for (Object obj : args) {
            key.append(obj.getClass().getSimpleName());
            key.append(DELIMITER);
        }

        return key.toString();
    }


}
