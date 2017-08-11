package com.module.userinfo.service.impl;

import com.module.userinfo.dao.UserDao;
import com.module.userinfo.entity.User;
import com.module.userinfo.service.IUserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * descrption:简单的实现服务类
 * authohr: wangji
 * date: 2017-07-31 18:00
 */
@Service
public class UserInfoService implements IUserInfoService {

    @Resource
    private UserDao userDao;


    public User getUserInfoById(Integer id) {
        return userDao.findById(id);
    }


}
