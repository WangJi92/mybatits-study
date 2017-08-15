##Hibernate Validator 方法界别验证，通过AOP实现
1. spring已经支持方法界别的参数验证了，我们只需要配置Bean就好了,然后在需要验证的类上面加上@Validated就可以了
```
      <!--  bean级别的校验 方法中的参数bean必须添加@Valid注解，后面紧跟着BindingResult result参数-->
    <bean id="global-validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean"></bean>
      <!--  方法级别的校验  要校验的方法所在类必须添加@Validated注解-->
    <bean class="org.springframework.validation.beanvalidation.MethodValidationPostProcessor"></bean>
     <!--可以引用自己的 validator 配置，在本文中（下面）可以找到 validator 的参考配置，如果不指定则系统使用默认的 -->
```
虽然这个很简单，不过笔者在测试的时候总是报错 Unable to initialize 'javax.el.ExpressionFactory'. Check that you have the EL dependencies on the classpath, or use ParameterMessageInterpolator instead,然后无法注入这两个Bean
```
 <dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>5.4.0.Final</version>
</dependency>
<dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>javax.el</artifactId>
    <version>2.2.6</version>
</dependency>
```
上面就是对于Hibernate Validator的依赖的处理，不过还是不行<https://stackoverflow.com/questions/42718869/hibernate-validation-unable-to-initialize-javax-el-expressionfactory-error/>
上面这里最后的有说明，我引用了两个不同版本的 You have 2 different incompatible versions of javax.el,我自己也是查看了Maven的依赖了啊，可是找不到啊！
看到了这句话 Have you removed all the other javax.el related dependencies? If you have several conflicting versions of javax.el, you're going to have this error. 
Thank You! It works if jsp-api removed.
原来我自己依赖了一个 ，移除了单元测试就行了，创建Bean也不会报错。
```
<dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>jsp-api</artifactId>
    <version>2.1</version>
    <scope>provided</scope>
</dependency>
```


2. 那么这个时候你就有点怪异了，既然之前我们使用了简单的Hibernate API进行处理，方法的参数，方法的返回值，或者Bean的验证，不可以直接的使用？那是当然的，当然可以直接的使用，在spring中你可以直接的注入的,在这里你可以选择Spring的API或者选择Hibernate的API,这样之前学习的东西都可以在局部进行使用啦！特别是想验证Bean信息的时候，不想移动到全局的异常处理的情况，这种局部的处理效果就感觉非常的舒服。
```
    @Autowired
    private Validator validator;
    //这里使用Hibernate的API
    Set<ConstraintViolation<ValidateInfoBean>> set = validator.validate(验证的对象);
    //也可以使用SPring的API,不过不太好使用
    
```
3. 一般情况下，我们对于Service层验证，可能使用面向接口的编程，那么这种@NotNull放置的位置应该怎么放置呢？
    *   接口中放置 void save(@NotNull(message = "不能为空") User user); 方法中不放public void save(User user)  通过
    *   接口和方法中都放置 ok
    *   接口中没有放置，实现中放置,就会出现错误，这个在使用使用的@Validated中也会出现问题 HV000151: A method overriding another method must not alter the parameter constraint configuration
    
4.  我是这样想的，就和spring一样的实现，如果某个方法上有我们定义的注解，或者某个类上有我们的注解就全部的处理方法参数的校验问题
    *   定义注解
     ```
        @Target({ElementType.METHOD,ElementType.TYPE})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        public @interface TestValidateParam {
        }
     ```
     *  通过AOP进行在方法执行之前对于参数校验，返回值进行参数校验,如果有异常掏出去
     ```java
     package com.common.utils.Hibernatevalidatedemo.ValidateParameter;
     
     import org.springframework.validation.FieldError;
     
     import java.util.List;
     
     /**
      * descrption: 参数异常，传递给上层应用进行处理
      * authohr: wangji
      * date: 2017-08-14 16:44
      */
     public class ParamValidException  extends RuntimeException{
     
         private List<FieldError> fieldErrors;
         public ParamValidException(List<FieldError> errors) {
             this.fieldErrors = errors;
         }
         public List<FieldError> getFieldErrors() {
             return fieldErrors;
         }
     
         public void setFieldErrors(List<FieldError> fieldErrors) {
             this.fieldErrors = fieldErrors;
         }
     }


     ```
     这里是处理AOP的逻辑 <aop:aspectj-autoproxy proxy-target-class="true" /> 开启AOP，下面就是使用Hibernate的原始的方法进行参数的校验。
     但是有个问题ConstraintViolation中无法获取到方法中参数的参数的名称，返回给上层应用处理不方便，spring给我们是想了一个这个
     LocalVariableTableParameterNameDiscoverer 非常方便的获取，然后返回给上层应用的时候就非常清楚的晓得是哪个字段出了问题。
     在AOP中可以非常方便的获取调用的方法，调用方法的实例，调用方法的参数等等信息
     ```java
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

     ```
5. 使用调用下面的方法，然后出现异常之后全局进行捕获异常，做你想做的处理就好了。
```
    @TestValidateParam
    public void doTest(@NotBlank(message = "不能为空")String message,String tt) {

    }
```

6.  全局异常的处理，包含了默认的spring @Validated 异常的处理
```java
package com.common.utils;

import com.common.utils.Hibernatevalidatedemo.ValidateParameter.ParamValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * descrption: 全局异常处理
 * authohr: wangji
 * date: 2017-08-01 8:54
 */
@Slf4j
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {

    private MappingJackson2JsonView  jsonView = new MappingJackson2JsonView();
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(jsonView);
        modelAndView.addObject("error",e.getMessage());
        if(e instanceof ConstraintViolationException){
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) e).getConstraintViolations();
            for(ConstraintViolation item : constraintViolations){
                log.info("Item:"+item.getPropertyPath().toString()+"  message:"+item.getMessage());
            }
        }else if(e instanceof ParamValidException){
           List<FieldError> fieldErrorList =  ((ParamValidException) e).getFieldErrors();
           for(FieldError fieldError:fieldErrorList){
               log.info("参数:"+fieldError.getField()+"\t错误原因:"+fieldError.getDefaultMessage());
           }
        }
        modelAndView.addObject("result",false);
        return modelAndView;
    }
}

```

7. 看看不如实践，自己试试才知道怎么回事，来吧，小伙子！
<https://github.com/WangJi92/mybatits-study/tree/master/mybatis-study/study-6-spring-Hibernate/src/main/java/com/common/utils/Hibernatevalidatedemo/ValidateParameter/>
                                                                    

