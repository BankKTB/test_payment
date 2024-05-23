package th.com.bloomcode.paymentservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;
import th.com.bloomcode.paymentservice.config.property.RedisFCProperty;

@Configuration
@RequiredArgsConstructor
public class RedisFCConfiguration {

    private final RedisFCProperty redisFCProperty;

    @Primary
    @Bean(name = "redisFCConnectionFactory")
    public RedisConnectionFactory userRedisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
        redisConnectionFactory.setHostName(redisFCProperty.getHost());
        redisConnectionFactory.setPort(redisFCProperty.getPort());
        redisConnectionFactory.setDatabase(redisFCProperty.getDatabase());
        redisConnectionFactory.setPoolConfig(getPoolConfig());
        return redisConnectionFactory;
    }

    private JedisPoolConfig getPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(8);
        jedisPoolConfig.setMinIdle(1);
        jedisPoolConfig.setMaxTotal(8);
        return jedisPoolConfig;
    }

    @Bean(name = "redisFCStringRedisTemplate")
    public StringRedisTemplate userStringRedisTemplate(@Qualifier("redisFCConnectionFactory") RedisConnectionFactory cf) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(cf);
        return stringRedisTemplate;
    }

    @Bean(name = "redisFCRedisTemplate")
    public RedisTemplate userRedisTemplate(@Qualifier("redisFCConnectionFactory") RedisConnectionFactory cf) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(cf);
        //setSerializer(stringRedisTemplate);
        return stringRedisTemplate;
    }
}
