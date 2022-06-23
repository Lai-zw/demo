package demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 通常我们使用scan是为了替换keys，keys命令执行时候会引发Redis锁，
 * 导致Redis操作大面积阻塞，所以Redis提供scan命令，不会阻塞主线程，
 * 支持按游标分批次返回数据，是比较理想的选择，缺点就是scan有可能返回重复数据，
 * 我们需要进行去重，这个在java里面使用Set接收返回值就ok了。
 * <p>
 * 作者：热黄油啤酒
 * 链接：https://juejin.cn/post/7043724302746648583
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
@Component
public class RedisScan {

    private final Random random = new Random();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisScan.class);

    /**
     * 获取一批指定前缀的key eg: key:* 获取所有key:开头的key
     *
     * @param pattern key匹配正则
     * @param count   一次获取数目
     */
    public Set<String> scan(String pattern, int count) {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions()
                    .match(pattern)
                    .count(count).build())) {
                while (cursor.hasNext()) {
                    keysTmp.add(new String(cursor.next(), StandardCharsets.UTF_8));
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            return keysTmp;
        });
    }

    /**
     * 批量删除
     *
     * @param pattern key匹配正则
     * @param step    阶梯删除的数目
     */
    public void batchDelete(String pattern, int step) {
        while (scan(pattern, step).size() > 0) {
            Set<String> keys = scan(pattern, step);
            // List<byte[]> list = keys.stream()
            //         .map(key -> key.getBytes(StandardCharsets.UTF_8))
            //         .collect(Collectors.toList());
            // // 转为二维数组 byte[][]
            // final byte[][] rawKeys = new byte[list.size()][];
            // int i = 0;
            // for (byte[] key : list) {
            //     rawKeys[i++] = key;
            // }
            // redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            //     connection.del(rawKeys);
            //     return null;
            // });
            redisTemplate.delete(keys);
        }
    }

    /**
     * 模拟指定数量的数据
     *
     * @param keyPrefix 前缀
     * @param count     数量
     */
    public void mock(String keyPrefix, int count) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < count; i++) {
            map.put(keyPrefix + i, i + "-" + 1024 * 1024 + "-" + random.nextInt(1024 * 1024));
        }
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 批量添加数据
     *
     * @param keyPrefix 前缀
     * @param count     数量
     */
    public void mockByPipe(String keyPrefix, int count) {
        Map<byte[], byte[]> map = new HashMap<>();
        for (int i = 0; i < count; i++) {
            map.put((keyPrefix + i).getBytes(StandardCharsets.UTF_8),
                    (i + "-" + random.nextInt(1000000)).getBytes(StandardCharsets.UTF_8));
        }
        // 调用 executePipelined(RedisCallback)，需要使用回调的连接进行 Redis 调用，不能直接使用 redisTemplate 调用，否则 pipeline 不生效
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.mSet(map);
            return null;
        });
    }
}
