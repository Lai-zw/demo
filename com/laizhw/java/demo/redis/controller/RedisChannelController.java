package demo.redis.controller;

import demo.redis.RedisChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 作者：热黄油啤酒
 * 链接：https://juejin.cn/post/7043724302746648583
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
@RestController
@RequestMapping("redis-channel")
public class RedisChannelController {

    @Autowired
    private RedisChannel redisChannel;

    @GetMapping("send")
    public void send(String msg) {
        redisChannel.send(msg);
    }
}
