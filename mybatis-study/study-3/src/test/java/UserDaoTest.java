import com.common.utils.MybatisSessionFactory;
import com.jet.module.UserManage.dao.UserDao;
import com.jet.module.UserManage.entity.Article;
import com.jet.module.UserManage.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JetWang on 2017/7/19.
 */
public class UserDaoTest {
    //添加了日志
    private static final Logger log = LoggerFactory.getLogger(UserDaoTest.class);

    @Test
    public  void testGetUserArticles(){
        SqlSession sqlSession = MybatisSessionFactory.getSQLSesssion();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        List<Article> articleList = userDao.getUserArticles(1);
        log.info(articleList.toString());
    }


    @Test
    public  void testDeleteByMapIds(){
        SqlSession sqlSession = MybatisSessionFactory.getSQLSesssion();
        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            Integer[] ids = {8,9};
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("ids",ids);
            userDao.deleteByMapIds(map);
            //这里需要提交事务才能写入数据库
            sqlSession.commit();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            MybatisSessionFactory.closeSession(sqlSession);
        }
    }
    @Test
    public  void testDeleteByIdsArray(){
        SqlSession sqlSession = MybatisSessionFactory.getSQLSesssion();
        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            Integer[] ids = {8,9};
            userDao.deleteByArrayIds(ids);
            //这里需要提交事务才能写入数据库
            sqlSession.commit();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            MybatisSessionFactory.closeSession(sqlSession);
        }
    }
    @Test
    public  void testDeleteByIdsList(){
        SqlSession sqlSession = MybatisSessionFactory.getSQLSesssion();
        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            List<Integer> ids = new ArrayList<Integer>();
            ids.add(10);
            ids.add(11);
            userDao.deleteByIds(ids);
            //这里需要提交事务才能写入数据库
            sqlSession.commit();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            MybatisSessionFactory.closeSession(sqlSession);
        }
    }

    @Test
    public void testDeleteById(){
        SqlSession sqlSession = MybatisSessionFactory.getSQLSesssion();
        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            userDao.deleteById(6);
            //这里需要提交事务才能写入数据库
            sqlSession.commit();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            MybatisSessionFactory.closeSession(sqlSession);
        }
    }


    @Test
    public void testAddUsers(){
        List<User> userList = new ArrayList<User>(5);
        for(int i=0;i<5;i++){
            User user = new User();
            user.setAge(i);
            user.setAddress("zunyi"+i);
            user.setName("wangji"+i);
            userList.add(user);
        }
        SqlSession sqlSession = MybatisSessionFactory.getSQLSesssion();
        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            userDao.addUsers(userList);
            //这里需要提交事务才能写入数据库
            sqlSession.commit();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            MybatisSessionFactory.closeSession(sqlSession);
        }
        log.info(userList.toString());
    }

    @Test
    public void testAddUser(){
        User user = new User();
        user.setName("wangwang");
        user.setAddress("zunyi");
        user.setAge(5);
        SqlSession sqlSession = MybatisSessionFactory.getSQLSesssion();
        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            userDao.addUser(user);
            //这里需要提交事务才能写入数据库
            sqlSession.commit();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            MybatisSessionFactory.closeSession(sqlSession);
        }
        log.info(user.toString());
    }


    @Test
    public void testFindAll(){
        SqlSession sqlSession = MybatisSessionFactory.getSQLSesssion();
        try {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            log.info(userDao.findAll().toString());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            MybatisSessionFactory.closeSession(sqlSession);
        }
    }



    @Test
    public void testSessionFactory(){
        Assert.assertNotNull(MybatisSessionFactory.getInstatnce());
    }
    @Test
    public void findByIdTest() {
        InputStream inputStream = null;
        SqlSessionFactory sqlSessionFactory = null;
        SqlSession sqlSession = null;

        try {
            inputStream = org.apache.ibatis.io.Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            sqlSession = sqlSessionFactory.openSession();
            //这里面向接口的，mybatis使用动态代理给你生成实现类，不需要DaoImpl
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            User user = userDao.findById(1);
          /*
          通过命名空间进行查询
          User user = sqlSession.selectOne("com.jet.module.UserManage.dao.UserDao.findById",1);
          */
            Assert.assertNotNull(user);
            System.out.println(user.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
