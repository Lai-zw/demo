package com.laizhw.demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis List的消息队列实现
 * <p>
 * 作者：热黄油啤酒
 * 链接：https://juejin.cn/post/7043724302746648583
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
@Component
public class RedisQueueOfList {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisQueueOfList.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final String TOPIC = "redis_queue";


    /**
     * 发送消息
     *
     * @param msg
     */
    public void send(String msg) {
        redisTemplate.opsForList().leftPush(TOPIC, msg);
    }

    @PostConstruct
    public void listener() {
        LOGGER.info("消费者已启动...");
        new Thread(() -> {
            while (true) {
                String msg = redisTemplate.opsForList().rightPop(TOPIC, 1, TimeUnit.SECONDS);
                if (msg == null) {
                    continue;
                }
                // 业务处理
                LOGGER.info("queue msg = {}", msg);
            }
        }).start();

    }
}
