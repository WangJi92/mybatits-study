package com.common.utils.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;

/**
 * descrption: ThreadPoolTaskExecutor 简单的使用
 * authohr: wangji
 * date: 2017-10-11 9:39
 * http://www.cnblogs.com/lic309/p/4186880.html 这个博客讲的不错 关于threadPoolExecutor
 * http://blog.csdn.net/u011116672/article/details/52517247 深入浅出Spring task定时任务 scheduler.schedule(task, new CronTrigger("30 * * * * ?")); ThreadPoolTaskScheduler这个类似的
 * http://www.zuidaima.com/share/2913887981407232.htm  spring整合java quartz实现动态定时任务的前台网页配置与管理
 * http://blog.csdn.net/ukulelepku/article/details/54310035  schedule详解
 */
@Slf4j
public class ThreadPoolTaskExecutorTest {

    private TaskExecutor taskExecutor;

    public void doWork() {
        taskExecutor.execute(new Runnable() {
            public void run() {
                log.info("taskExecutor is run");
            }
        });
    }

    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-ThreadPoolTaskExecutor.xml");
        ThreadPoolTaskExecutorTest threadPoolTaskExecutorTest = (ThreadPoolTaskExecutorTest) applicationContext.getBean("threadPoolTaskExecutorTest");
        threadPoolTaskExecutorTest.doWork();
    }
}
