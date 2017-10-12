package com.common.utils.task;

import org.quartz.*;

/**
 * descrption:  spring3.1以下的版本必须使用quartz1.x系列，3.1以上的版本才支持quartz 2.x，不然会出错。
 * http://www.cnblogs.com/xuha0/p/6813617.html 管理Quartz2.2
 * authohr: wangji
 * date: 2017-10-12 10:14
 * https://www.2cto.com/kf/201708/674079.html
 * http://blog.csdn.net/liuchuanhong1/article/details/60873295
 * http://blog.csdn.net/pujiaolin/article/details/67712429
 * http://blog.csdn.net/shirley_john_thomas/article/details/60146783 排除节假日的情况 quartz实现每周一至周五 非法定节假日 每天9:30-11:30,13:00-15:00执行定时任务
 */
public class QuartzManagerVision2 {

    Scheduler sched;

    public Scheduler getSched() {
        return sched;
    }

    public void setSched(Scheduler sched) {
        this.sched = sched;
    }
   //创建一个执行的任务
    public   JobDetail createJob(String jobName, String groupName, Class jobClass,String description){
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, groupName)
                .withDescription(description)
                .build();
        return jobDetail;
    }

// DailyTimeIntervalScheduleBuilder
// CronScheduleBuilder
// ScheduleBuilder
// SimpleScheduleBuilder
// TriggerBuilder
    public Trigger createTrigger(String triggerName,String triggerGroup,String description){
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName,triggerGroup)
                .withDescription(description)
                .startNow()
                .withSchedule( SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(1))
                .modifiedByCalendar("")//可以排除掉一些日期,可以自动添加到sche中去，排除一些日历
                .build();
        return trigger;
    }

    public void addJob(Trigger trigger,JobDetail jobDetail){
        try {
            sched.scheduleJob(jobDetail,trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void deleteJob(String triggerName,String jobName,String jobGroup,String triggerGroup){
        try {
            TriggerKey triggerKey = new TriggerKey(triggerName,triggerGroup);
            sched.pauseTrigger(triggerKey);//停止触发器
            sched.unscheduleJob(triggerKey);
            sched.deleteJob(new JobKey(jobName,jobGroup));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void pauseOrResumeJob(String jobName,String jobGroup,boolean pauseFlag){
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            if(pauseFlag ==true){
                sched.pauseJob(jobKey);
            }else{
                sched.resumeJob(jobKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
