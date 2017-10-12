import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.config.MethodInvokingBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * descrption:
 * authohr: wangji
 * date: 2017-08-24 13:35
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath:spring-methodInvoking.xml"})
public class MethodTest extends AbstractJUnit4SpringContextTests{


    @Resource(name = "sysProps")
    public Properties properties;

    @Resource(name ="javaVersion")
    public String javaVersion;

    @Resource(name = "testBean")
    public MethodInvokingBean methodInvokingBean;

    @Test
    public void testMethod(){
       // log.info(methodInvokingBean.getTargetMethod());
    }


    @Test
    public void test(){
        log.info(properties.toString());
        log.info(javaVersion.toString());
    }


}
