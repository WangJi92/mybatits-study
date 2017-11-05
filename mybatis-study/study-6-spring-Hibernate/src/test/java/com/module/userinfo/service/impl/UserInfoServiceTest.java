package com.module.userinfo.service.impl;

import BaseSpringTest.JUnitServiceBase;
import com.module.userinfo.entity.User;
import com.module.userinfo.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by wangji on 2017/8/11.
 */
@Slf4j
public class UserInfoServiceTest extends JUnitServiceBase{

    @Autowired
    public IUserInfoService iUserInfoService;
    @Test
    public void getUserInfoById() throws Exception {
        User user = iUserInfoService.getUserInfoById(1);
        log.info(user!=null?user.toString():"null");
        User user1 = iUserInfoService.getUserInfoById(1);
        log.info(user1!=null?user.toString():"null");
    }
    @Test
    public void Test1(){
        List<User> users = iUserInfoService.findAll();
        for(User user:users){
            log.info(user.toString());
        }

        List<User> users1 = iUserInfoService.findAll();
        for(User user:users1){
            log.info(user.toString());
        }


    }

}