##Hibernate Validator 校验方法的参数，返回值，构造函数
1.  之前我们使用hibernate-Validator的时候基本是对于某个JavaBean进行的校验，比如下面的这个例子
```java
public class ValidateInfoBean {

    @NotNull(message = "姓名不能为空!")
    @Min(value = 1, message = "Id只能大于等于1，小于等于10")
    @Max(value = 10, message = "Id只能大于等于1，小于等于10")
    private Integer id;

    @NotNull(message = "姓名不能为空!")
    @Size(min = 2, max = 4, message = "姓名长度必须在{min}和{max}之间")
    @Pattern(regexp = "[\u4e00-\u9fa5]+", message = "名称只能输入是中文字符")
    private String userName;

    @NotNull(message = "密码不能为空!")
    @Size(min = 6, max = 12, message = "密码长度必须在{min}和{max}之间")
    private String passWord;

    @NotNull(message = "日期不能为空!")
    @Past(message = "你只能输入过去的日期")
    private Date birthday;

    @NotNull(message = "邮件不能为空!")
    @Email(message = "邮件格式不正确")
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```
下面对于当前的这个JavaBean的实例进行校验
```java
package com.common.utils.Hibernatevalidatedemo;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

/**
 * descrption: 使用Hibernate的实现类，进行校验
 * authohr: wangji
 * date: 2017-08-12 10:35
 *  http://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#preface
 *  http://jinnianshilongnian.iteye.com/blog/1990081
 */
@Slf4j
public class ValidateTest {
    public static void main(String[] args) {
        ValidateInfoBean infoBean = new ValidateInfoBean();
        infoBean.setId(-1);
        infoBean.setUserName("中国wj");
        infoBean.setPassWord("12345");
        infoBean.setEmail("testt.com");
        infoBean.setBirthday(new Date(2015,3,2));
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ValidateInfoBean>> set = validator.validate(infoBean);
        pringValidateStr(set);
    }
    public static void pringValidateStr(Set<ConstraintViolation<ValidateInfoBean>> set2) {
        for (ConstraintViolation<ValidateInfoBean> constraintViolation : set2){
            log.info("错误：" + constraintViolation.getMessage());
            log.info("字段："+constraintViolation.getPropertyPath().toString());

        }
    }
}
```

2. 但是很多时候啊，我都是相对于一些简单的参数进行校验，直接的在方法的参数中，或者是方法的返回值，不会用到那么大的JavaBean
```
 @NotNull(message = "返回值不能为空") 
 public User getUserInfoById(@NotNull(message = "不能为空") Integer id，String message);
```
* 碰到上面的情况怎么处理呢？还好Hibernate Validator对于这种东西是非常的支持的，还有很多高级的特性，感觉简单的校验不用那么复杂，能够解决简单的问题就行了，好下面来看看如何处理这样的业务的逻辑的信息
Hibernate Validator中已经实现了下面的这个接口了，看到这些参数就非常明了的知道该如何的去解决问题了。
```java

package javax.validation.executable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.groups.Default;

/**
 * Validates parameters and return values of methods and constructors.
 * Implementations of this interface must be thread-safe.
 *
 * @author Gunnar Morling
 * @since 1.1
 */
public interface ExecutableValidator {

	//校验参数
	<T> Set<ConstraintViolation<T>> validateParameters(T object,
													   Method method,
													   Object[] parameterValues,
													   Class<?>... groups);
	//校验返回值
	<T> Set<ConstraintViolation<T>> validateReturnValue(T object,
														Method method,
														Object returnValue,
														Class<?>... groups);

    //校验构造函数
	<T> Set<ConstraintViolation<T>> validateConstructorParameters(Constructor<? extends T> constructor,
																  Object[] parameterValues,
																  Class<?>... groups);

	//校验构造函数的返回值												  
	<T> Set<ConstraintViolation<T>> validateConstructorReturnValue(Constructor<? extends T> constructor,
																   T createdObject,
																   Class<?>... groups);
}

```
*   看看我们需要校验的类
```java
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

    public ValidateParamService() {
    }
}

```
*   校验参数
```
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
//        2017-08-15 15:08:09,138  INFO [Version.java:30] : HV000001: Hibernate Validator 5.4.0.Final
//        2017-08-15 15:08:09,408  INFO [ValidateParamService.java:64] : 不能为null
//        2017-08-15 15:08:09,409  INFO [ValidateParamService.java:64] : 需要在0和5之间
```

*   校验方法的返回值
```
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
//        2017-08-15 15:10:01,182  INFO [Version.java:30] : HV000001: Hibernate Validator 5.4.0.Final
//        2017-08-15 15:10:01,404  INFO [ValidateParamService.java:93] : 不能为空的字符串
```
*   校验构造函数
```
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
//2017-08-15 15:13:42,696  INFO [Version.java:30] : HV000001: Hibernate Validator 5.4.0.Final
//2017-08-15 15:13:42,932  INFO [ValidateParamService.java:111] : 不能为空
```

上面的使用是不是非常的简单，不过相同对与普通的一句话的事情，相对来说还是比较的麻烦，不过这个只是对于局部来说，如果全局都是统一的使用统一的处理，那么就省下来来了好多的时间去做这种和业务无关的事情，更好的全局的去处理，其实我们是可以做到这个的，下一次再说吧，如何做到这个问题。
github 网址：<https://github.com/WangJi92/mybatits-study/tree/master/mybatis-study/study-6-spring-Hibernate/src/main/java/com/common/utils/Hibernatevalidatedemo/ValidateParameter/>