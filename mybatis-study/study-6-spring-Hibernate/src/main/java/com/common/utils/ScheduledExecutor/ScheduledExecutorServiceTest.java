package com.common.utils.ScheduledExecutor;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * descrption: JDKScheduledExecutorService的使用、
 * authohr: wangji
 * date: 2017-10-10 15:29
 */
@Slf4j
public class ScheduledExecutorServiceTest {

    public static void main(String[] args) {
        //schdule();
        //scheduleWithFixedDelay();
        //scheduleAtFixedRate1();
        scheduleAtFixedRate2();
    }
    // schedule(Runnable command, long delay, TimeUnit unit)，schedule方法被用来延迟指定时间后执行某个指定任务。
    public static void schdule() {
//        开始:2017-10-10 03:31:59
//        测试schedule方法：2017-10-10 03:32:00
        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(5);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println("开始:" + sdf.format(new Date()));
        schedule.schedule(new Runnable() {
            public void run() {
                System.out.println("测试schedule方法："+sdf.format(new Date()));
            }
        },1, TimeUnit.SECONDS);
    }


//    scheduleWithFixedDelay(Runnable command, long initialDelay, long delay,TimeUnit unit)
//    创建并执行一个在给定初始延迟后首次启用的定期操作，随后，
//    在每一次执行终止和下一次执行开始之间都存在给定的延迟，
//    如果任务的执行时间超过了廷迟时间（delay），
//    下一个任务则会在（当前任务执行所需时间+delay）后执行。
    public static void scheduleWithFixedDelay(){
//        开始:2017-10-10 03:35:02
//        测试scheduleWithFixedDelay方法：2017-10-10 03:35:06 初始化花了4秒
//        测试scheduleWithFixedDelay方法：2017-10-10 03:35:11  延迟两秒+睡眠3秒（下一次五秒）
//        测试scheduleWithFixedDelay方法：2017-10-10 03:35:16
//        测试scheduleWithFixedDelay方法：2017-10-10 03:35:21
        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(5);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println("开始:" + sdf.format(new Date()));
        schedule.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.out.println("测试scheduleWithFixedDelay方法："+sdf.format(new Date()));
            }
        },1,2, TimeUnit.SECONDS);
    }
    //scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
    //创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期；也就是将在 initialDelay 后开始执行，
    // 然后在initialDelay+period 后执行，接着在 initialDelay + 2 * period 后执行，依此类推。
    //如果任务的执行时间小于period，将会按上述规律执行。否则，则会按任务的实际执行时间进行周期执行。

//    command：执行线程
//    initialDelay：初始化延时
//    period：两次开始执行最小间隔时间
//    unit：计时单位
    public static void scheduleAtFixedRate1(){
//        开始:2017-10-10 03:43:41
//        测试scheduleAtFixedRate方法：2017-10-10 03:43:45
//        测试scheduleAtFixedRate方法：2017-10-10 03:43:50
//          从上面的测试结果可以看到如果任务的执行时间小于period，每次间隔period执行一次任务。
        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(5);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println("开始:" + sdf.format(new Date()));
        schedule.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.out.println("测试scheduleAtFixedRate方法："+sdf.format(new Date()));
            }
        },1,5, TimeUnit.SECONDS);
    }
    //测试代码(任务的执行时间大于period)
    public static void scheduleAtFixedRate2(){
        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(5);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println("开始:" + sdf.format(new Date()));
        schedule.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.out.println("测试scheduleAtFixedRate方法："+sdf.format(new Date()));
            }
        },1,2, TimeUnit.SECONDS);
    }
//    开始:2017-10-10 03:45:55
//    测试scheduleAtFixedRate方法：2017-10-10 03:45:59
//    测试scheduleAtFixedRate方法：2017-10-10 03:46:02
//    测试scheduleAtFixedRate方法：2017-10-10 03:46:05
//    测试scheduleAtFixedRate方法：2017-10-10 03:46:08
//    测试scheduleAtFixedRate方法：2017-10-10 03:46:11
//    从上面的测试结果可以看到如果任务的执行时间大于period，则会按任务的实际执行时间进行周期执行

}
