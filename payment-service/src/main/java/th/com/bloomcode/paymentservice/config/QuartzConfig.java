package th.com.bloomcode.paymentservice.config;

import org.quartz.spi.JobFactory;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import th.com.bloomcode.paymentservice.factory.AutowiringSpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class QuartzConfig {

    private final DataSource dataSource;

    private final ApplicationContext applicationContext;

    private final QuartzProperties quartzProperties;

    public QuartzConfig(DataSource dataSource, ApplicationContext applicationContext, QuartzProperties quartzProperties) {
        this.dataSource = dataSource;
        this.applicationContext = applicationContext;
        this.quartzProperties = quartzProperties;
    }

    @Bean
    public JobFactory jobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setQuartzProperties(properties);
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactory.setAutoStartup(true);
        schedulerFactory.setJobFactory(jobFactory());
        return schedulerFactory;
    }

}
