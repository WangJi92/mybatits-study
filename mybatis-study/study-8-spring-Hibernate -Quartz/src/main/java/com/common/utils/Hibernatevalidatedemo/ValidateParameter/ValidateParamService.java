package com.common.utils.Hibernatevalidatedemo.ValidateParameter;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * descrption: 测试validate参数，构造参数，返回值等等，使用Hibernate Validate进行测试
 * authohr: wangji
 * date: 2017-08-14 15:34
 */
@Slf4j
public class ValidateParamService {
  //参考文档 https://diamondfsd.com/article/78fa12cd-b530-4a90-b438-13d5a0c4e26c/

    //校验参数
    public void update(
            @NotNull String userId,
            @NotNull @Range(min = 0, max = 5) Integer status){
        log.info("通过校验");
    }
    //校验构造函数
    public ValidateParamService(@NotNull(message = "不能为空")String message) {
        log.info("Constructor通过校验");
    }
    //校验返回值
    public @NotBlank(message = "不能为空的字符串")String ValidateReturn(){
        return "";
    }


    public static void main(String[] args)throws NoSuchMethodException {
        testValidateReturnValue();
    }
    //校验方法的参数
    public  static void testValidateParam()throws NoSuchMethodException{
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //1.获取校验器
        Validator validator = factory.getValidator();
        //2.获取校验方法参数的校验器
        ExecutableValidator validatorParam = validator.forExecutables();
        //创建一个要校验参数的实例
        ValidateParamService validateParamService = new ValidateParamService();
        //获取要校验的方法
        Method method = validateParamService.getClass().getMethod("update",String.class,Integer.class);
        //传递校验参数的输入的参数
        Object[] paramObjects = new Object[]{null,7};
        //开始校验
        Set<ConstraintViolation<ValidateParamService>>
                constraintViolationSet = validatorParam.
                validateParameters(validateParamService,method,paramObjects);
        for(ConstraintViolation item:constraintViolationSet){
            log.info(item.getMessage());
        }
    }

    //校验返回值
    public  static void testValidateReturnValue()throws NoSuchMethodException{
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //1.获取校验器
        Validator validator = factory.getValidator();
        //2.获取校验方法参数的校验器
        ExecutableValidator validatorParam = validator.forExecutables();

        //3.创建一个要校验参数的实例
        ValidateParamService validateParamService = new ValidateParamService();
        //4.获取要校验的方法
        Method method = validateParamService.getClass().getMethod("ValidateReturn");
        Object returnValue =null;
        try {
            returnValue = method.invoke(validateParamService);//调用方法获取返回值
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //5.校验返回值
        Set<ConstraintViolation<ValidateParamService>>
                constraintViolationSet =validatorParam.validateReturnValue(validateParamService,method,returnValue);
        for(ConstraintViolation item:constraintViolationSet){
            log.info(item.getMessage());
        }

    }

    //校验构造函数
    public  static void testValidateConstructor()throws NoSuchMethodException{
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //1.获取校验器
        Validator validator = factory.getValidator();
        //2.获取校验方法参数的校验器
        ExecutableValidator validatorParam = validator.forExecutables();
        //3.获取构造函数
        Constructor<ValidateParamService> constructor = ValidateParamService.class.getConstructor(String.class);
        Object[] constructorsParams = new Object[]{null};
        Set<ConstraintViolation<ValidateParamService>>  constraintViolationSet  =validatorParam.validateConstructorParameters(constructor,constructorsParams);
        for(ConstraintViolation item:constraintViolationSet){
            log.info(item.getMessage());
        }
    }

    public ValidateParamService() {
    }
}
