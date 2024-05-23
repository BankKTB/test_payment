package th.com.bloomcode.paymentservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;
import th.com.bloomcode.paymentservice.config.property.RedisPortalProperty;

@Configuration
@RequiredArgsConstructor
public class RedisPortalConfiguration {

    private final RedisPortalProperty redisPortalProperty;

    private JedisPoolConfig getPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(8);
        jedisPoolConfig.setMinIdle(1);
        jedisPoolConfig.setMaxTotal(8);
        return jedisPoolConfig;
    }

    @Bean(name = "redisPortalConnectionFactory")
    public RedisConnectionFactory roleRedisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
        redisConnectionFactory.setHostName(redisPortalProperty.getHost());
        redisConnectionFactory.setPort(redisPortalProperty.getPort());
        redisConnectionFactory.setDatabase(redisPortalProperty.getDatabase());
        redisConnectionFactory.setPoolConfig(getPoolConfig());
        return redisConnectionFactory;
    }

    @Bean(name = "redisPortalStringRedisTemplate")
    public StringRedisTemplate roleStringRedisTemplate(@Qualifier("redisPortalConnectionFactory") RedisConnectionFactory cf) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(cf);
        return stringRedisTemplate;
    }

    @Bean(name = "redisPortalRedisTemplate")
    public RedisTemplate roleRedisTemplate(@Qualifier("redisPortalConnectionFactory") RedisConnectionFactory cf) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(cf);
        //setSerializer(stringRedisTemplate);
        return stringRedisTemplate;
    }
}
