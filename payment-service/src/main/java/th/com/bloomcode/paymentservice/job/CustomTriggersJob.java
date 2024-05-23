package th.com.bloomcode.paymentservice.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.payment.entity.CustomTriggers;
import th.com.bloomcode.paymentservice.service.CustomTriggersSchedulerService;
import th.com.bloomcode.paymentservice.service.PaymentService;
import th.com.bloomcode.paymentservice.service.payment.PaymentAliasService;
import th.com.bloomcode.paymentservice.service.payment.PrepareProposalDocumentService;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.util.Date;

@Slf4j
public class CustomTriggersJob implements Job, InterruptableJob {

    @Autowired
    private CustomTriggersSchedulerService customTriggersSchedulerService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PrepareProposalDocumentService prepareProposalDocumentService;

    @Autowired
    private PaymentAliasService paymentAliasService;

    private volatile Thread thread;
    private volatile Boolean isJobInterrupted = false;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final long startTime = System.currentTimeMillis();
        this.thread = Thread.currentThread();
        Boolean isFail = false;
        JobKey jobKey = context.getJobDetail().getKey();
        /* Get customTriggers data recorded by scheduler during scheduling customTriggers */
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String id = dataMap.getString("id");
        String username = dataMap.getString("triggerBy");
        String chain = dataMap.getString("chain");
        String chainId = dataMap.getString("chainId");

        /* unschedule or delete after job is executed */
        try {
            context.getScheduler().deleteJob(new JobKey(id));

            TriggerKey triggerKey = new TriggerKey(id);

            context.getScheduler().unscheduleJob(triggerKey);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        /* Get customTriggers data from database by id */
        CustomTriggers customTriggers = customTriggersSchedulerService.getCustomTriggers(Long.valueOf(id));
        PaymentAlias paymentAlias = paymentAliasService.findOneById(customTriggers.getPaymentAliasId());
        try{
            log.info("Executing job for id {}", id);
            log.info("payment alias : {}", customTriggers.getPaymentAlias());
            customTriggers.setState("RUNNING");
            customTriggers.setStatus("");
            customTriggers.setStartAtInMillis(startTime);
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
            if (customTriggers.getPaymentType() == 0) {
                prepareProposalDocumentService.proposalJob(paymentAlias, wsWebInfo);
            } else {
                if ("S".equalsIgnoreCase(customTriggers.getPaymentAlias().getProposalStatus()) && customTriggers.getPaymentAlias().getProposalTotalDocument() > 0) {
                    prepareProposalDocumentService.realRunJob(paymentAlias, wsWebInfo);
                } else {
                    isFail = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (isJobInterrupted || isFail) {
                log.info("Job " + jobKey + " did not complete");
                customTriggers.setState("CANCELLED");
                customTriggers.setStatus("FAIL");

                if (customTriggers.getPaymentType() == 1) {
                    paymentAlias.setRunJobStatus("E");
                } else {
                    paymentAlias.setProposalJobStatus("S");
                }
//                customTriggers.setDuration((System.nanoTime() - startTime) / 1000000000);
//                customTriggers.setEndAtInMillis(System.currentTimeMillis());
            } else {
                if (customTriggers.getPaymentType() == 0) {
                    log.info("Job " + jobKey + " completed at " + new Date());
                    long endAtInMillis = System.currentTimeMillis();
                    customTriggers.setState("FINISHED");
                    customTriggers.setStatus("SUCCESS");
                    customTriggers.setDuration((endAtInMillis - startTime) / 1000);
                    customTriggers.setEndAtInMillis(endAtInMillis);

                    if (customTriggers.getPaymentType() == 1) {
                        paymentAlias.setRunJobStatus("S");
                    } else {
                        paymentAlias.setProposalJobStatus("S");
                    }
                }
            }
            /* update customTriggers status in database */
            customTriggersSchedulerService.updateCustomTriggers(customTriggers);
            paymentAliasService.save(paymentAlias);

            if ("Y".equalsIgnoreCase(chain) && Integer.parseInt(chainId) > 0) {
                doChain(context);
            }
        }
    }

    private void doChain(JobExecutionContext context) {
        final long startTime = System.nanoTime();
        this.thread = Thread.currentThread();
        Boolean isFail = false;
        JobKey jobKey = context.getJobDetail().getKey();
        /* Get customTriggers data recorded by scheduler during scheduling customTriggers */
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String username = dataMap.getString("triggerBy");
        String chainId = dataMap.getString("chainId");
        /* Get customTriggers data from database by id */
        CustomTriggers customTriggers = customTriggersSchedulerService.getCustomTriggers(Long.valueOf(chainId));
        PaymentAlias paymentAlias = paymentAliasService.findOneById(customTriggers.getPaymentAliasId());
        CustomTriggers parentCustomTriggers = customTriggersSchedulerService.getOne(customTriggers.getParentId());
        try{
            log.info("Executing job for id {}", chainId);
            log.info("payment alias : {}", customTriggers.getPaymentAlias());
            customTriggers.setState("RUNNING");
            customTriggers.setStatus("");
            customTriggers.setUpdatedBy(username);
            if (null != parentCustomTriggers) {
                customTriggers.setTriggerAtInMillis(parentCustomTriggers.getEndAtInMillis());
            }

            /* update customTriggers status in database */
            customTriggersSchedulerService.updateCustomTriggers(customTriggers);

            WSWebInfo wsWebInfo = new WSWebInfo();
            wsWebInfo.setIpAddress("127.0.0.1");
            wsWebInfo.setUserWebOnline(username);
            wsWebInfo.setFiArea("1000");
            wsWebInfo.setCompCode("99999");
            wsWebInfo.setPaymentCenter("9999999999");
            /* Running process */
            if (customTriggers.getPaymentType() == 0) {
                prepareProposalDocumentService.proposalJob(paymentAlias, wsWebInfo);
            } else {
                log.info("proposal status : {} >>> total doc : {}", paymentAlias.getProposalStatus(), paymentAlias.getProposalTotalDocument());
                if ("S".equalsIgnoreCase(paymentAlias.getProposalStatus()) && paymentAlias.getProposalTotalDocument() > 0) {
                    prepareProposalDocumentService.realRunJob(paymentAlias, wsWebInfo);
                } else {
                    isFail = true;
                }
            }

            /* unschedule or delete after job is executed */
            try {
                context.getScheduler().deleteJob(new JobKey(chainId));

                TriggerKey triggerKey = new TriggerKey(chainId);

                context.getScheduler().unscheduleJob(triggerKey);

            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (isJobInterrupted || isFail) {
                log.info("Job " + jobKey + " did not complete");
                customTriggers.setState("CANCELLED");
                customTriggers.setStatus("FAIL");

                if (customTriggers.getPaymentType() == 1) {
                    paymentAlias.setRunJobStatus("E");
                }
//                customTriggers.setDuration((System.nanoTime() - startTime) / 1000000000);
//                customTriggers.setEndAtInMillis(System.currentTimeMillis());

            } else {
                if (customTriggers.getPaymentType() == 0) {
                    log.info("Job " + jobKey + " completed at " + new Date());
                    Long endAtInMillis = System.currentTimeMillis();
                    customTriggers.setState("FINISHED");
                    customTriggers.setStatus("SUCCESS");
                    customTriggers.setDuration((endAtInMillis - customTriggers.getTriggerAtInMillis()) / 1000);
                    customTriggers.setEndAtInMillis(endAtInMillis);

                    if (customTriggers.getPaymentType() == 1) {
                        paymentAlias.setRunJobStatus("S");
                    } else {
                        paymentAlias.setProposalJobStatus("S");
                    }
                }
            }
            /* update customTriggers status in database */
            customTriggersSchedulerService.updateCustomTriggers(customTriggers);
            paymentAliasService.save(paymentAlias);
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        if (null != this.thread) {
            this.isJobInterrupted = true;
            this.thread.interrupt();
        }
    }
}
