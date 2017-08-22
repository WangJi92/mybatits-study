package com.module.userinfo.service.impl;

import BaseSpringTest.JUnitServiceBase;
import com.module.userinfo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/**
 * Created by wangji on 2017/8/11.
 */
@Slf4j
public class UserInfoServiceTest extends JUnitServiceBase{

    @Autowired
    public UserInfoService iUserInfoService;
    @Test
    public void getUserInfoById() throws Exception {
        User user = iUserInfoService.getUserInfoById(1);
        log.info(user!=null?user.toString():"null");
    }

    @Test
    @Rollback(false)
    public void TestSave(){
        User user = new User();
        user.setAge(33);
        user.setAddress("datasource1");
        user.setName("datasource1");
        iUserInfoService.save(user);
    }

    @Test
    public void Test1(){
        List<User> users = iUserInfoService.findAll();
        for(User user:users){
            log.info(user.toString());
        }
    }
    @Test
    public void Test2(){
         iUserInfoService.test();
    }

    @Test
    @Rollback(false)
    public void Test3(){
        iUserInfoService.testInsert();
    }

}