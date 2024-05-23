package th.com.bloomcode.paymentservice.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.payment.entity.CustomTriggers;
import th.com.bloomcode.paymentservice.service.CustomTriggersSchedulerService;
import th.com.bloomcode.paymentservice.service.FileTransferService;
import th.com.bloomcode.paymentservice.service.payment.ReturnService;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.util.Date;

@Slf4j
public class CustomTriggerReturnJob implements Job, InterruptableJob {
    private volatile Thread thread;
    private volatile Boolean isJobInterrupted = false;

    @Autowired
    private CustomTriggersSchedulerService customTriggersSchedulerService;

    @Autowired
    private FileTransferService fileTransferService;

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        if (null != this.thread) {
            this.isJobInterrupted = true;
            this.thread.interrupt();
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.thread = Thread.currentThread();
        Boolean isFail = false;
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        /* Get customTriggers data recorded by scheduler during scheduling customTriggers */
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        String id = dataMap.getString("id");
        String username = dataMap.getString("triggerBy");
        /* Get customTriggers data from database by id */
        CustomTriggers customTriggers = customTriggersSchedulerService.getCustomTriggers(Long.valueOf(id));
        try{
            log.info("Executing job for id {}", id);
            log.info("payment alias : {}", customTriggers.getPaymentAlias());
            customTriggers.setState("RUNNING");
            customTriggers.setStatus("");
            customTriggers.setUpdatedBy(username);

            /* update customTriggers status in database */
            customTriggersSchedulerService.updateCustomTriggers(customTriggers);

            WSWebInfo wsWebInfo = new WSWebInfo();
            wsWebInfo.setIpAddress("127.0.0.1");
            wsWebInfo.setUserWebOnline(username);
            wsWebInfo.setFiArea("1000");
            wsWebInfo.setCompCode("99999");
            wsWebInfo.setPaymentCenter("9999999999");
            /* Running process */
            fileTransferService.downloadFileJob(username);

            /* unschedule or delete after job is executed */
            try {
                jobExecutionContext.getScheduler().deleteJob(new JobKey(id));

                TriggerKey triggerKey = new TriggerKey(id);

                jobExecutionContext.getScheduler().unscheduleJob(triggerKey);

            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (isJobInterrupted) {
                log.info("Job " + jobKey + " did not complete");
                customTriggers.setState("CANCELLED");
                customTriggers.setStatus("FAIL");
            } else {
                log.info("Job " + jobKey + " completed at " + new Date());
                Long endAtInMillis = System.currentTimeMillis();
                customTriggers.setState("FINISHED");
                customTriggers.setStatus("SUCCESS");
                customTriggers.setDuration((endAtInMillis - customTriggers.getTriggerAtInMillis()) / 1000);
                customTriggers.setEndAtInMillis(endAtInMillis);
            }
            /* update customTriggers status in database */
            customTriggersSchedulerService.updateCustomTriggers(customTriggers);
        }
    }
}
