package com.common.utils.ListenableFuture;

import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * descrption: 异步处理机制
 * authohr: wangji
 * date: 2017-10-10 14:12
 */
public class ListenableFutureUseDemo {

   //提供了一个ListenableFuture，其是jdk的Future的封装，用来支持回调（成功/失败)
   //ListenableFutureTask 继承 FutureTask更近一步增加了异步的性质
   //对于那种处理异步事件感觉非常的不错，不用一直去阻塞等待处理事件

    //我自己的SimpleAsyncTaskExecutor 分析 http://blog.csdn.net/u012881904/article/details/78142991
    public static void main(String[] args) throws Exception{
        ListenableFutureTask<String> task = new ListenableFutureTask<String>(new Callable() {
            public Object call() throws Exception {
                Thread.sleep(10 * 1000L);
                System.out.println("=======task execute");
                return "hello";
            }
        });

        task.addCallback(new ListenableFutureCallback<String>() {
            public void onSuccess(String result) {
                System.out.println("===success callback 1"+result);
            }

            public void onFailure(Throwable t) {
            }
        });

        task.addCallback(new ListenableFutureCallback<String>() {
            public void onSuccess(String result) {
                System.out.println("===success callback 2"+result);
            }

            public void onFailure(Throwable t) {
            }
        });
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(task);
        String result = task.get();
        System.out.println(result);
    }
}
//=======task execute
//hello 原始的FutureTask阻塞等待的方式
//===success callback 1hello //主要利用了FutureTask执行完成之后会调用done方法，然后执行异步事件，对于注册的监听启动了队列的形式，线程安全的执行每个任务
//===success callback 2hello

