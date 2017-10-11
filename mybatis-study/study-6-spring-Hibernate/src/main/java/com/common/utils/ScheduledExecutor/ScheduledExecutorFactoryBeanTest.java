package com.common.utils.ScheduledExecutor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * descrption: ScheduledExecutorFactoryBean 简单的使用
 * authohr: wangji
 * date: 2017-10-10 18:43
 */
public class ScheduledExecutorFactoryBeanTest {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-task.xml");
    }
}
