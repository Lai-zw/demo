package demo.redis.config;

import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author : Laizhw@
 * @version : v1.0
 * @className : RedisSentinelConfiguration
 */
@Configuration
public class RedisSentinelConfiguration {
    /**
     * Lettuce
     */
    // Lettuce@Bean
    // public RedisConnectionFactory lettuceConnectionFactory() {
    //     RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
    //             .master("mymaster")
    //             .sentinel("127.0.0.1", 26379)
    //             .sentinel("127.0.0.1", 26380);
    //     return new LettuceConnectionFactory(sentinelConfig);
    // }
}
