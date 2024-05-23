package th.com.bloomcode.paymentservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentAliasResponse;
import th.com.bloomcode.paymentservice.model.request.CustomTriggersRequest;
import th.com.bloomcode.paymentservice.payment.entity.CustomTriggers;
import th.com.bloomcode.paymentservice.service.CustomTriggersSchedulerService;
import th.com.bloomcode.paymentservice.service.payment.PaymentAliasService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/scheduler")
@Slf4j
public class CustomTriggersSchedulerController {

    private final CustomTriggersSchedulerService customTriggersSchedulerService;
    private final PaymentAliasService paymentAliasService;

    @Autowired
    public CustomTriggersSchedulerController(CustomTriggersSchedulerService customTriggersSchedulerService, PaymentAliasService paymentAliasService) {
        this.customTriggersSchedulerService = customTriggersSchedulerService;
        this.paymentAliasService = paymentAliasService;
    }


    @PostMapping(value = "/schedule")
    public @ResponseBody
    ResponseEntity<Result<CustomTriggers>> schedule(@RequestBody CustomTriggers request) {
        return customTriggersSchedulerService.scheduleCustomTriggers(request);
    }


    @PostMapping(value = "/unschedule")
    public ResponseEntity<Void> unschedule(@RequestBody CustomTriggers request) {
        return customTriggersSchedulerService.unscheduleCustomTriggers(request.getId());
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<Result<List<CustomTriggers>>> getAll() {
//        log.info("customTriggersSchedulerService.getAllCustomTriggers() : {}", customTriggersSchedulerService.getAllCustomTriggers());
        return customTriggersSchedulerService.getAllCustomTriggers();
    }

    @PostMapping(value = "/getAllByCondition")
    public ResponseEntity<Result<List<CustomTriggers>>> getAllByCondition(@RequestBody CustomTriggersRequest request) {
//        log.info("customTriggersSchedulerService.getAllCustomTriggers() : {}", customTriggersSchedulerService.getAllCustomTriggers());
        return customTriggersSchedulerService.getAllWithPredicate(request);
    }

    @PatchMapping(path = "/run/{id}")
    public ResponseEntity<Void> runJob(@PathVariable Long id) throws IOException {
        return customTriggersSchedulerService.startJobNow(id);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Void> addJob(@RequestBody CustomTriggers request) throws IOException {
        return customTriggersSchedulerService.addJob(request);
    }

    @PostMapping(path = "/addJobRunNow")
    public ResponseEntity<Void> addJobRunNow(@RequestBody CustomTriggers request) throws IOException {
        return customTriggersSchedulerService.addJobRunNow(request);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Void> updateJob(@PathVariable Long id, @RequestBody CustomTriggers request) throws IOException {
        return customTriggersSchedulerService.updateJob(id, request);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) throws IOException {
        return customTriggersSchedulerService.deleteJob(id);
    }

    @PatchMapping(path = "/pause/{id}")
    public ResponseEntity<Void> pauseJob(@PathVariable Long id) throws IOException {
        return customTriggersSchedulerService.pauseJob(id);
    }

    @PatchMapping(path = "/resume/{id}")
    public ResponseEntity<Void> resumeJob(@PathVariable Long id) throws IOException {
        return customTriggersSchedulerService.resumeJob(id);
    }

    @PatchMapping(path = "/stop/{id}")
    public ResponseEntity<Void> stopJob(@PathVariable Long id) throws IOException {
        return customTriggersSchedulerService.stopJob(id);
    }

    @GetMapping(path = "/findCreateJobByCondition/{value}")
    public ResponseEntity<Result<List<PaymentAliasResponse>>> find(@PathVariable("value") String value) throws Exception {
        return paymentAliasService.findCreateJobByCondition(value);
    }

    @GetMapping(path = "/findCreateJobByCondition/{paymentDate}/{paymentName}")
    public ResponseEntity<Result<PaymentAliasResponse>> findOne(@PathVariable("paymentDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date paymentDate, @PathVariable("paymentName") String paymentName) throws Exception {
        return paymentAliasService.findCreateJobByCondition(paymentDate, paymentName);
    }

    @GetMapping(path = "/findSearchJobByCondition/{value}")
    public ResponseEntity<Result<List<PaymentAliasResponse>>> findSearchJobByCondition(@PathVariable("value") String value) throws Exception {
        return paymentAliasService.findSearchJobByCondition(value);
    }

    @DeleteMapping(path = "/deleteJobLog/{id}")
    public ResponseEntity<Void> deleteJobLog(@PathVariable Long id) throws IOException {
        return customTriggersSchedulerService.deleteJobLog(id);
    }
}
