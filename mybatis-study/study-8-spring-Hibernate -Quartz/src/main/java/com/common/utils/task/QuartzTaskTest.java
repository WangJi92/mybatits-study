package com.common.utils.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * descrption: 使用quartz实现定时任务
 * authohr: wangji
 * date: 2017-08-22 14:13
 */
@Component("quartzTaskTest")
@Slf4j
public class QuartzTaskTest {

    private volatile Integer count = 0;

    public  void doTask()throws InterruptedException{
        Date date1 = new Date();
        log.info("count ："+count+++"  time:"+date1.toString());
        Thread.currentThread().sleep(10000);
        Date date = new Date();
        log.info("休息了10秒，"+date.toString());
    }
}
