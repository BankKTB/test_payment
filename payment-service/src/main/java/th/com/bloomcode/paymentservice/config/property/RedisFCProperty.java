package th.com.bloomcode.paymentservice.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis.fc")
public class RedisFCProperty extends RedisCommonProperty {
}
