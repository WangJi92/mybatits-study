package com.common.utils.Hibernatevalidatedemo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by JetWang on 2017/8/13.
 */
//@Aspect放在类头上，把这个类作为一个切面。
//@Pointcut放在方法头上，定义一个可被别的方法引用的切入点表达式。
//        5种通知。
//@Before，前置通知，放在方法头上。
//@After，后置【finally】通知，放在方法头上。
//@AfterReturning，后置【try】通知，放在方法头上，使用returning来引用方法返回值。
//@AfterThrowing，后置【catch】通知，放在方法头上，使用throwing来引用抛出的异常。
//@Around，环绕通知，放在方法头上，这个方法要决定真实的方法是否执行，而且必须有返回值。
//    http://www.cnblogs.com/duyinqiang/p/5696653.html aop拦截器使用
@Component
@Aspect
@Slf4j
public class DoActionAspect {

//@within()
//表示拦截含有这个注解的类中所有方法
//@annotation()
//表示拦截含有这个注解的方法
    @Pointcut("@within(com.common.utils.Hibernatevalidatedemo.aop.doAction)||@annotation(com.common.utils.Hibernatevalidatedemo.aop.doAction)")
    public void pointcut()
    {
        //定义需要拦截的注解
    }

    /**
     * 前置通知（Before advice） ：在某连接点（JoinPoint）之前执行的通知，但这个通知不能阻止连接点前的执行。
     * @param joinPoint
     */
    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint)//这里可以修改参数的值
    {
        log.info("=====DoActionAspect(doBefore)前置通知开始=====");
        Method method = getMethod(joinPoint);
        Annotation annotation = getAnnotation(joinPoint,doAction.class);
        log.info(method.toString());
        log.info(annotation!=null?annotation.toString():"annotation is null");

        Object[] objects = joinPoint.getArgs();
        Object targetClass = joinPoint.getTarget();
        log.info("targetClass:"+targetClass.getClass().getName());
    }
    @After("pointcut()")
    public void doAfterFinnaly(JoinPoint joinPoint){
        //相当于finnaly方法总会执行
        //如果有异常会在异常之前执行
        log.info("=== doAfterFinnaly ===");
    }
    /**
     * 后通知（After advice） ：当某连接点退出的时候执行的通知（不论是正常返回还是异常退出）。
     * @param joinPoint
     */
    @AfterReturning(pointcut = "pointcut()")
    public void doAfterReturning(JoinPoint joinPoint)//已经返回了之后处理的事情
    {
        log.info("=====DoActionAspect(doAfterReturning)后置通知开始=====");
    }

    /**
     * 抛出异常后通知（After throwing advice） ： 在方法抛出异常退出时执行的通知。
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e)
    {
        //这里仅仅的记录异常，不能对于异常的信息进行捕获，这里可以记录异常啦
        log.info("=====DoActionAspect(doAfterThrowing)异常通知开始=====");
    }

    /**
     * 环绕通知（Around advice） ：包围一个连接点的通知，类似Web中Servlet规范中的Filter的doFilter方法。可以在方法的调用前后完成自定义的行为，也可以选择不执行。
     * @param joinPoint
     */
    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable
    {
        log.info("=====DoActionAspect 环绕通知开始=====");
        Object obj= null;
        try {
            obj = joinPoint.proceed();
//            可以通过ProceedingJoinPoint.getArgs()获取方法调用参数，
//            对其进行修改，然后通过ProceedingJoinPoint.proceed(Object[] args)来传入修改过的参数继续调用。
        } catch (Exception e) {
           log.error("捕获异常的信息",e);
           throw e;
        }
        //如果有异常这里就不不执行了
        log.info("=====DoActionAspect 环绕通知结束=====");
        return  obj;
    }

    /**
     * 获取拦截的方法
     * @param joinPoint
     * @return
     */
    private Method getMethod(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return method;
    }

    /**
     * 根据获取的方法获取拦截的注解的值
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private Annotation getAnnotation(JoinPoint joinPoint,Class<? extends Annotation> annotationClass)
    {
        Method method =getMethod(joinPoint);
        if (method != null && annotationClass!=null)
        {
            Annotation annotation = method.getAnnotation(annotationClass);
            return  annotation;
        }
        return null;
    }

}
