package th.com.bloomcode.paymentservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.config.QuartzConfig;
import th.com.bloomcode.paymentservice.job.CustomTriggerReturnJob;
import th.com.bloomcode.paymentservice.job.CustomTriggersJob;
import th.com.bloomcode.paymentservice.model.*;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.request.CustomTriggersRequest;
import th.com.bloomcode.paymentservice.payment.dao.CustomTriggersRepository;
import th.com.bloomcode.paymentservice.payment.entity.CustomTriggers;
import th.com.bloomcode.paymentservice.service.CustomTriggersSchedulerService;
import th.com.bloomcode.paymentservice.service.payment.PaymentAliasService;
import th.com.bloomcode.paymentservice.service.payment.PrepareRunDocumentService;
import th.com.bloomcode.paymentservice.util.JobUtil;
import th.com.bloomcode.paymentservice.util.SpecificationUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CustomTriggersSchedulerServiceImpl implements CustomTriggersSchedulerService {

    private final QuartzConfig quartzConfig;

    private final CustomTriggersRepository customTriggersRepository;

    private final PaymentAliasService paymentAliasService;

    private final PrepareRunDocumentService prepareRunDocumentService;

    public CustomTriggersSchedulerServiceImpl(
            QuartzConfig quartzConfig,
            CustomTriggersRepository customTriggersRepository,
            PaymentAliasService paymentAliasService, PrepareRunDocumentService prepareRunDocumentService) {
        this.quartzConfig = quartzConfig;
        this.customTriggersRepository = customTriggersRepository;
        this.paymentAliasService = paymentAliasService;
        this.prepareRunDocumentService = prepareRunDocumentService;
    }

    @Override
    public ResponseEntity<Result<CustomTriggers>> scheduleCustomTriggers(CustomTriggers customTriggers) {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        log.info("Scheduling customTriggers for senderId {}", customTriggers.getId());

        CustomTriggers exist = customTriggersRepository.findOneByPaymentAliasIdAndPaymentType(customTriggers.getPaymentAliasId(), customTriggers.getPaymentType()).orElse(null);
        if (null != exist) {
            return new ResponseEntity<>(new Result<>(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.toString(), customTriggers), HttpStatus.CONFLICT);
        }
        Date runJobAt = new Date(customTriggers.getTriggerAtInMillis());
        runJobAt.setSeconds(0);
        /* Saving payment in database */
        customTriggers.setState("SCHEDULED");
        customTriggers.setCreatedBy(jwt.getSub());
        customTriggers.setUpdatedBy(jwt.getSub());
        customTriggers.setTriggerAtInMillis(runJobAt.getTime());
        customTriggers.setJobType("PAYMENT");
        customTriggers.setJobDate(new Timestamp(runJobAt.getTime()));
        customTriggers = customTriggersRepository.save(customTriggers);
        try {
            Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();

            log.info("Creating job detail ..........");

            // cast id to String
            String id = String.valueOf(customTriggers.getId());

            // create JobDetail
            JobDetail jobDetail = JobBuilder.newJob(CustomTriggersJob.class).withIdentity(id).build();

            // Add data to JobDataMap
            jobDetail.getJobDataMap().put("id", id);
            jobDetail.getJobDataMap().put("triggerBy", jwt.getSub());
            jobDetail.getJobDataMap().put("chain", "N");
            jobDetail.getJobDataMap().put("chainId", "0");

            PaymentAlias paymentAlias =
                    paymentAliasService.findOneById(customTriggers.getPaymentAliasId());
            if (customTriggers.getPaymentType() == 0) {
                paymentAlias.setProposalTriggersId(customTriggers.getId());
            } else {
                paymentAlias.setPaymentTriggersId(customTriggers.getId());
            }
            paymentAliasService.update(paymentAlias);

//      SimpleTrigger trigger =
//          TriggerBuilder.newTrigger()
//              .withIdentity(id)
//              .startAt(runJobAt)
//              .withSchedule(
//                  SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(5).repeatForever())
//              .build();

            SimpleTrigger trigger =
                    TriggerBuilder.newTrigger()
                            .withIdentity(id)
                            .startAt(runJobAt)
                            .withSchedule(
                                    SimpleScheduleBuilder.simpleSchedule())
                            .build();

            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();

            customTriggers.setStatus("SCHEDULED_SUCCESS");
            customTriggers.setState("SCHEDULED");

        } catch (IOException | SchedulerException e) {
            // scheduling failed
            customTriggers.setStatus("SCHEDULED_FAIL");
            customTriggers.setState("FAILED");
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Result<>(HttpStatus.CREATED.value(), HttpStatus.CREATED.toString(), customTriggers), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> unscheduleCustomTriggers(Long id) {

        log.info("Unscheduling payment for id {}", id);

        Scheduler scheduler;
        String paymentIdStringValue = String.valueOf(id);

        try {
            TriggerKey triggerKey = new TriggerKey(paymentIdStringValue);
            scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
            scheduler.deleteJob(new JobKey(paymentIdStringValue));
            scheduler.unscheduleJob(triggerKey);
        } catch (IOException | SchedulerException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Result<CustomTriggers>> updateCustomTriggers(CustomTriggers customTriggers) {

        log.info("Updating payment for id {}", customTriggers.getId());

        return new ResponseEntity<>(new Result<>(HttpStatus.OK.value(), HttpStatus.OK.toString(), customTriggersRepository.save(customTriggers)), HttpStatus.OK);
    }

    @Override
    public CustomTriggers getCustomTriggers(Long id) {
        return customTriggersRepository.findById(id).get();
    }

    @Override
    public CustomTriggers getCustomTriggersByParentId(Long id, String state) {
        return customTriggersRepository.findOneByParentIdAndState(id, state);
    }

    @Override
    public ResponseEntity<Result<CustomTriggers>> getCustomTriggersById(Long id) {

        log.info("Retrieving payment by id {}", id);

        return new ResponseEntity<>(new Result<>(HttpStatus.OK.value(), HttpStatus.OK.toString(), customTriggersRepository.findById(id).get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Result<List<CustomTriggers>>> getAllCustomTriggers() {
        return new ResponseEntity<>(new Result<>(HttpStatus.OK.value(), HttpStatus.OK.toString(), customTriggersRepository.findAll()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Result<List<CustomTriggers>>> getAllWithPredicate(CustomTriggersRequest request) {
        SearchQuery searchQuery = new SearchQuery();
        if (!Util.isEmpty(request.getState())) {
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setKey("state");
            searchCriteria.setValue(request.getState());
            searchCriteria.setOperation(SearchOperation.IN);
            searchQuery.addSearchCriteria(searchCriteria);
        }

        if (!Util.isEmpty(request.getStatus())) {
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setKey("status");
            searchCriteria.setValue(request.getStatus());
            searchCriteria.setOperation(SearchOperation.EQUAL);
            searchQuery.addSearchCriteria(searchCriteria);
        }

        if (!Util.isEmpty(request.getPaymentAliasId())) {
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setKey("paymentAliasId");
            searchCriteria.setValue(request.getPaymentAliasId());
            searchCriteria.setOperation(SearchOperation.EQUAL);
            searchQuery.addSearchCriteria(searchCriteria);
        }

        if (!Util.isEmpty(request.getPaymentType())) {
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setKey("paymentType");
            searchCriteria.setValue(request.getPaymentType());
            searchCriteria.setOperation(SearchOperation.EQUAL);
            searchQuery.addSearchCriteria(searchCriteria);
        }

        if (!Util.isEmpty(request.getJobDate())) {
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setKey("jobDate");
            searchCriteria.setValue(request.getJobDate());
            searchCriteria.setOperation(SearchOperation.EQUAL);
            searchQuery.addSearchCriteria(searchCriteria);
        }

        if (!Util.isEmpty(request.getPaymentName())) {
            SearchCriteria parentSearchCriteria = new SearchCriteria();
            parentSearchCriteria.setKey("paymentName");
            parentSearchCriteria.setValue(request.getPaymentName());
            parentSearchCriteria.setOperation(SearchOperation.EQUAL);
            JoinColumn joinColumn = new JoinColumn();
            joinColumn.setJoinColumnName("paymentAlias");
            joinColumn.setSearchCriteria(parentSearchCriteria);
            searchQuery.addJoinColumn(joinColumn);
        }

        if (!Util.isEmpty(request.getPaymentDate())) {
            SearchCriteria parentSearchCriteria = new SearchCriteria();
            parentSearchCriteria.setKey("paymentDate");
            parentSearchCriteria.setValue(request.getPaymentDate());
            parentSearchCriteria.setOperation(SearchOperation.EQUAL);
            JoinColumn joinColumn = new JoinColumn();
            joinColumn.setJoinColumnName("paymentAlias");
            joinColumn.setSearchCriteria(parentSearchCriteria);
            searchQuery.addJoinColumn(joinColumn);
        }

//    log.info("searchQuery {} ", searchQuery);
        Specification<CustomTriggers> spec =
                SpecificationUtil.bySearchQuery(searchQuery, CustomTriggers.class);
        List<CustomTriggers> customTriggers = customTriggersRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "updated"));
//    log.info("customTriggers : {}", customTriggers);
        if (customTriggers.size() > 0) {
            return new ResponseEntity<>(new Result<>(HttpStatus.OK.value(), HttpStatus.OK.toString(), customTriggers), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Result<>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.toString(), null), HttpStatus.OK);
        }
    }

    @Override
    public List<CustomTriggers> getAll(CustomTriggersRequest request) {
        SearchQuery searchQuery = new SearchQuery();
        if (!Util.isEmpty(request.getState())) {
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setKey("state");
            searchCriteria.setValue(request.getState());
            searchCriteria.setOperation(SearchOperation.IN);
            searchQuery.addSearchCriteria(searchCriteria);
        }

        if (!Util.isEmpty(request.getPaymentType())) {
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setKey("paymentType");
            searchCriteria.setValue(request.getPaymentType());
            searchCriteria.setOperation(SearchOperation.EQUAL);
            searchQuery.addSearchCriteria(searchCriteria);
        }

        Specification<CustomTriggers> spec =
                SpecificationUtil.bySearchQuery(searchQuery, CustomTriggers.class);
        return customTriggersRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "updated"));
    }

    @Override
    public CustomTriggers getOne(Long id) {
        return customTriggersRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<Void> addJob(CustomTriggers customTriggers) throws IOException {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        log.info("Scheduling customTriggers for senderId {}", customTriggers.getId());

        if (customTriggers.getJobType().equalsIgnoreCase("PAYMENT")) {
            if (null != customTriggers.getParentId() && customTriggers.getParentId() > 0) {
                /* Saving payment in database */
                try {
                    // reset first job
                    Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
                    String groupKey = "DEFAULT";
                    JobKey parentJobKey = new JobKey(String.valueOf(customTriggers.getParentId()), groupKey);
                    boolean status = scheduler.deleteJob(parentJobKey);

                    log.info("Creating job detail ..........");

                    CustomTriggers customTriggersParent = getCustomTriggers(customTriggers.getParentId());       // cast id to String
                    String idParent = String.valueOf(customTriggersParent.getId());

                    // create JobDetail
                    JobDetail jobDetailParent = JobBuilder.newJob(CustomTriggersJob.class).withIdentity(idParent).build();

                    // Add data to JobDataMap
                    jobDetailParent.getJobDataMap().put("id", idParent);
                    jobDetailParent.getJobDataMap().put("triggerBy", jwt.getSub());
                    jobDetailParent.getJobDataMap().put("chain", "Y");

                    Date runJobAt = new Date(customTriggersParent.getTriggerAtInMillis());
                    runJobAt.setSeconds(0);

                    SimpleTrigger triggerParent =
                            TriggerBuilder.newTrigger()
                                    .withIdentity(idParent)
                                    .startAt(runJobAt)
                                    .withSchedule(
                                            SimpleScheduleBuilder.simpleSchedule())
                                    .build();

                    log.info("Creating job detail ..........");

                    customTriggers.setState("CHAINED");
                    customTriggers.setTriggerAtInMillis(0L);
                    customTriggers.setCreatedBy(jwt.getSub());
                    customTriggers.setUpdatedBy(jwt.getSub());
                    customTriggers.setJobType("PAYMENT");
                    customTriggers.setJobDate(new Timestamp(runJobAt.getTime()));
                    customTriggers = customTriggersRepository.save(customTriggers);

                    jobDetailParent.getJobDataMap().put("chainId", String.valueOf(customTriggers.getId()));

//        // cast id to String
//        String id = String.valueOf(customTriggers.getId());
//        JobKey jobKey = new JobKey(id, groupKey);
//
//        // create JobDetail
//        JobDetail jobDetail = JobBuilder.newJob(CustomTriggersJob.class).withIdentity(id).storeDurably(true).build();
//        JobChainingJobListener chain = new JobChainingJobListener("chain");
//        chain.addJobChainLink(jobDetailParent.getKey(), jobDetail.getKey());
//
//        // Add data to JobDataMap
//        jobDetail.getJobDataMap().put("id", id);
//        jobDetail.getJobDataMap().put("triggerBy", jwt.getSub());

                    PaymentAlias paymentAlias =
                            paymentAliasService.findOneById(customTriggers.getPaymentAliasId());
                    if (customTriggers.getPaymentType() == 0) {
                        paymentAlias.setProposalJobStatus("W");
                        paymentAlias.setProposalTriggersId(customTriggers.getId());
//          paymentAlias.setProposalScheduleDate(new Timestamp(customTriggersParent.getTriggerAtInMillis()));
                    } else {
                        paymentAlias.setRunJobStatus("A");
                        paymentAlias.setPaymentTriggersId(customTriggers.getId());
//          paymentAlias.setRunScheduleDate(new Timestamp(customTriggersParent.getTriggerAtInMillis()));
                    }
                    paymentAliasService.save(paymentAlias);

//        scheduler.getListenerManager().addJobListener(chain);
//        scheduler.addJob(jobDetail, true);
                    scheduler.scheduleJob(jobDetailParent, triggerParent);
                    scheduler.start();
                    customTriggers.setStatus("SCHEDULED_SUCCESS");
                    customTriggers.setState("SCHEDULED");

                } catch (IOException | SchedulerException e) {
                    // scheduling failed
                    customTriggers.setStatus("SCHEDULED_FAIL");
                    customTriggers.setState("FAILED");
                    e.printStackTrace();
                }
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                /* Saving payment in database */
                Date runJobAt = new Date(customTriggers.getTriggerAtInMillis());
                runJobAt.setSeconds(0);
                customTriggers.setState("SCHEDULED");
                customTriggers.setCreatedBy(jwt.getSub());
                customTriggers.setUpdatedBy(jwt.getSub());
                customTriggers.setTriggerAtInMillis(runJobAt.getTime());
                customTriggers.setJobType("PAYMENT");
                customTriggers.setJobDate(new Timestamp(runJobAt.getTime()));
                customTriggers = customTriggersRepository.save(customTriggers);
                try {
                    Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();

                    log.info("Creating job detail ..........");

                    // cast id to String
                    String id = String.valueOf(customTriggers.getId());

                    // create JobDetail
                    JobDetail jobDetail = JobBuilder.newJob(CustomTriggersJob.class).withIdentity(id).build();

                    // Add data to JobDataMap
                    jobDetail.getJobDataMap().put("id", id);
                    jobDetail.getJobDataMap().put("triggerBy", jwt.getSub());
                    jobDetail.getJobDataMap().put("chain", "N");
                    jobDetail.getJobDataMap().put("chainId", "0");


                    PaymentAlias paymentAlias =
                            paymentAliasService.findOneById(customTriggers.getPaymentAliasId());
                    if (customTriggers.getPaymentType() == 0) {
                        paymentAlias.setProposalJobStatus("W");
                        paymentAlias.setProposalTriggersId(customTriggers.getId());
                        paymentAlias.setProposalScheduleDate(new Timestamp(customTriggers.getTriggerAtInMillis()));
                    } else {
                        paymentAlias.setRunJobStatus("W");
                        paymentAlias.setPaymentTriggersId(customTriggers.getId());
                        paymentAlias.setRunScheduleDate(new Timestamp(customTriggers.getTriggerAtInMillis()));
                    }
                    paymentAliasService.update(paymentAlias);

                    SimpleTrigger trigger =
                            TriggerBuilder.newTrigger()
                                    .withIdentity(id)
                                    .startAt(runJobAt)
                                    .withSchedule(
                                            SimpleScheduleBuilder.simpleSchedule())
                                    .build();

                    scheduler.scheduleJob(jobDetail, trigger);
                    scheduler.start();

                    customTriggers.setStatus("SUCCESS");
                    customTriggers.setState("SCHEDULED");

                } catch (IOException | SchedulerException e) {
                    // scheduling failed
                    customTriggers.setStatus("FAIL");
                    customTriggers.setState("FAILED");
                    e.printStackTrace();
                }
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        } else {
            CustomTriggers exist = customTriggersRepository.findOneByJobTypeAndState("RETURN", "SCHEDULED").orElse(null);
            if (null != exist) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            Date runJobAt = new Date(customTriggers.getTriggerAtInMillis());
            runJobAt.setSeconds(0);
            customTriggers.setState("SCHEDULED");
            customTriggers.setCreatedBy(jwt.getSub());
            customTriggers.setUpdatedBy(jwt.getSub());
            customTriggers.setPaymentType(1L);
            customTriggers.setTriggerAtInMillis(runJobAt.getTime());
            customTriggers.setJobDate(new Timestamp(runJobAt.getTime()));
            customTriggers = customTriggersRepository.save(customTriggers);
            try {
                Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();

                log.info("Creating job detail ..........");

                // cast id to String
                String id = String.valueOf(customTriggers.getId());

                // create JobDetail
                JobDetail jobDetail = JobBuilder.newJob(CustomTriggerReturnJob.class).withIdentity(id).build();

                // Add data to JobDataMap
                jobDetail.getJobDataMap().put("id", id);
                jobDetail.getJobDataMap().put("triggerBy", jwt.getSub());


                SimpleTrigger trigger =
                        TriggerBuilder.newTrigger()
                                .withIdentity(id)
                                .startAt(runJobAt)
                                .withSchedule(
                                        SimpleScheduleBuilder.simpleSchedule())
                                .build();

                scheduler.scheduleJob(jobDetail, trigger);
                scheduler.start();

                customTriggers.setStatus("SUCCESS");
                customTriggers.setState("SCHEDULED");

            } catch (IOException | SchedulerException e) {
                // scheduling failed
                customTriggers.setStatus("FAIL");
                customTriggers.setState("FAILED");
                e.printStackTrace();
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Void> addJobRunNow(CustomTriggers customTriggers) throws IOException {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        log.info("Scheduling customTriggers for senderId {}", customTriggers.getId());
        log.info("Scheduling customTriggers for payment alias {}", customTriggers.getPaymentAliasId());
        CustomTriggers exist = customTriggersRepository.findOneByPaymentAliasIdAndPaymentType(customTriggers.getPaymentAliasId(), customTriggers.getPaymentType()).orElse(null);
        if (null != exist) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (null != customTriggers.getParentId() && customTriggers.getParentId() > 0) {
            /* Saving payment in database */
            try {
                // reset first job
                Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
                String groupKey = "DEFAULT";
                JobKey parentJobKey = new JobKey(String.valueOf(customTriggers.getParentId()), groupKey);
                boolean status = scheduler.deleteJob(parentJobKey);

                log.info("Creating job detail ..........");

                CustomTriggers customTriggersParent = getCustomTriggers(customTriggers.getParentId());       // cast id to String
                String idParent = String.valueOf(customTriggersParent.getId());

                Date runJobAt = new Date(customTriggersParent.getTriggerAtInMillis());
                runJobAt.setSeconds(0);

                // create JobDetail
                JobDetail jobDetailParent = JobBuilder.newJob(CustomTriggersJob.class).withIdentity(idParent).build();

                // Add data to JobDataMap
                jobDetailParent.getJobDataMap().put("id", idParent);
                jobDetailParent.getJobDataMap().put("triggerBy", jwt.getSub());
                jobDetailParent.getJobDataMap().put("chain", "Y");

                Trigger triggerParent =
                        TriggerBuilder.newTrigger()
                                .forJob(jobDetailParent)
                                .withIdentity(idParent)
                                .startNow()
                                .build();

                log.info("Creating job detail ..........");

                customTriggers.setState("CHAINED");
                customTriggers.setTriggerAtInMillis(0L);
                customTriggers.setCreatedBy(jwt.getSub());
                customTriggers.setUpdatedBy(jwt.getSub());
                customTriggers.setJobType("PAYMENT");
                customTriggers.setJobDate(new Timestamp(runJobAt.getTime()));
                customTriggers = customTriggersRepository.save(customTriggers);

                jobDetailParent.getJobDataMap().put("chainId", String.valueOf(customTriggers.getId()));

                PaymentAlias paymentAlias =
                        paymentAliasService.findOneById(customTriggers.getPaymentAliasId());
                if (customTriggers.getPaymentType() == 0) {
                    paymentAlias.setProposalTriggersId(customTriggers.getId());
                } else {
                    paymentAlias.setPaymentTriggersId(customTriggers.getId());
                }
                paymentAliasService.update(paymentAlias);

                scheduler.scheduleJob(jobDetailParent, triggerParent);
                scheduler.start();
                customTriggers.setStatus("SUCCESS");
                customTriggers.setState("RUNNING");

            } catch (IOException | SchedulerException e) {
                // scheduling failed
                customTriggers.setStatus("FAIL");
                customTriggers.setState("FAILED");
                e.printStackTrace();
            }
        } else {
            PaymentAlias paymentAlias = paymentAliasService.findOneById(customTriggers.getPaymentAliasId());

            Date runJobAt = new Date();
//            runJobAt.setSeconds(0);

            /* Saving payment in database */
            customTriggers.setState("SCHEDULED");
            customTriggers.setStatus("SUCCESS");
            customTriggers.setCreatedBy(jwt.getSub());
            customTriggers.setUpdatedBy(jwt.getSub());
            customTriggers.setTriggerAtInMillis(runJobAt.getTime());
            customTriggers.setJobType("PAYMENT");
            customTriggers.setJobDate(new Timestamp(runJobAt.getTime()));
            customTriggers = customTriggersRepository.save(customTriggers);

            if (customTriggers.getPaymentType() == 0) {
                if (null == paymentAlias.getProposalStatus()) {
                    paymentAlias.setProposalTriggersId(customTriggers.getId());
                    paymentAlias.setProposalStatus("WP");
                    paymentAliasService.save(paymentAlias);
                }
            } else {
                if (null == paymentAlias.getRunStatus()) {
                    paymentAlias.setPaymentTriggersId(customTriggers.getId());
//                    paymentAlias.setRunStart(new Timestamp(System.currentTimeMillis()));

                    paymentAlias.setRunStatus("P");
                    paymentAliasService.save(paymentAlias);
                }
            }
            try {
                Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();

                log.info("Creating job detail ..........");

                // cast id to String
                String id = String.valueOf(customTriggers.getId());

                // create JobDetail
                JobDetail jobDetail = JobBuilder.newJob(CustomTriggersJob.class).withIdentity(id).build();

                // Add data to JobDataMap
                jobDetail.getJobDataMap().put("id", id);
                jobDetail.getJobDataMap().put("triggerBy", jwt.getSub());
                jobDetail.getJobDataMap().put("chain", "N");
                jobDetail.getJobDataMap().put("chainId", "0");

                Trigger trigger =
                        TriggerBuilder.newTrigger()
                                .forJob(jobDetail)
                                .withIdentity(id)
                                .startNow()
                                .build();

                scheduler.scheduleJob(jobDetail, trigger);
                scheduler.start();

            } catch (IOException | SchedulerException e) {
                // scheduling failed
                customTriggers.setStatus("FAIL");
                customTriggers.setState("FAILED");
                customTriggersRepository.save(customTriggers);
                e.printStackTrace();
            }

        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> updateJob(Long id, CustomTriggers request) throws IOException {
        Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
        log.info("Request received for updating one time job.");
        String jobKey = String.valueOf(request.getId());
        log.info(
                "Parameters received for updating one time job : jobKey :"
                        + id
                        + ", date: "
                        + new Date(request.getTriggerAtInMillis()));
        try {
            // Trigger newTrigger = JobUtil.createSingleTrigger(jobKey, new
            // Timestamp(request.getTriggerAtInMillis()),
            // SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
            Trigger newTrigger =
                    JobUtil.createSingleTrigger(
                            jobKey,
                            new Timestamp(request.getTriggerAtInMillis()),
                            SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

            Date dt = scheduler
                    .rescheduleJob(TriggerKey.triggerKey(jobKey), newTrigger);
            log.info(
                    "Trigger associated with jobKey :"
                            + jobKey
                            + " rescheduled successfully for date :"
                            + dt);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(
                    "SchedulerException while updating one time job with key :"
                            + jobKey
                            + " message :"
                            + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> deleteJob(Long id) throws IOException {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
        log.info("Request received for deleting job.");
        CustomTriggers customTriggersChild = getCustomTriggersByParentId(id, "CHAINED");
        if (null != customTriggersChild) {
            customTriggersRepository.delete(customTriggersChild);

            String jobKey = String.valueOf(customTriggersChild.getId());
            String groupKey = "DEFAULT";

            JobKey jkey = new JobKey(jobKey, groupKey);
            log.info("Parameters received for deleting job : jobKey :" + jobKey);

            try {
                boolean status = scheduler.deleteJob(jkey);
                log.info("Job with jobKey :" + jobKey + " deleted with status :" + status);
                PaymentAlias paymentAlias =
                        paymentAliasService.findOneById(customTriggersChild.getPaymentAliasId());
                if (customTriggersChild.getPaymentType() == 0) {
                    paymentAlias.setProposalTriggersId(0L);
                    paymentAlias.setProposalScheduleDate(null);
                    paymentAlias.setProposalJobStatus(null);
                } else {
                    paymentAlias.setPaymentTriggersId(0L);
                    paymentAlias.setRunScheduleDate(null);
                    paymentAlias.setRunJobStatus(null);
                }
                paymentAliasService.update(paymentAlias);
            } catch (SchedulerException e) {
                log.error("SchedulerException while deleting child job with key :" + jobKey + " message :" + e.getMessage());
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        CustomTriggers customTriggers = getCustomTriggers(id);
        customTriggers.setState("INACTIVE");
        customTriggers.setUpdatedBy(jwt.getSub());
        customTriggers.setUpdated(new Timestamp(System.currentTimeMillis()));
        customTriggersRepository.delete(customTriggers);
        String jobKey = String.valueOf(id);
        String groupKey = "DEFAULT";

        JobKey jkey = new JobKey(jobKey, groupKey);
        log.info("Parameters received for deleting job : jobKey :" + jobKey);

        try {
            boolean status = scheduler.deleteJob(jkey);
            log.info("customTriggers.getParentId() : {}", customTriggers.getParentId());
            if (null != customTriggers.getParentId() && customTriggers.getParentId() > 0) {
                JobKey parentJobKey = new JobKey(String.valueOf(customTriggers.getParentId()), groupKey);
                status = scheduler.deleteJob(parentJobKey);
                CustomTriggers customTriggersParent = getCustomTriggers(customTriggers.getParentId());       // cast id to String
                String idParent = String.valueOf(customTriggersParent.getId());

                // create JobDetail
                JobDetail jobDetailParent = JobBuilder.newJob(CustomTriggersJob.class).withIdentity(idParent).build();

                // Add data to JobDataMap
                jobDetailParent.getJobDataMap().put("id", idParent);
                jobDetailParent.getJobDataMap().put("triggerBy", jwt.getSub());
                jobDetailParent.getJobDataMap().put("chain", "N");
                jobDetailParent.getJobDataMap().put("chainId", "0");

                Date runJobAt = new Date(customTriggersParent.getTriggerAtInMillis());
                runJobAt.setSeconds(0);
                SimpleTrigger triggerParent =
                        TriggerBuilder.newTrigger()
                                .withIdentity(idParent)
                                .startAt(runJobAt)
                                .withSchedule(
                                        SimpleScheduleBuilder.simpleSchedule())
                                .build();
                scheduler.scheduleJob(jobDetailParent, triggerParent);
                scheduler.start();
            }

//      boolean status = scheduler.deleteJob(jkey);
//      log.info("Job with jobKey :"+jobKey+ " deleted with status :"+status);
            PaymentAlias paymentAlias =
                    paymentAliasService.findOneById(customTriggers.getPaymentAliasId());
            if (customTriggers.getPaymentType() == 0) {
                paymentAlias.setProposalTriggersId(0L);
                paymentAlias.setProposalScheduleDate(null);
                paymentAlias.setProposalJobStatus(null);
            } else {
                paymentAlias.setPaymentTriggersId(0L);
                paymentAlias.setRunScheduleDate(null);
                paymentAlias.setRunJobStatus(null);
            }
            paymentAliasService.update(paymentAlias);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SchedulerException e) {
            log.error("SchedulerException while deleting job with key :" + jobKey + " message :" + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> pauseJob(Long id) throws IOException {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
        log.info("Request received for pausing job.");

        CustomTriggers customTriggers = getCustomTriggers(id);
        customTriggers.setState("PAUSED");
        customTriggers.setUpdatedBy(jwt.getSub());
        customTriggers.setUpdated(new Timestamp(System.currentTimeMillis()));
        customTriggersRepository.save(customTriggers);
        String jobKey = String.valueOf(id);
        String groupKey = "DEFAULT";
        JobKey jkey = new JobKey(jobKey, groupKey);
        log.info("Parameters received for pausing job : jobKey :" + jobKey + ", groupKey :" + groupKey);

        try {
            scheduler.pauseJob(jkey);
            log.info("Job with jobKey :" + jobKey + " paused successfully.");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SchedulerException e) {
            log.error("SchedulerException while pausing job with key :" + jobKey + " message :" + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> resumeJob(Long id) throws IOException {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
        log.info("Request received for resuming job.");

        CustomTriggers customTriggers = getCustomTriggers(id);
        customTriggers.setState("SCHEDULED");
        customTriggers.setUpdatedBy(jwt.getSub());
        customTriggers.setUpdated(new Timestamp(System.currentTimeMillis()));
        customTriggersRepository.save(customTriggers);
        String jobKey = String.valueOf(id);
        String groupKey = "DEFAULT";

        JobKey jKey = new JobKey(jobKey, groupKey);
        log.info("Parameters received for resuming job : jobKey :" + jobKey);
        try {
            scheduler.resumeJob(jKey);
            log.info("Job with jobKey :" + jobKey + " resumed successfully.");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SchedulerException e) {
            log.error("SchedulerException while resuming job with key :" + jobKey + " message :" + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> startJobNow(Long id) throws IOException {
        Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
        log.info("Request received for starting job now.");
        log.info(
                "Parameters received for starting job now : jobKey :"
                        + id
                        + ", date: "
                        + new Date());
        String jobKey = String.valueOf(id);
        String groupKey = "DEFAULT";

        JobKey jKey = new JobKey(jobKey, groupKey);
        log.info("Parameters received for starting job now : jobKey :" + jobKey);
        try {
            scheduler.triggerJob(jKey);
            log.info("Job with jobKey :" + jobKey + " started now succesfully.");
            CustomTriggers customTriggers = getCustomTriggers(id);
            PaymentAlias paymentAlias =
                    paymentAliasService.findOneById(customTriggers.getPaymentAliasId());
            if (customTriggers.getPaymentType() == 0) {
                paymentAlias.setProposalJobStatus("I");
            } else {
                paymentAlias.setRunJobStatus("I");
            }
            paymentAliasService.update(paymentAlias);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SchedulerException e) {
            log.error("SchedulerException while starting job now with key :" + jobKey + " message :" + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<Boolean>> isJobRunning(Long id) throws IOException {
        Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
        log.info("Request received to check if job is running");

        String jobKey = String.valueOf(id);
        String groupKey = "DEFAULT";

        log.info("Parameters received for checking job is running now : jobKey :" + jobKey);
        try {

            List<JobExecutionContext> currentJobs = scheduler.getCurrentlyExecutingJobs();
            if (currentJobs != null) {
                for (JobExecutionContext jobCtx : currentJobs) {
                    String jobNameDB = jobCtx.getJobDetail().getKey().getName();
                    String groupNameDB = jobCtx.getJobDetail().getKey().getGroup();
                    if (jobKey.equalsIgnoreCase(jobNameDB) && groupKey.equalsIgnoreCase(groupNameDB)) {
                        return new ResponseEntity<>(new Result<>(true), HttpStatus.OK);
                    }
                }
            }
        } catch (SchedulerException e) {
            log.error("SchedulerException while checking job with key :" + jobKey + " is running. error message :" + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Result<>(false), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Result<Boolean>> isJobWithNamePresent(Long id) throws IOException {
        Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
        try {
            String groupKey = "DEFAULT";
            JobKey jobKey = new JobKey(String.valueOf(id), groupKey);
            if (scheduler.checkExists(jobKey)) {
                return new ResponseEntity<>(new Result<>(true), HttpStatus.OK);
            }
        } catch (SchedulerException e) {
            log.error("SchedulerException while checking job with name and group exist:" + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Result<>(false), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Result<String>> getJobState(Long id) throws IOException {
        Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
        log.info("JobServiceImpl.getJobState()");

        try {
            String groupKey = "DEFAULT";
            JobKey jobKey = new JobKey(String.valueOf(id), groupKey);

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);

            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
            if (triggers != null && triggers.size() > 0) {
                for (Trigger trigger : triggers) {
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    String state = "";
                    if (Trigger.TriggerState.PAUSED.equals(triggerState)) {
                        state = "PAUSED";
                    } else if (Trigger.TriggerState.BLOCKED.equals(triggerState)) {
                        state = "BLOCKED";
                    } else if (Trigger.TriggerState.COMPLETE.equals(triggerState)) {
                        state = "COMPLETE";
                    } else if (Trigger.TriggerState.ERROR.equals(triggerState)) {
                        state = "ERROR";
                    } else if (Trigger.TriggerState.NONE.equals(triggerState)) {
                        state = "NONE";
                    } else if (Trigger.TriggerState.NORMAL.equals(triggerState)) {
                        state = "SCHEDULED";
                    }
                    return new ResponseEntity<>(new Result<>(state), HttpStatus.OK);
                }
            }
        } catch (SchedulerException e) {
            log.error("SchedulerException while checking job with name and group exist:" + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Result<>(""), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> stopJob(Long id) throws IOException {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
        log.info("JobServiceImpl.stopJob()");
        try {
            CustomTriggers customTriggers = getCustomTriggers(id);
            customTriggers.setState("CANCELLED");
            customTriggers.setUpdatedBy(jwt.getSub());
            customTriggers.setUpdated(new Timestamp(System.currentTimeMillis()));
            customTriggersRepository.save(customTriggers);
            String jobKey = String.valueOf(id);
            String groupKey = "DEFAULT";

            JobKey jkey = new JobKey(jobKey, groupKey);
            scheduler.interrupt(jkey);
            PaymentAlias paymentAlias =
                    paymentAliasService.findOneById(customTriggers.getPaymentAliasId());
            if (customTriggers.getPaymentType() == 0) {
                paymentAlias.setProposalJobStatus("P");
            } else {
                paymentAlias.setRunJobStatus("P");
            }
            paymentAliasService.update(paymentAlias);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (SchedulerException e) {
            log.error("SchedulerException while stopping job. error message :" + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> deleteJobLog(Long id) {
        try {
            customTriggersRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateRunJob(Long id) throws IOException {
        CustomTriggers customTriggers = getCustomTriggers(id);
        customTriggers.setState("FINISHED");
        customTriggers.setStatus("SUCCESS");
        customTriggersRepository.save(customTriggers);
    }

    @Override
    public CustomTriggers save(CustomTriggers customTriggers) throws IOException {
        return customTriggersRepository.save(customTriggers);
    }
}
