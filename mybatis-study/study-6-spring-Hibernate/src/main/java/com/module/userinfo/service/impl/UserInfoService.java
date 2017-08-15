package com.module.userinfo.service.impl;

import com.common.utils.Hibernatevalidatedemo.ValidateParameter.TestValidateParam;
import com.module.userinfo.dao.UserDao;
import com.module.userinfo.entity.User;
import com.module.userinfo.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * descrption:简单的实现服务类
 * authohr: wangji
 * date: 2017-07-31 18:00
 */
@Service
@Slf4j
public class UserInfoService implements IUserInfoService {


    @Resource
    private UserDao userDao;

    @TestValidateParam
    public User getUserInfoById(Integer id) {
        return userDao.findById(id);
    }

    public void save(User user) {
        userDao.save(user);
    }

}
