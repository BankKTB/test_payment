package th.com.bloomcode.paymentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import th.com.bloomcode.paymentservice.support.AuditorAwareImpl;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
public class JpaConfiguration {

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
