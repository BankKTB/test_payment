package th.com.bloomcode.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJms
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
public class PaymentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PaymentServiceApplication.class, args);
  }
}
