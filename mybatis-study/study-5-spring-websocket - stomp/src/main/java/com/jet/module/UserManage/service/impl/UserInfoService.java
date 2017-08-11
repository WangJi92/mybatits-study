package com.jet.module.UserManage.service.impl;

import com.jet.module.UserManage.dao.UserDao;
import com.jet.module.UserManage.entity.User;
import com.jet.module.UserManage.service.IUserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    public List<User> findAll() {
        return userDao.findAll();
    }

    public void addUser(User user) {
        if (user != null) {
            userDao.addUser(user);
        }
    }

    public void addUsers(List<User> userList) {
        if (userList != null && userList.size() > 0) {
            userDao.addUsers(userList);
        }
    }

    public void deleteById(Integer id) {
        userDao.deleteById(id);
    }

    public void deleteByIds(List<Integer> ids) {
        if (ids != null && ids.size() > 0) {
            userDao.deleteByIds(ids);
        }
    }

    public void deleteByArrayIds(Integer[] ids) {
        if (ids != null && ids.length > 0) {
            userDao.deleteByArrayIds(ids);
        }
    }

    public void deleteByMapIds(Map<String, Object> map) {
        if (map != null && map.size() > 0 && map.get("ids") != null) {
            userDao.deleteByMapIds(map);
        }
    }
}
