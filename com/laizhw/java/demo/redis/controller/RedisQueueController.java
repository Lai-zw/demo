package demo.redis.controller;

import demo.redis.RedisQueueOfList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基于Redis List的消息队列简单来说就是列表push, 消费者循环pop,
 * 可以小规模使用，消息量的场景建议使用更加专业的消息队列中间件（kafka、rocketmq...）
 * <p>
 * 作者：热黄油啤酒
 * 链接：https://juejin.cn/post/7043724302746648583
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
@RestController
@RequestMapping("redis-queue")
public class RedisQueueController {

    @Autowired
    private RedisQueueOfList queueOfList;

    @GetMapping("/send")
    public void send(String msg) {
        queueOfList.send(msg);
    }
}
