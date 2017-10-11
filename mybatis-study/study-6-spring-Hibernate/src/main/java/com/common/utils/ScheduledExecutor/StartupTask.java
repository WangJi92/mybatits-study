package com.common.utils.ScheduledExecutor;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * descrption: spring中配置使用的一个简单的线程
 * authohr: wangji
 * date: 2017-10-10 18:24
 */
@Slf4j
public class StartupTask implements Runnable {
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error("线程中断",e);
        }
        log.info(new Date()+" ScheduledExecutorTask...");
    }
}
