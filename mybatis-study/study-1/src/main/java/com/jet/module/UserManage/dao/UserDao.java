package com.jet.module.UserManage.dao;

import com.jet.module.UserManage.entity.User;

/**
 * Created by JetWang on 2017/7/19.
 */
public interface UserDao {
    User findById(Integer id);
}
