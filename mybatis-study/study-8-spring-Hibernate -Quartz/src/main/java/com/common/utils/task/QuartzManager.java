//package com.common.utils.task;
//
//import org.quartz.CronTrigger;
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerFactory;
//import org.quartz.impl.StdSchedulerFactory;
//
///**
// * spring3.1以下的版本必须使用quartz1.x系列，3.1以上的版本才支持quartz 2.x，不然会出错。
// * descrption: Quartz调度管理器 http://www.cnblogs.com/xrab/p/5850186.html
// * authohr: wangji
// * date: 2017-10-12 9:51
// */
//public class QuartzManager {
//    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
//    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";
//
//    /**
//     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
//     *
//     * @param sched
//     *            调度器
//     *
//     * @param jobName
//     *            任务名
//     * @param cls
//     *            任务
//     * @param time
//     *            时间设置，参考quartz说明文档
//     *
//     * @Title: QuartzManager.java
//     */
//    public static void addJob(Scheduler sched, String jobName, @SuppressWarnings("rawtypes") Class cls, String time) {
//        try {
//            JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, cls);// 任务名，任务组，任务执行类
//            // 触发器
//            CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
//            trigger.setCronExpression(time);// 触发器时间设定
//            sched.scheduleJob(jobDetail, trigger);
//            // 启动
//            if (!sched.isShutdown()) {
//                sched.start();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @Description: 添加一个定时任务
//     *
//     * @param sched
//     *            调度器
//     *
//     * @param jobName
//     *            任务名
//     * @param jobGroupName
//     *            任务组名
//     * @param triggerName
//     *            触发器名
//     * @param triggerGroupName
//     *            触发器组名
//     * @param jobClass
//     *            任务
//     * @param time
//     *            时间设置，参考quartz说明文档
//     *
//     * @Title: QuartzManager.java
//     */
//    public static void addJob(Scheduler sched, String jobName, String jobGroupName, String triggerName, String triggerGroupName, @SuppressWarnings("rawtypes") Class jobClass, String time) {
//        try {
//            JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass);// 任务名，任务组，任务执行类
//            // 触发器
//            CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组
//            trigger.setCronExpression(time);// 触发器时间设定
//            sched.scheduleJob(jobDetail, trigger);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
//     *
//     * @param sched
//     *            调度器
//     * @param jobName
//     * @param time
//     *
//     * @Title: QuartzManager.java
//     */
//    @SuppressWarnings("rawtypes")
//    public static void modifyJobTime(Scheduler sched, String jobName, String time) {
//        try {
//            CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName, TRIGGER_GROUP_NAME);
//            if (trigger == null) {
//                return;
//            }
//            String oldTime = trigger.getCronExpression();
//            if (!oldTime.equalsIgnoreCase(time)) {
//                JobDetail jobDetail = sched.getJobDetail(jobName, JOB_GROUP_NAME);
//                Class objJobClass = jobDetail.getJobClass();
//                removeJob(sched, jobName);
//                addJob(sched, jobName, objJobClass, time);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @Description: 修改一个任务的触发时间
//     *
//     * @param sched
//     *            调度器 *
//     * @param sched
//     *            调度器
//     * @param triggerName
//     * @param triggerGroupName
//     * @param time
//     *
//     * @Title: QuartzManager.java
//     */
//    public static void modifyJobTime(Scheduler sched, String triggerName, String triggerGroupName, String time) {
//        try {
//            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName, triggerGroupName);
//            if (trigger == null) {
//                return;
//            }
//            String oldTime = trigger.getCronExpression();
//            if (!oldTime.equalsIgnoreCase(time)) {
//                CronTrigger ct = (CronTrigger) trigger;
//                // 修改时间
//                ct.setCronExpression(time);
//                // 重启触发器
//                sched.resumeTrigger(triggerName, triggerGroupName);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
//     *
//     * @param sched
//     *            调度器
//     * @param jobName
//     *
//     * @Title: QuartzManager.java
//     */
//    public static void removeJob(Scheduler sched, String jobName) {
//        try {
//            sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
//            sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
//            sched.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @Description: 移除一个任务
//     *
//     * @param sched
//     *            调度器
//     * @param jobName
//     * @param jobGroupName
//     * @param triggerName
//     * @param triggerGroupName
//     *
//     * @Title: QuartzManager.java
//     */
//    public static void removeJob(Scheduler sched, String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
//        try {
//            sched.pauseTrigger(triggerName, triggerGroupName);// 停止触发器
//            sched.unscheduleJob(triggerName, triggerGroupName);// 移除触发器
//            sched.deleteJob(jobName, jobGroupName);// 删除任务
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @Description:启动所有定时任务
//     *
//     * @param sched
//     *            调度器
//     *
//     * @Title: QuartzManager.java
//     */
//    public static void startJobs(Scheduler sched) {
//        try {
//            sched.start();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @Description:关闭所有定时任务
//     *
//     *
//     * @param sched
//     *            调度器
//     *
//     *
//     * @Title: QuartzManager.java
//     */
//    public static void shutdownJobs(Scheduler sched) {
//        try {
//            if (!sched.isShutdown()) {
//                sched.shutdown();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void main(String[] args) throws Exception{
//        SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
//        Scheduler sche = gSchedulerFactory.getScheduler();
//        String job_name = "动态任务调度";
//        System.out.println("【系统启动】开始(每1秒输出一次)...");
//        QuartzManager.addJob(sche, job_name, HelloJob.class, "0/1 * * * * ?");
//
//        Thread.sleep(3000);
//        System.out.println("【修改时间】开始(每2秒输出一次)...");
//        QuartzManager.modifyJobTime(sche, job_name, "10/2 * * * * ?");
//        Thread.sleep(4000);
//        System.out.println("【移除定时】开始...");
//        QuartzManager.removeJob(sche, job_name);
//        System.out.println("【移除定时】成功");
//
//        System.out.println("【再次添加定时任务】开始(每10秒输出一次)...");
//        QuartzManager.addJob(sche, job_name, HelloJob.class, "*/10 * * * * ?");
//        Thread.sleep(30000);
//        System.out.println("【移除定时】开始...");
//        QuartzManager.removeJob(sche, job_name);
//        System.out.println("【移除定时】成功");
//    }
//}
