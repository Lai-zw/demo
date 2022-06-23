package demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import demo.redis.RedisScan;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * 作者：热黄油啤酒
 * 链接：https://juejin.cn/post/7043724302746648583
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class RedisScanTest extends DemoApplicationTests {

    @Autowired
    RedisScan redisScan;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisScanTest.class);

    @Test
    public void testMock() {
        TimeInterval timer = DateUtil.timer();
        // redisScan.mock("mock:", 1000000);
        redisScan.mockByPipe("mock:", 1000000);
        LOGGER.info("耗时：{}ms", timer.interval());
    }

    @Test
    public void batchDeleteTest() {
        TimeInterval timer = DateUtil.timer();
        redisScan.batchDelete("mock:*", 1000);
        LOGGER.info("耗时：{}ms", timer.interval());
    }

    @Test
    public void scanTest() {
        TimeInterval timer = DateUtil.timer();
        Set<String> scan = redisScan.scan("mock:*", 10);
        scan.forEach(System.out::println);

        LOGGER.info("耗时：{}ms", timer.interval());
    }
}
