package com.jet.module.UserManage.dao;

import com.jet.module.UserManage.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * descrption: 使用注解实现功能
 * authohr: wangji
 * date: 2017-08-07 10:47
 */
@Repository
public interface UserDaoUseAnnotation {

    /* @ResultMap(value = "listUser") ：重要的注解，
    可以解决复杂的映射关系，包括resultMap嵌套，
    鉴别器discriminator等等。注意一旦你启用该注解
    ，你将不得不在你的映射文件中配置你的resultMap，
    而value = "getByTestText"即为映射文件中的resultMap ID(注意此处的value = "listUser"，必须是在映射文件中指定命名空间路径)。
    @ResultMap在某些简单场合可以用@Results代替，但是复杂查询，比如联合、嵌套查询@ResultMap就会显得解耦方便更容易管理。*/
    //http://blog.csdn.net/ljhljh8888/article/details/8081585
    //http://www.blogjava.net/dbstar/archive/2011/08/08/355825.html
    @Select("select * from user where id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "age", column = "userAge", javaType = Integer.class),
            @Result(property = "name", column = "userName", javaType = String.class),
            @Result(property = "address", column = "userAddress", javaType = String.class)
    })
    @Options(useCache = true, flushCache = true, timeout = 10000)
    User findById(Integer id);


    //多参数问题，可以使用对象封装，或者使用注解
    @Update("update user set userAge=#{age} where id = #{id}")
    Integer updateUserAge(@Param("age") Integer age, @Param("id") Integer id);

 /* 使用注解来配置Mapper的新特性，本篇文章主要介绍其中几个@Provider的使用方式，
 他们是：@SelectProvider、@UpdateProvider、@InsertProvider和@DeleteProvider。*/

}
