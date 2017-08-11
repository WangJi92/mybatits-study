package com.jet.module.UserManage.service;

import com.jet.module.UserManage.entity.User;

import java.util.List;
import java.util.Map;

/**
 * descrption:
 * authohr: wangji
 * date: 2017-07-31 18:13
 */
public interface IUserInfoService {
    User getUserInfoById(Integer id);

    List<User> findAll();

    void addUser(User user);

    //这里插入多个数据
    void addUsers(List<User> userList);

    void deleteById(Integer id);

    void deleteByIds(List<Integer> ids);

    void deleteByArrayIds(Integer []ids);

    void deleteByMapIds(Map<String,Object> map);
}
