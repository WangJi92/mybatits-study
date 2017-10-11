#ScheduledExecutorFactoryBean的简单源码解析以及使用
>ScheduledExecutorFactoryBean 配合ScheduledExecutorTask将JDK原来的ScheduledExecutorService提供的三种API进行了统一的封装，使用相同的方式去执行，非常的便捷，封装的思路也是比较厉害的，很多的东西值得我们去思考和学习。

##原生的ScheduledExecutorService的使用一共有三种API的使用
```java
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

```

##ScheduledExecutorTask 就是将着三种不同的周期执行函数进行统一封装
```java
public class ScheduledExecutorTask {
	private Runnable runnable;//执行的线程
	private long delay = 0;
	private long period = -1;//周期性时间或者延迟时间或者用0表示只执行一次，将三种API都使用起来了
	private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
	private boolean fixedRate = false;
}
```
1. 如果配置period=0将执行这个方法：schedule(Runnable command, long delay, TimeUnit unit)，schedule方法被用来延迟指定时间后执行某个指定任务
只执行一次

2. 如果配置fixedRate = true && period>0 将执行这方法 scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
固定的频率，如果任务的周期大于peroid，将使用任务的周期作为频率

3.  如果配置fixdRate = false period>0 将执行这个方法 scheduleWithFixedDelay(Runnable command, long initialDelay, long delay,TimeUnit unit)
有延迟的执行，initialDelay与delay相同  delay与peroid这个值相同 

只是将Runable简单的包装一下子
```java
public class ScheduledExecutorTask {
    private Runnable runnable;
    private long delay = 0L;
    private long period = -1L;
    private TimeUnit timeUnit;
    private boolean fixedRate;

    public ScheduledExecutorTask() {
        this.timeUnit = TimeUnit.MILLISECONDS;
        this.fixedRate = false;
    }

    public ScheduledExecutorTask(Runnable executorTask) {
        this.timeUnit = TimeUnit.MILLISECONDS;
        this.fixedRate = false;
        this.runnable = executorTask;
    }
    ....
}
```

##ScheduledExecutorFactoryBean，工厂Bean，可以通过getObject或者到当前配置的实例，工厂Bean的作用就是简化我们的配置，将大大减少工作量
类图

###程序的入口点为 实现了InitializingBean初始化Bean完成之后执行方法
父类ExecutorConfigurationSupport 初始化
```
	public void afterPropertiesSet() {
		initialize();
	}
	/**
	 * Set up the ExecutorService.
	 */
	public void initialize() {
		if (logger.isInfoEnabled()) {
			logger.info("Initializing ExecutorService " + (this.beanName != null ? " '" + this.beanName + "'" : ""));
		}
		if (!this.threadNamePrefixSet && this.beanName != null) {
			setThreadNamePrefix(this.beanName + "-");
		}
		this.executor = initializeExecutor(this.threadFactory, this.rejectedExecutionHandler);
	}
	//放置给后代的钩子函数
	protected abstract ExecutorService initializeExecutor(
    			ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler);
	
	
```
ScheduledExecutorFactoryBean 实现了这个方法 initializeExecutor，初始化线程池，以及将任务注入到线程池中，还有进行一些异常出现后的包装
```
@Override
	@UsesJava7
	protected ExecutorService initializeExecutor(
			ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        //使用JDK的API创建一个ScheduledExecutorService  
        // new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
		ScheduledExecutorService executor =
				createExecutor(this.poolSize, threadFactory, rejectedExecutionHandler);
        //设置removeOncancle属性
		if (this.removeOnCancelPolicy) {
			if (setRemoveOnCancelPolicyAvailable && executor instanceof ScheduledThreadPoolExecutor) {
				((ScheduledThreadPoolExecutor) executor).setRemoveOnCancelPolicy(true);
			}
			else {
				logger.info("Could not apply remove-on-cancel policy - not a Java 7+ ScheduledThreadPoolExecutor");
			}
		}

		// 创建特殊的task放置在线程池中，Register specified ScheduledExecutorTasks, if necessary.
		//这里是非常重要的逻辑步骤了
		if (!ObjectUtils.isEmpty(this.scheduledExecutorTasks)) {
			registerTasks(this.scheduledExecutorTasks, executor);
		}

		// Wrap executor with an unconfigurable decorator.进行一下包装，不允许修改属性
		this.exposedExecutor = (this.exposeUnconfigurableExecutor ?
				Executors.unconfigurableScheduledExecutorService(executor) : executor);

		return executor;
	}
```
对于三种不同的API区分的实现
```
protected void registerTasks(ScheduledExecutorTask[] tasks, ScheduledExecutorService executor) {
		for (ScheduledExecutorTask task : tasks) {
		   //包装任务，掏出异常是否继续进行执行
			Runnable runnable = getRunnableToSchedule(task);
			// public boolean isOneTimeTask() {
            //		return (this.period <= 0);
            // }
			if (task.isOneTimeTask()) {
				executor.schedule(runnable, task.getDelay(), task.getTimeUnit());
			}
			else {
				if (task.isFixedRate()) {
					executor.scheduleAtFixedRate(runnable, task.getDelay(), task.getPeriod(), task.getTimeUnit());
				}
				else {
					executor.scheduleWithFixedDelay(runnable, task.getDelay(), task.getPeriod(), task.getTimeUnit());
				}
			}
		}
	}
```
对于任务进行包装，执行任务异常之后是否继续进行下一次的任务呀
```
/**
	 * Determine the actual Runnable to schedule for the given task.
	 * <p>Wraps the task's Runnable in a
	 * {@link org.springframework.scheduling.support.DelegatingErrorHandlingRunnable}
	 * that will catch and log the Exception. If necessary, it will suppress the
	 * Exception according to the
	 * {@link #setContinueScheduledExecutionAfterException "continueScheduledExecutionAfterException"}
	 * flag.
	 * @param task the ScheduledExecutorTask to schedule
	 * @return the actual Runnable to schedule (may be a decorator)
	 */
	protected Runnable getRunnableToSchedule(ScheduledExecutorTask task) {
		return (this.continueScheduledExecutionAfterException ?
				new DelegatingErrorHandlingRunnable(task.getRunnable(), TaskUtils.LOG_AND_SUPPRESS_ERROR_HANDLER) :
				new DelegatingErrorHandlingRunnable(task.getRunnable(), TaskUtils.LOG_AND_PROPAGATE_ERROR_HANDLER));
	}
	public class DelegatingErrorHandlingRunnable implements Runnable {
    
    	private final Runnable delegate;
    
    	private final ErrorHandler errorHandler;
    
    
    	/**
    	 * Create a new DelegatingErrorHandlingRunnable.
    	 * @param delegate the Runnable implementation to delegate to
    	 * @param errorHandler the ErrorHandler for handling any exceptions
    	 */
    	public DelegatingErrorHandlingRunnable(Runnable delegate, ErrorHandler errorHandler) {
    		Assert.notNull(delegate, "Delegate must not be null");
    		Assert.notNull(errorHandler, "ErrorHandler must not be null");
    		this.delegate = delegate;
    		this.errorHandler = errorHandler;
    	}
    
    	@Override
    	public void run() {
    		try {
    			this.delegate.run();
    		}
    		catch (UndeclaredThrowableException ex) {
    			this.errorHandler.handleError(ex.getUndeclaredThrowable());
    		}
    		catch (Throwable ex) {
    			this.errorHandler.handleError(ex);
    		}
    	}
    
    	@Override
    	public String toString() {
    		return "DelegatingErrorHandlingRunnable for " + this.delegate;
    	}
    }
    private static class LoggingErrorHandler implements ErrorHandler {
    
    		private final Log logger = LogFactory.getLog(LoggingErrorHandler.class);
    
    		@Override
    		public void handleError(Throwable t) {
    			if (logger.isErrorEnabled()) {
    				logger.error("Unexpected error occurred in scheduled task.", t);
    			}
    		}
    }
    private static class PropagatingErrorHandler extends LoggingErrorHandler {
    
    		@Override
    		public void handleError(Throwable t) {
    			super.handleError(t);
    			ReflectionUtils.rethrowRuntimeException(t);
    		}
    }
   
```

##使用
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context-3.2.xsd
		http://www.springframework.org/schema/aop
		http://www.springframework.org/schema/aop/spring-aop-3.2.xsd
		http://www.springframework.org/schema/tx
		http://www.springframework.org/schema/tx/spring-tx-3.2.xsd ">

    <!-- 使用Spring的任务调度类ScheduledExecutorTask，将ScheduledExecutorService三种不同的执行函数封装起来形成一种函数进行执行-->
    <bean id="startupScheduledTask" class="org.springframework.scheduling.concurrent.ScheduledExecutorTask">
       <!--  ScheduledExecutorService scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) 如果任务的执行时间大于period，则会按任务的实际执行时间进行周期执行-->
       <!--  ScheduledExecutorService scheduleWithFixedDelay(Runnable command, long initialDelay, long delay,TimeUnit unit)-->
       <!-- 如果fixedRate为true代表以固定的频率执行代码，依旧是第一个方法，周期性的-->
        <property name="fixedRate" value="false"></property>
        <property name="delay" value="3000" /><!--多久开始执行-->
        <!-- 每次任务间隔0秒 0表示只执行一次  相当于这个方法 ScheduledExecutorService schedule(Runnable command, long delay, TimeUnit unit)，schedule方法被用来延迟指定时间后执行某个指定任务。-->
        <!--period，相当于代码中的peroid或者delay-->
        <property name="period" value="3000" />
        <!-- 配置主任务 -->
        <property name="runnable">
            <bean class="com.common.utils.ScheduledExecutor.StartupTask"></bean>
        </property>
    </bean>
    <bean id="timerFactory" class="org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean">
        <property name="scheduledExecutorTasks">
            <list>
                <ref bean="startupScheduledTask" />
                <!-- 如有多任务，可以将任务bean放到list中 -->
            </list>
        </property>
        <property name="continueScheduledExecutionAfterException" value="true"></property><!--抛出异常继续执行？-->
        <property name="poolSize" value="2"></property><!--线程池的大小-->
    </bean>
</beans>
```
```java
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
```

源码：
```java

public class ScheduledExecutorFactoryBean extends ExecutorConfigurationSupport implements FactoryBean<ScheduledExecutorService> {
    private static final boolean setRemoveOnCancelPolicyAvailable;
    private int poolSize = 1;
    private ScheduledExecutorTask[] scheduledExecutorTasks;
    private boolean removeOnCancelPolicy = false;
    private boolean continueScheduledExecutionAfterException = false;
    private boolean exposeUnconfigurableExecutor = false;
    private ScheduledExecutorService exposedExecutor;

    public ScheduledExecutorFactoryBean() {
    }

    public void setPoolSize(int poolSize) {
        Assert.isTrue(poolSize > 0, "\'poolSize\' must be 1 or higher");
        this.poolSize = poolSize;
    }

    public void setScheduledExecutorTasks(ScheduledExecutorTask... scheduledExecutorTasks) {
        this.scheduledExecutorTasks = scheduledExecutorTasks;
    }

    public void setRemoveOnCancelPolicy(boolean removeOnCancelPolicy) {
        this.removeOnCancelPolicy = removeOnCancelPolicy;
    }

    public void setContinueScheduledExecutionAfterException(boolean continueScheduledExecutionAfterException) {
        this.continueScheduledExecutionAfterException = continueScheduledExecutionAfterException;
    }

    public void setExposeUnconfigurableExecutor(boolean exposeUnconfigurableExecutor) {
        this.exposeUnconfigurableExecutor = exposeUnconfigurableExecutor;
    }

    @UsesJava7
    protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        ScheduledExecutorService executor = this.createExecutor(this.poolSize, threadFactory, rejectedExecutionHandler);
        if(this.removeOnCancelPolicy) {
            if(setRemoveOnCancelPolicyAvailable && executor instanceof ScheduledThreadPoolExecutor) {
                ((ScheduledThreadPoolExecutor)executor).setRemoveOnCancelPolicy(true);
            } else {
                this.logger.info("Could not apply remove-on-cancel policy - not a Java 7+ ScheduledThreadPoolExecutor");
            }
        }

        if(!ObjectUtils.isEmpty(this.scheduledExecutorTasks)) {
            this.registerTasks(this.scheduledExecutorTasks, executor);
        }

        this.exposedExecutor = this.exposeUnconfigurableExecutor?Executors.unconfigurableScheduledExecutorService(executor):executor;
        return executor;
    }

    protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        return new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
    }

    protected void registerTasks(ScheduledExecutorTask[] tasks, ScheduledExecutorService executor) {
        ScheduledExecutorTask[] var3 = tasks;
        int var4 = tasks.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            ScheduledExecutorTask task = var3[var5];
            Runnable runnable = this.getRunnableToSchedule(task);
            if(task.isOneTimeTask()) {
                executor.schedule(runnable, task.getDelay(), task.getTimeUnit());
            } else if(task.isFixedRate()) {
                executor.scheduleAtFixedRate(runnable, task.getDelay(), task.getPeriod(), task.getTimeUnit());
            } else {
                executor.scheduleWithFixedDelay(runnable, task.getDelay(), task.getPeriod(), task.getTimeUnit());
            }
        }

    }

    protected Runnable getRunnableToSchedule(ScheduledExecutorTask task) {
        return this.continueScheduledExecutionAfterException?new DelegatingErrorHandlingRunnable(task.getRunnable(), TaskUtils.LOG_AND_SUPPRESS_ERROR_HANDLER):new DelegatingErrorHandlingRunnable(task.getRunnable(), TaskUtils.LOG_AND_PROPAGATE_ERROR_HANDLER);
    }

    public ScheduledExecutorService getObject() {
        return this.exposedExecutor;
    }

    public Class<? extends ScheduledExecutorService> getObjectType() {
        return this.exposedExecutor != null?this.exposedExecutor.getClass():ScheduledExecutorService.class;
    }

    public boolean isSingleton() {
        return true;
    }

    static {
        setRemoveOnCancelPolicyAvailable = ClassUtils.hasMethod(ScheduledThreadPoolExecutor.class, "setRemoveOnCancelPolicy", new Class[]{Boolean.TYPE});
    }
}

public abstract class ExecutorConfigurationSupport extends CustomizableThreadFactory implements BeanNameAware, InitializingBean, DisposableBean {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private ThreadFactory threadFactory = this;
    private boolean threadNamePrefixSet = false;
    private RejectedExecutionHandler rejectedExecutionHandler = new AbortPolicy();
    private boolean waitForTasksToCompleteOnShutdown = false;
    private int awaitTerminationSeconds = 0;
    private String beanName;
    private ExecutorService executor;

    public ExecutorConfigurationSupport() {
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = (ThreadFactory)(threadFactory != null?threadFactory:this);
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        super.setThreadNamePrefix(threadNamePrefix);
        this.threadNamePrefixSet = true;
    }

    public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
        this.rejectedExecutionHandler = (RejectedExecutionHandler)(rejectedExecutionHandler != null?rejectedExecutionHandler:new AbortPolicy());
    }

    public void setWaitForTasksToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
    }

    public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
    }

    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void afterPropertiesSet() {
        this.initialize();
    }

    public void initialize() {
        if(this.logger.isInfoEnabled()) {
            this.logger.info("Initializing ExecutorService " + (this.beanName != null?" \'" + this.beanName + "\'":""));
        }

        if(!this.threadNamePrefixSet && this.beanName != null) {
            this.setThreadNamePrefix(this.beanName + "-");
        }

        this.executor = this.initializeExecutor(this.threadFactory, this.rejectedExecutionHandler);
    }

    protected abstract ExecutorService initializeExecutor(ThreadFactory var1, RejectedExecutionHandler var2);

    public void destroy() {
        this.shutdown();
    }

    public void shutdown() {
        if(this.logger.isInfoEnabled()) {
            this.logger.info("Shutting down ExecutorService" + (this.beanName != null?" \'" + this.beanName + "\'":""));
        }

        if(this.waitForTasksToCompleteOnShutdown) {
            this.executor.shutdown();
        } else {
            this.executor.shutdownNow();
        }

        this.awaitTerminationIfNecessary();
    }

    private void awaitTerminationIfNecessary() {
        if(this.awaitTerminationSeconds > 0) {
            try {
                if(!this.executor.awaitTermination((long)this.awaitTerminationSeconds, TimeUnit.SECONDS) && this.logger.isWarnEnabled()) {
                    this.logger.warn("Timed out while waiting for executor" + (this.beanName != null?" \'" + this.beanName + "\'":"") + " to terminate");
                }
            } catch (InterruptedException var2) {
                if(this.logger.isWarnEnabled()) {
                    this.logger.warn("Interrupted while waiting for executor" + (this.beanName != null?" \'" + this.beanName + "\'":"") + " to terminate");
                }

                Thread.currentThread().interrupt();
            }
        }

    }
}
```





