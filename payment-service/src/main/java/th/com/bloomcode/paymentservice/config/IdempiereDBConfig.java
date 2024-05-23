package th.com.bloomcode.paymentservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "idemEntityManagerFactory",
        basePackages = {"th.com.bloomcode.paymentservice.idem.dao"}
)
public class IdempiereDBConfig {
    @Bean(name = "idemDataSource")
    @ConfigurationProperties(prefix = "idem.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "idemEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("idemDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("th.com.bloomcode.paymentservice.idem.entity")
                .persistenceUnit("idem")
                .build();
    }

    @Bean(name = "idemTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("idemEntityManagerFactory") EntityManagerFactory
                    entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
