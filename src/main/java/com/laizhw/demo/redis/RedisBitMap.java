package com.laizhw.demo.redis;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 1.基于最小的单位bit进行存储，所以非常省空间。
 * 2.设置时候时间复杂度O(1)、读取时候时间复杂度O(n)，操作是非常快的。
 * 3.二进制数据的存储，进行相关计算的时候非常快。
 * 4.方便扩容
 * <p>
 * 作者：热黄油啤酒
 * 链接：https://juejin.cn/post/7043724302746648583
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
@Component
public class RedisBitMap {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String ONLINE_STATUS = "online_status";

    /**
     * 设置在线状态
     *
     * @param userId
     * @param online
     */
    public void setOnlineStatus(int userId, boolean online) {
        redisTemplate.opsForValue().setBit(ONLINE_STATUS, userId, online);
    }

    /**
     * 统计在线人数
     *
     * @return
     */
    public Long getOnlineCount() {
        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(ONLINE_STATUS.getBytes()));
    }
}