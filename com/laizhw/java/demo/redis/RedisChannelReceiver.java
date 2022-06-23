package demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 消息接收处理
 * <p>
 * 作者：热黄油啤酒
 * 链接：https://juejin.cn/post/7043724302746648583
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
@Component
public class RedisChannelReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisChannelReceiver.class);

    public void receiver(String msg) {
        // TODO 消息处理业务
        LOGGER.info("receiver msg = {}", msg);
    }

}
