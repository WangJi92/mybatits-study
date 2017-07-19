import com.jet.module.UserManage.dao.UserDao;
import com.jet.module.UserManage.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by JetWang on 2017/7/19.
 */
public class UserDaoTest {

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
