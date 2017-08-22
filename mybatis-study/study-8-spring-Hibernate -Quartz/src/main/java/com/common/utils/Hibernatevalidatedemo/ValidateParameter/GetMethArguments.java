package com.common.utils.Hibernatevalidatedemo.ValidateParameter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * descrption: 通过spring的LocalVariableTableParameterNameDiscoverer 获取方法的参数，spring也是通过使用ASM通过字节码获取方法中参数的具体的名称
 * authohr: wangji
 * date: 2017-08-15 10:20
 */
@Slf4j
public class GetMethArguments {

    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    public void testArguments(String test,Integer myInteger,boolean booleanTest){
    }
    public void test(){

    }
    public static void main(String[] args) {
        Method[] methods  = GetMethArguments.class.getMethods();
        for(Method method:methods){
            String[] paraNames = parameterNameDiscoverer.getParameterNames(method);

            log.info("methodName:"+method.getName());
            if(paraNames !=null){
                StringBuffer buffer = new StringBuffer();
                for(String string:paraNames){
                    buffer.append(string).append("\t");
                }
                log.info("parameArguments:"+buffer.toString());
            }else{
                log.info("无参数");
            }
        }
    }
//2017-08-15 10:32:46,914  INFO [GetMethArguments.java:29] : methodName:main
//2017-08-15 10:32:46,917  INFO [GetMethArguments.java:35] : parameArguments:args
//2017-08-15 10:32:46,917  INFO [GetMethArguments.java:29] : methodName:test
//2017-08-15 10:32:46,919  INFO [GetMethArguments.java:35] : parameArguments:
//2017-08-15 10:32:46,919  INFO [GetMethArguments.java:29] : methodName:testArguments
//2017-08-15 10:32:46,919  INFO [GetMethArguments.java:35] : parameArguments:test	myInteger	booleanTest
//2017-08-15 10:32:46,924  INFO [GetMethArguments.java:29] : methodName:wait
//2017-08-15 10:32:46,924  INFO [GetMethArguments.java:37] : 无参数
//2017-08-15 10:32:46,924  INFO [GetMethArguments.java:29] : methodName:wait
//2017-08-15 10:32:46,924  INFO [GetMethArguments.java:37] : 无参数
//2017-08-15 10:32:46,924  INFO [GetMethArguments.java:29] : methodName:wait
//2017-08-15 10:32:46,924  INFO [GetMethArguments.java:37] : 无参数
//2017-08-15 10:32:46,924  INFO [GetMethArguments.java:29] : methodName:equals
//2017-08-15 10:32:46,925  INFO [GetMethArguments.java:37] : 无参数
//2017-08-15 10:32:46,925  INFO [GetMethArguments.java:29] : methodName:toString
//2017-08-15 10:32:46,925  INFO [GetMethArguments.java:37] : 无参数
//2017-08-15 10:32:46,928  INFO [GetMethArguments.java:29] : methodName:hashCode
//2017-08-15 10:32:46,928  INFO [GetMethArguments.java:37] : 无参数
//2017-08-15 10:32:46,928  INFO [GetMethArguments.java:29] : methodName:getClass
//2017-08-15 10:32:46,928  INFO [GetMethArguments.java:37] : 无参数
//2017-08-15 10:32:46,928  INFO [GetMethArguments.java:29] : methodName:notify
//2017-08-15 10:32:46,928  INFO [GetMethArguments.java:37] : 无参数
//2017-08-15 10:32:46,928  INFO [GetMethArguments.java:29] : methodName:notifyAll
//2017-08-15 10:32:46,928  INFO [GetMethArguments.java:37] : 无参数
}
