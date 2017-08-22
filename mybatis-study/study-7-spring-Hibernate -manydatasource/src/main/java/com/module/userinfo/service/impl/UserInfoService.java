package com.module.userinfo.service.impl;

import com.common.utils.Hibernatevalidatedemo.ValidateParameter.TestValidateParam;
import com.common.utils.dao.CommonHibernateDao;
import com.common.utils.manydatasource.DynamicSwitchDataSource;
import com.common.utils.manydatasource.HandlerDataSource;
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
    public User getUserInfoById(Integer id) {
        return userDao.findById(id);
    }

    @DynamicSwitchDataSource(dataSource = "datasource1")
    public void save(User user) {
        userDao.save(user);
    }

    @DynamicSwitchDataSource(dataSource = "datasource1")
    public List<User> findAll() {
        String sql = "select u.userName as name,u.userAge as age,u.userAddress as address,u.id from user u";
        List<User> list = commonDao.findListBySQL(sql, User.class);
        return list;
    }

    public void test() {
        HandlerDataSource.putDataSource("datasource1");
        String sql = "select u.userName as name,u.userAge as age,u.userAddress as address,u.id from user u";
        List<User> list = commonDao.findListBySQL(sql, User.class);

        HandlerDataSource.putDataSource("datasource0");
        commonDao.deleteById(1, User.class);
    }

    public void  testInsert(){
        //无法保存事务的执行，只能插入默认的数据库的事务处理
        HandlerDataSource.putDataSource("datasource1");
        User user = new User();
        user.setAge(33);
        user.setAddress("datasour1");
        user.setName("wangji");
        commonDao.saveOrUpdate(user);//这个没有执行
        HandlerDataSource.putDataSource("datasource0");
        User user2 = new User();
        user2.setAge(33);
        user2.setAddress("datasour1");
        user2.setName("wangji");
        commonDao.saveOrUpdate(user);
    }


}
   