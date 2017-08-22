package testHibernate;

import BaseSpringTest.JUnitServiceBase;
import com.common.utils.dao.CommonHibernateDao;
import com.module.userinfo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * descrption: 测试commonHibernate
 * authohr: wangji
 * date: 2017-08-16 14:24
 */
//org.hibernate.HibernateException: Could not obtain transaction-synchronized Session for current thread
    //在CommonHibernateDao 上加个事务的注解就行了，一般我们Service层上进行处理，不会有这个异常
@Slf4j
public class testCommonHibernate extends JUnitServiceBase {

    @Autowired
    public CommonHibernateDao commonDao;
//    Create TABLE `user` (
//            `id` int(11) NOT NULL AUTO_INCREMENT,
//  `userName` varchar(50) ,
//  `userAge` int(11) ,
//  `userAddress` varchar(200),
//    PRIMARY KEY (`id`)
//) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

    @Test
    public void Test1(){
        String sql = "select u.userName as name,u.userAge as age,u.userAddress as address,u.id from user u";
        List<User> list =commonDao.findListBySQL(sql,User.class);
        for(User user:list){
            log.info(user.toString());
        }

    }
    @Test
    public void Test2(){
        String sql = "select u.userName as name,u.userAge as age,u.userAddress as address,u.id from user u";
        List<Map<String,Object>> list =commonDao.findListMapBySQL(sql);
        for(Map<String,Object> map:list){
            for(Map.Entry entry:map.entrySet()){
                log.info("key:"+entry.getKey()+"\t value:"+entry.getValue());
            }
        }
    }
    @Test
    public void test3(){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        detachedCriteria.addOrder(Order.asc("name"));
        detachedCriteria.add(Restrictions.like("name","%an%"));
        List<User> users = commonDao.findALlByDetachedCriteria(detachedCriteria);
        for(User user:users){
            log.info(user.toString());
        }

    }

    @Test
    public  void Tets4(){
        final String sql = "select u.userName as name,u.userAge as age,u.userAddress as address,u.id from user u";
        @SuppressWarnings({"unchecked"})
        List<User> users = commonDao.findListBySQL(User.class,new CommonHibernateDao.AddScala() {
            public void addScalaToBean(SQLQuery query) {
                query.addScalar("name", StandardBasicTypes.STRING);
                query.addScalar("age",StandardBasicTypes.INTEGER);
                query.addScalar("address",StandardBasicTypes.STRING);
                query.addScalar("id",StandardBasicTypes.INTEGER);
            }
        },sql,null);

        for(User user:users){
            log.info(user.toString());
        }
    }

    @Test
    public void test5(){
        User user = commonDao.findById(User.class,24);
        log.info(user.toString());
    }

    @Test
    public void test6(){
        commonDao.deleteById(24,User.class);
    }
    @Test
    public void test7(){
        User user = commonDao.findUniqueBy(User.class,"id",32);
        log.info(user.toString());
    }
}
