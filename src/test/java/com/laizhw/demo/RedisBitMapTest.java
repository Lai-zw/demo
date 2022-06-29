package com.laizhw.demo;

import com.laizhw.demo.redis.RedisBitMap;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 作者：热黄油啤酒
 * 链接：https://juejin.cn/post/7043724302746648583
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class RedisBitMapTest extends DemoApplicationTests {

    @Autowired
    RedisBitMap redisBitMap;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisBitMapTest.class);


    @Test
    public void setOnlineStatusTest() {
        // 10000个人
        for (int i = 0; i < 10000; i++) {
            // 设置偶数在线 奇数不在线
            redisBitMap.setOnlineStatus(i, i % 2 == 0);
        }
    }

    @Test
    public void onlineCountTest() {
        Long i = redisBitMap.getOnlineCount();
        LOGGER.info("oline count = {}", i);

    }
}
