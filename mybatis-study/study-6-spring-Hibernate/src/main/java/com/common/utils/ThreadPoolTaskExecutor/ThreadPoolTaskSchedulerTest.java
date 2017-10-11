package com.common.utils.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

/**
 * descrption: ThreadPoolTaskScheduler 的简单使用,通过固定编程的方式实现Spring cron的处理
 * authohr: wangji
 * date: 2017-10-11 14:38
 * 核心使用的是ThreadPoolTaskScheduler->ScheduledExecutorService(中三种重要的方法)+cron表达式的实现
 */
@Slf4j
public class ThreadPoolTaskSchedulerTest {

    private TaskScheduler taskScheduler;

    public void doWork() {
        taskScheduler.schedule(new Runnable() {
            public void run() {
                log.info(Thread.currentThread().getName()+"15秒到45秒每隔1秒触发任务");
            }
        },new CronTrigger("15-45 * * * * ?"));
        taskScheduler.schedule(new Runnable() {
            public void run() {
                log.info(Thread.currentThread().getName()+"每分钟的每15秒时任务任务，每隔5秒触发一次 ");
            }
        },new CronTrigger("15/5 * * * * ?"));
    }

    public TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-ThreadPoolTaskScheduler.xml");
//        ThreadPoolTaskExecutorTest threadPoolTaskExecutorTest = (ThreadPoolTaskExecutorTest) applicationContext.getBean("threadPoolTaskSchedulerTest");
//        threadPoolTaskExecutorTest.doWork();
    }
}
// /*@author Juergen Hoeller
//    * @since 3.0
//    * @see org.springframework.core.task.TaskExecutor
//    * @see java.util.concurrent.ScheduledExecutorService
//    * @see org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
//    */
//public interface TaskScheduler {
//
//    /**
//     * Schedule the given {@link Runnable}, invoking it whenever the trigger
//     * indicates a next execution time.
//     * <p>Execution will end once the scheduler shuts down or the returned
//     * {@link ScheduledFuture} gets cancelled.
//     * @param task the Runnable to execute whenever the trigger fires
//     * @param trigger an implementation of the {@link Trigger} interface,
//     * e.g. a {@link org.springframework.scheduling.support.CronTrigger} object
//     * wrapping a cron expression
//     * @return a {@link ScheduledFuture} representing pending completion of the task,
//     * or {@code null} if the given Trigger object never fires (i.e. returns
//     * {@code null} from {@link Trigger#nextExecutionTime})
//     * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
//     * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
//     * @see org.springframework.scheduling.support.CronTrigger
//     */
//    ScheduledFuture<?> schedule(Runnable task, Trigger trigger);
//
//    /**
//     * Schedule the given {@link Runnable}, invoking it at the specified execution time.
//     * <p>Execution will end once the scheduler shuts down or the returned
//     * {@link ScheduledFuture} gets cancelled.
//     * @param task the Runnable to execute whenever the trigger fires
//     * @param startTime the desired execution time for the task
//     * (if this is in the past, the task will be executed immediately, i.e. as soon as possible)
//     * @return a {@link ScheduledFuture} representing pending completion of the task
//     * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
//     * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
//     */
//    ScheduledFuture<?> schedule(Runnable task, Date startTime);
//
//    /**
//     * Schedule the given {@link Runnable}, invoking it at the specified execution time
//     * and subsequently with the given period.
//     * <p>Execution will end once the scheduler shuts down or the returned
//     * {@link ScheduledFuture} gets cancelled.
//     * @param task the Runnable to execute whenever the trigger fires
//     * @param startTime the desired first execution time for the task
//     * (if this is in the past, the task will be executed immediately, i.e. as soon as possible)
//     * @param period the interval between successive executions of the task (in milliseconds)
//     * @return a {@link ScheduledFuture} representing pending completion of the task
//     * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
//     * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
//     */
//    ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period);
//
//    /**
//     * Schedule the given {@link Runnable}, starting as soon as possible and
//     * invoking it with the given period.
//     * <p>Execution will end once the scheduler shuts down or the returned
//     * {@link ScheduledFuture} gets cancelled.
//     * @param task the Runnable to execute whenever the trigger fires
//     * @param period the interval between successive executions of the task (in milliseconds)
//     * @return a {@link ScheduledFuture} representing pending completion of the task
//     * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
//     * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
//     */
//    ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period);
//
//    /**
//     * Schedule the given {@link Runnable}, invoking it at the specified execution time
//     * and subsequently with the given delay between the completion of one execution
//     * and the start of the next.
//     * <p>Execution will end once the scheduler shuts down or the returned
//     * {@link ScheduledFuture} gets cancelled.
//     * @param task the Runnable to execute whenever the trigger fires
//     * @param startTime the desired first execution time for the task
//     * (if this is in the past, the task will be executed immediately, i.e. as soon as possible)
//     * @param delay the delay between the completion of one execution and the start
//     * of the next (in milliseconds)
//     * @return a {@link ScheduledFuture} representing pending completion of the task
//     * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
//     * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
//     */
//    ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay);
//
//    /**
//     * Schedule the given {@link Runnable}, starting as soon as possible and
//     * invoking it with the given delay between the completion of one execution
//     * and the start of the next.
//     * <p>Execution will end once the scheduler shuts down or the returned
//     * {@link ScheduledFuture} gets cancelled.
//     * @param task the Runnable to execute whenever the trigger fires
//     * @param delay the interval between successive executions of the task (in milliseconds)
//     * @return a {@link ScheduledFuture} representing pending completion of the task
//     * @throws org.springframework.core.task.TaskRejectedException if the given task was not accepted
//     * for internal reasons (e.g. a pool overload handling policy or a pool shutdown in progress)
//     */
//    ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay);
//
//}