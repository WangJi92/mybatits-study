package com.module.userinfo.dao;

import com.common.utils.dao.HibernateEntityDao;
import com.module.userinfo.entity.User;
import org.springframework.stereotype.Repository;

/**
 * descrption:
 * authohr: wangji
 * date: 2017-08-11 14:45
 */
@Repository
public class UserDao extends HibernateEntityDao<User>{


    public User findById(Integer id){
     return super.findById(1);
    }


}
