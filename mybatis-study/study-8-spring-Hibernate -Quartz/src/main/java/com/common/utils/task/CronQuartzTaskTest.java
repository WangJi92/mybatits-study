package com.common.utils.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * descrption:
 * authohr: wangji
 * date: 2017-08-22 15:20
 */
@Slf4j
@Component("cronQuartzTaskTest")
public class CronQuartzTaskTest {

    public  void doTask()throws InterruptedException{
        Date date= new Date();
        log.info("执行定时任务"+date.toString());
    }
}
