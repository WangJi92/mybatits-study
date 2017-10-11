package com.common.utils.ThreadPoolTaskExecutor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * descrption:
 * authohr: wangji
 * date: 2017-10-11 14:26
 */
@Component(value = "springTaskCron")
public class SpringTaskCron {
    public void execute1(){
        System.out.printf("Task: %s, Current time: %s\n", 1, new Date().toString());
    }

    public void execute2(){
        System.out.printf("Task: %s, Current time: %s\n", 2,new Date().toString());
    }

    public void execute3(){
        System.out.printf("Task: %s, Current time: %s\n", 3,new Date().toString());
    }

    public void execute4(){
        System.out.printf("Task: %s, Current time: %s\n", 4,new Date().toString());
    }

    public void execute5(){
        System.out.printf("Task: %s, Current time: %s\n", 5,new Date().toString());
    }

    public void execute6(){
        System.out.printf("Task: %s, Current time: %s\n", 6,new Date().toString());
    }

    public void execute7(){
        System.out.printf("Task: %s, Current time: %s\n", 7,new Date().toString());
    }

    public void execute8(){
        System.out.printf("Task: %s, Current time: %s\n", 8,new Date().toString());
    }

    public void execute9(){
        System.out.printf("Task: %s, Current time: %s\n", 9,new Date().toString());
    }

    public void execute10(){
        System.out.printf("Task: %s, Current time: %s\n", 10,new Date().toString());
    }
    public void execute11(){
        System.out.printf("Task: %s, Current time: %s\n", 11,new Date().toString());
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-scheduled.xml");
    }

}
