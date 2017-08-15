##Spring LocalVariableTableParameterNameDiscoverer获取方法的参数名
 
*   问题：Java.lang.reflect 包中提供了很多方法，获取所有的方法，获取所有的参数类型等，但是却没有一个方法能够帮助我们获取方法的参数名列表。
*   解决办法：可以通过ASM提供的通过字节码获取方法的参数名称，spring给我们集成了这个东西，让我们使用起来非常的方便
*   作用：这个东西有啥用，其实我们在Action层就会使用到，前端传递过来的参数是怎么反馈到具体的方法上的参数的值？这个需要理解，可以和最原始的Htttprequest.getParamer("xxx")这样的性质一样的吧。然后通过反射注入到具体的值中，但是怎么一一对应起来呢？这个就需要方法的参数名称啦，你懂了？通过名称看一下传递进来的参数是否含有具体的值，然后通过反射写入到具体的位置，这样我们在Action层的具体的方法中，可以获取到请求的值，而不用自己去手动的处理Httprequest.getParamer("xxx")，这个也是框架给我们带来的便利性，离不开你.......

1.  代码实践
主要的方法：LocalVariableTableParameterNameDiscoverer.getParameterNames(Method method);然后返回参数的Stirng数组，没有返回NULL
```java
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

```