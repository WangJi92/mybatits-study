package com.jet.module.UserManage.service.impl;

import BaseSpringTest.JUnitServiceBase;
import com.jet.module.UserManage.dao.UserDaoUseAnnotation;
import com.jet.module.UserManage.entity.User;
import com.jet.module.UserManage.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangji on 2017/7/31.
 */
@Slf4j
public class UserInfoServiceTest extends JUnitServiceBase {


    /* @Slf4j这个注解是使用lombok ==  private static final Logger log = LoggerFactory.getLogger(UserInfoServiceTest.class);*/
    @Resource
    private IUserInfoService iUserInfoService;

    @Test
    public void getUserInfoById() throws Exception {
        User user = iUserInfoService.getUserInfoById(1);
        log.info(user.toString());
    }


    @Test
    public void findAll() throws Exception {
        log.info(iUserInfoService.findAll().toString());
    }

    @Test
    public void addUser() throws Exception {
        User user = new User();
        user.setName("wangwang");
        user.setAddress("zunyi");
        user.setAge(5);
        iUserInfoService.addUser(user);
    }

    @Test
    public void addUsers() throws Exception {
        List<User> userList = new ArrayList<User>(5);
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setAge(i);
            user.setAddress("zunyi" + i);
            user.setName("wangji" + i);
            userList.add(user);
        }
        iUserInfoService.addUsers(userList);
    }

    @Test
    public void deleteById() throws Exception {
        iUserInfoService.deleteById(3);
    }

    @Test
    public void deleteByIds() throws Exception {
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(10);
        ids.add(11);
        iUserInfoService.deleteByIds(ids);
    }

    @Test
    public void deleteByArrayIds() throws Exception {
        Integer[] ids = {8, 9};
        iUserInfoService.deleteByArrayIds(ids);
    }

    @Test
    public void deleteByMapIds() throws Exception {
        Integer[] ids = {8, 9};
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        iUserInfoService.deleteByMapIds(map);
    }
}