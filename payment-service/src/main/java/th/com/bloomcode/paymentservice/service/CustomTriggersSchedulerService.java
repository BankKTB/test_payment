package th.com.bloomcode.paymentservice.service;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.request.CustomTriggersRequest;
import th.com.bloomcode.paymentservice.payment.entity.CustomTriggers;

import java.io.IOException;
import java.util.List;

public interface CustomTriggersSchedulerService {
    /**
     * This method schedules customTriggers.
     */
    ResponseEntity<Result<CustomTriggers>> scheduleCustomTriggers(CustomTriggers customTriggers);

    /**
     * This method unschedules customTriggers.
     * @return
     */
    ResponseEntity<Void> unscheduleCustomTriggers(Long id);

    /**
     * This method updates customTriggers in database.
     */
    ResponseEntity<Result<CustomTriggers>> updateCustomTriggers(CustomTriggers customTriggers);

    /**
     * This method retrieves customTriggers by id from database.
     */
    CustomTriggers getCustomTriggers(Long id);

    CustomTriggers getCustomTriggersByParentId(Long id, String state);

    ResponseEntity<Result<CustomTriggers>> getCustomTriggersById(Long id);

    ResponseEntity<Result<List<CustomTriggers>>> getAllCustomTriggers();

    ResponseEntity<Result<List<CustomTriggers>>> getAllWithPredicate(CustomTriggersRequest request);

    List<CustomTriggers> getAll(CustomTriggersRequest request);

    ResponseEntity<Void> addJob(CustomTriggers customTriggers) throws IOException;

    ResponseEntity<Void> addJobRunNow(CustomTriggers customTriggers) throws IOException;

    ResponseEntity<Void> updateJob(Long id, CustomTriggers request) throws IOException;

    ResponseEntity<Void> deleteJob(Long id) throws IOException;

    ResponseEntity<Void> pauseJob(Long id) throws IOException;

    ResponseEntity<Void> resumeJob(Long id) throws IOException;

    ResponseEntity<Void> startJobNow(Long id) throws IOException;

    ResponseEntity<Result<Boolean>> isJobRunning(Long id) throws IOException;

    ResponseEntity<Result<Boolean>> isJobWithNamePresent(Long id) throws IOException;

    ResponseEntity<Result<String>> getJobState(Long id) throws IOException;

    ResponseEntity<Void> stopJob(Long id) throws IOException;

    ResponseEntity<Void> deleteJobLog(Long id);

    void updateRunJob(Long id) throws IOException;

    CustomTriggers save(CustomTriggers customTriggers) throws IOException;

    CustomTriggers getOne(Long id);
}
