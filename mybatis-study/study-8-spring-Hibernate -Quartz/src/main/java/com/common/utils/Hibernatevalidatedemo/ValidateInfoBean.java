package com.common.utils.Hibernatevalidatedemo;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * descrption: 测试类，看看Validate
 * authohr: wangji
 * date: 2017-08-12 10:36
 */
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
/*
@Valid 被注释的元素是一个对象，需要检查此对象的所有字段值
@Null 被注释的元素必须为 null
@NotNull 被注释的元素必须不为 null
@AssertTrue 被注释的元素必须为 true
@AssertFalse 被注释的元素必须为 false
@Min(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@Max(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@DecimalMin(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@DecimalMax(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@Size(max, min) 被注释的元素的大小必须在指定的范围内
@Digits (integer, fraction) 被注释的元素必须是一个数字，其值必须在可接受的范围内
@Past 被注释的元素必须是一个过去的日期
@Future 被注释的元素必须是一个将来的日期
@Pattern(value) 被注释的元素必须符合指定的正则表达式*/
