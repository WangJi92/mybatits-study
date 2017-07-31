package com.common.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * descrption: 获取Sqlsession
 * authohr: wangji
 * date: 2017-07-20 8:34
 */
public class MybatisSessionFactory{
    private static final Logger log = LoggerFactory.getLogger(MybatisSessionFactory.class);

    private static  volatile SqlSessionFactory  sqlSessionFactory;

    public   static SqlSessionFactory getInstatnce(){
        if(sqlSessionFactory == null){
            synchronized (MybatisSessionFactory.class){
                if(sqlSessionFactory ==null){
                    try {
                        sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
                    } catch (IOException e) {
                        log.error("mybatits 启动异常");
                    }
                }
            }
        }
        return sqlSessionFactory;
    };
    public static SqlSession getSQLSesssion(){
        if( getInstatnce() !=null){
            return sqlSessionFactory.openSession();
        }
        return null;
    }

    public static void closeSession(SqlSession sqlSession){
        if(sqlSession !=null){
            sqlSession.close();
        }
    }


}
