package BaseSpringTest;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * descrption: spring-test的基类，这样就可以很方便的进行测试Spring-IOC容器里面的Bean了
 * authohr: wangji
 * date: 2017-07-31 18:23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath:Spring-applicationContext.xml"})
public class JUnitServiceBase extends AbstractTransactionalJUnit4SpringContextTests {
    //http://www.cnblogs.com/chyg/p/4139343.html 单元测试使用事务
}