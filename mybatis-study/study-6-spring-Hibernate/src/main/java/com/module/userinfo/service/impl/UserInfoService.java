package com.module.userinfo.service.impl;

import com.common.utils.Hibernatevalidatedemo.aop.doAction;
import com.module.userinfo.dao.UserDao;
import com.module.userinfo.entity.User;
import com.module.userinfo.service.IUserInfoService;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * descrption:简单的实现服务类
 * authohr: wangji
 * date: 2017-07-31 18:00
 */
@Service
@doAction()
public class UserInfoService implements IUserInfoService {

    @Resource
    private UserDao userDao;


    public User getUserInfoById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    @doAction(doSome = "test")
    public void doS(User u) {
        throw new RuntimeException("xxx");
    }
}
