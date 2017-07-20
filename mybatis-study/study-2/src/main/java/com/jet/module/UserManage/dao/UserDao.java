package com.jet.module.UserManage.dao;

import com.jet.module.UserManage.entity.User;

import java.util.List;
import java.util.Map;

/**
 * Created by JetWang on 2017/7/19.
 */
public interface UserDao {

    User findById(Integer id);

    List<User> findAll();
    //这里只是插入一个数据
    void addUser(User user);

    //这里插入多个数据
    void addUsers(List<User> userList);

    void deleteById(Integer id);

    void deleteByIds(List<Integer> ids);

    void deleteByArrayIds(Integer []ids);

    void deleteByMapIds(Map<String,Object> map);

}
