package com.common.utils.Hibernatevalidatedemo;

import lombok.extern.slf4j.Slf4j;

import javax.validation.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * descrption: 校验分组
 * authohr: wangji
 * date: 2017-08-12 14:04
 */
@GroupSequence({First.class,Second.class,ValidateGroup.class})
@Slf4j
public class ValidateGroup {

    @NotNull(message = "姓名不能为空!", groups = {First.class})
    @Size(min = 2, max = 4, message = "姓名长度必须在{min}和{max}之间",groups = {First.class})
    @Pattern(regexp = "[\u4e00-\u9fa5]+", message = "名称只能输入是中文字符",groups = {First.class})
    private String userName;

    @NotNull(message = "密码不能为空!",groups = {Second.class})
    @Size(min = 6, max = 12, message = "密码长度必须在{min}和{max}之间",groups = {Second.class})
    private String passWord;

    @NotNull(message = "日期不能为空!")
    @Past(message = "你只能输入过去的日期")
    private Date birthday;

    public static void main(String[] args) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        ValidateGroup testFirst = new ValidateGroup();
        testFirst.setUserName("wangji");
        //下面这里使用来分类进行校验，通过Group唯一标识进行区别，也可以使用多个进行先后顺序的校验
        //还可以进行类界别的校验顺序@GroupSequence，如果没有定义Group那么就会使用，当前类进行标识
       // pringValidateStr(validator.validate(testFirst,First.class));
//        2017-08-12 14:22:13,899  INFO [ValidateGroup.java:44] : 错误：姓名长度必须在2和4之间
//        2017-08-12 14:22:13,900  INFO [ValidateGroup.java:45] : 字段：userName
//        2017-08-12 14:22:13,900  INFO [ValidateGroup.java:44] : 错误：名称只能输入是中文字符
//        2017-08-12 14:22:13,900  INFO [ValidateGroup.java:45] : 字段：userName


//        通过@GroupSequence指定验证顺序：
//        先验证First分组，如果有错误立即返回
//        而不会验证Second分组，接着如果First分组验证通过了，
//        那么才去验证Second分组，最后指定User.class表示那些没有分组的在最后。
//        这样我们就可以实现按顺序验证分组了。
         pringValidateStr(validator.validate(testFirst));
//        输出的只是第一个校验错误，其他的不继续校验了
//        2017-08-12 14:27:46,378  INFO [Version.java:30] : HV000001: Hibernate Validator 5.4.0.Final
//        2017-08-12 14:27:46,649  INFO [ValidateGroup.java:55] : 错误：姓名长度必须在2和4之间
//        2017-08-12 14:27:46,650  INFO [ValidateGroup.java:56] : 字段：userName
//        2017-08-12 14:27:46,650  INFO [ValidateGroup.java:55] : 错误：名称只能输入是中文字符
//        2017-08-12 14:27:46,650  INFO [ValidateGroup.java:56] : 字段：userName






    }
    public static void pringValidateStr(Set<ConstraintViolation<ValidateGroup>> set2) {
        for (ConstraintViolation<ValidateGroup> constraintViolation : set2){
            log.info("错误：" + constraintViolation.getMessage());
            log.info("字段："+constraintViolation.getPropertyPath().toString());

        }
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
}
