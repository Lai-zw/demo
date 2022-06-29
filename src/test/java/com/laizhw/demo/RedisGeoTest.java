package com.laizhw.demo;

import com.laizhw.demo.redis.RedisGeo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 作者：热黄油啤酒
 * 链接：https://juejin.cn/post/7043724302746648583
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class RedisGeoTest extends DemoApplicationTests {
    @Autowired
    private RedisGeo redisGeo;

    @Test
    public void addTest() {
        // 添加一些城市点位
        redisGeo.add("北京", 116.405285, 39.904989);
        redisGeo.add("武汉", 114.311582, 30.598467);
        redisGeo.add("郑州", 113.631419, 34.753439);
        redisGeo.add("广州", 113.271431, 23.135336);
        redisGeo.add("南宁", 108.373451, 22.822607);
    }

    @Test
    public void distanceTest() {
        // 北京到武汉的距离
        double distance = redisGeo.distance("北京", "武汉");
        LOGGER.info("distance = {}km", distance);
    }

    @Test
    public void circumTest() {
        // 北京周边1000km的城市
        List<Map<String, Object>> circumCity = redisGeo.circum("北京", 1000);
        LOGGER.info("circum city = {}", circumCity);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisGeoTest.class);
}
