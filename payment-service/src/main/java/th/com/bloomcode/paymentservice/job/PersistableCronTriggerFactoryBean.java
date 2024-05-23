package th.com.bloomcode.paymentservice.job;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

import java.text.ParseException;

public class PersistableCronTriggerFactoryBean extends CronTriggerFactoryBean {
	
	public static final String JOB_DETAIL_KEY = "jobDetail";
	
    @Override
    public void afterPropertiesSet() throws ParseException {
        super.afterPropertiesSet();

        getJobDataMap().remove(JOB_DETAIL_KEY);
    }
}