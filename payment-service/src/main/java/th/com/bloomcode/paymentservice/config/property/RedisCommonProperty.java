package th.com.bloomcode.paymentservice.config.property;

import lombok.Data;

@Data
public class RedisCommonProperty {
    private String host;
    private int port;
    private int database;
    private int timeout;
}
