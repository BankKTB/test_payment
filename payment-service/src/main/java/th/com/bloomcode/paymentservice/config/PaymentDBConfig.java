package th.com.bloomcode.paymentservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "paymentEntityManagerFactory",
        basePackages = {"th.com.bloomcode.paymentservice.payment.dao"},
        transactionManagerRef = "paymentTransactionManager"
)
public class PaymentDBConfig {
    private Map<String, Object> properties;

    public PaymentDBConfig() {
        properties = new HashMap<>();
        properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        properties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());

        properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
//        properties.put("hibernate.hbm2ddl.auto","update");
        properties.put("hibernate.jdbc.batch_size", "50");
        properties.put("hibernate.order_inserts", "true");
//    properties.put("hibernate.generate_statistics", "true");
        properties.put("hibernate.jdbc.batch_versioned_data", "true");
    }

    @Primary
    @Bean(name = "paymentDataSource")
    @ConfigurationProperties(prefix = "payment.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "paymentEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("paymentDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("th.com.bloomcode.paymentservice.payment.entity")
                .properties(properties)
                .persistenceUnit("payment")
                .build();
    }

    @Primary
    @Bean(name = "paymentTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("paymentEntityManagerFactory") EntityManagerFactory
                    entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
