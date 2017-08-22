package com.common.utils.Hibernatevalidatedemo.ValidateParameter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * descrption: 使用AOP进行参数校验
 * authohr: wangji
 * date: 2017-08-14 16:20
 */
@Component
@Aspect
@Slf4j
public class ValidateParamUseAop {


    //获取方法的参数的名称，一般的API无法获取，需要通过解析字节码获取，通常ASM，spring也是使用这个进行获取。
    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Pointcut("@within(com.common.utils.Hibernatevalidatedemo.ValidateParameter.TestValidateParam)||@annotation(com.common.utils.Hibernatevalidatedemo.ValidateParameter.TestValidateParam)")
    public void pointcut() {}

    @Before("pointcut()")
    public void before(JoinPoint point) throws NoSuchMethodException, SecurityException, ParamValidException{
        //  获得切入目标对象
        Object target = point.getThis();
        // 获得切入方法参数
        Object [] args = point.getArgs();
        // 获得切入的方法
        Method method = ((MethodSignature)point.getSignature()).getMethod();

        // 执行校验，获得校验结果
        Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, args);

        ValidateConstraintViolationThrowExpection(validResult,method);
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable
    {
        Object returnValue = null;
        try {
            returnValue = joinPoint.proceed();
//            可以通过ProceedingJoinPoint.getArgs()获取方法调用参数，
//            对其进行修改，然后通过ProceedingJoinPoint.proceed(Object[] args)来传入修改过的参数继续调用。
        } catch (Exception e) {
            log.error("捕获异常的信息",e);
            throw e;
        }
        Object target = joinPoint.getThis();
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        Set<ConstraintViolation<Object>> validResult = validReturnValue(target,method,returnValue);
        ValidateConstraintViolationThrowExpection(validResult,method);
        //如果有异常这里就不不执行了
        return  returnValue;
    }


    public void ValidateConstraintViolationThrowExpection(Set<ConstraintViolation<Object>> validResult,Method method){
        if(!validResult.isEmpty()){
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            log.info(parameterNames.toString());
            List<FieldError> errors = new ArrayList<FieldError>();
            for(ConstraintViolation item :validResult){
                //获取参数路径的信息(参数的位置，参数的名称等等)
                PathImpl path = (PathImpl)item.getPropertyPath();
                int paramIndex = path.getLeafNode().getParameterIndex();
                String parameterName = parameterNames[paramIndex];
                FieldError fieldError = new FieldError("",parameterName,item.getMessage());
                errors.add(fieldError);
            }
            throw new ParamValidException(errors);
        }
    }

    //进行返回值的校验
    private <T> Set<ConstraintViolation<T>> validReturnValue(T obj, Method method, Object  returnValue){
        ExecutableValidator validatorParam = validator.forExecutables();
        return validatorParam.validateReturnValue(obj,method,returnValue);
    }

    //进行参数校验
    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object [] params){
        ExecutableValidator validatorParam = validator.forExecutables();
        return validatorParam.validateParameters(obj,method,params);
    }


}
