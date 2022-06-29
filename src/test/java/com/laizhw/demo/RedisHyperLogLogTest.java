package com.laizhw.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * 基数不大，数据量不大就用不上，会有点大材小用浪费空间
 * 有局限性，就是只能统计基数数量，而没办法去知道具体的内容是什么
 * <p>
 * 作者：热黄油啤酒
 * 链接：https://juejin.cn/post/7043724302746648583
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class RedisHyperLogLogTest extends DemoApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisHyperLogLogTest.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void setTest() {
        TimeInterval timer = DateUtil.timer();
        String key = "pv_set:20211220";
        // 模拟1000次操作
        for (int i = 1; i < 1000; i++) {
            redisTemplate.opsForSet().add(key, String.valueOf(i));
        }
        Long size = redisTemplate.opsForSet().size(key);
        LOGGER.info("size = {}, 耗时= {}ms", size, timer.interval());
        // 操作999次返回999
    }

    @Test
    public void hllTest() {
        TimeInterval timer = DateUtil.timer();
        String key = "pv_hll:20211220";
        // 模拟1000次操作
        for (int i = 1; i < 1000; i++) {
            redisTemplate.opsForHyperLogLog().add(key, String.valueOf(i));
        }
        Long size = redisTemplate.opsForHyperLogLog().size(key);
        LOGGER.info("size = {}, 耗时= {}ms", size, timer.interval());
        // 操作999次返回996
    }

}

