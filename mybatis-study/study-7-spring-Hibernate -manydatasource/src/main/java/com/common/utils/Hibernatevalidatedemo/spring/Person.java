package com.common.utils.Hibernatevalidatedemo.spring;

/**
 * descrption:
 * authohr: wangji
 * date: 2017-08-12 15:50
 */
public class Person {
    @GenderAnnotation(message = "必须是女人")
    private Gender gender;

    private Integer age;

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
