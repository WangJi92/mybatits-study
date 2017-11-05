package com.module.userinfo.service.impl;

import com.common.utils.Hibernatevalidatedemo.ValidateParameter.TestValidateParam;
import com.common.utils.RedisCache.annotation.RedisCache;
import com.common.utils.RedisCache.annotation.RedisEvit;
import com.common.utils.dao.CommonHibernateDao;
import com.module.userinfo.dao.UserDao;
import com.module.userinfo.entity.User;
import com.module.userinfo.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


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
    @Autowired
    private CommonHibernateDao commonDao;

    @TestValidateParam
    @RedisCache(type = User.class)
    public User getUserInfoById(Integer id) {
        return userDao.findById(id);
    }

    public void save(User user) {
        userDao.save(user);
    }

    @RedisCache(type = User.class)
    public List<User> findAll(){
        String sql = "select u.userName as name,u.userAge as age,u.userAddress as address,u.id from user u";
        List<User> list =commonDao.findListBySQL(sql,User.class);
        return list;
    }

}
