package th.com.bloomcode.paymentservice.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;
import th.com.bloomcode.paymentservice.model.request.CustomTriggersRequest;
import th.com.bloomcode.paymentservice.payment.entity.CustomTriggers;
import th.com.bloomcode.paymentservice.repository.payment.PaymentAliasRepository;
import th.com.bloomcode.paymentservice.service.CustomTriggersSchedulerService;
import th.com.bloomcode.paymentservice.service.payment.PaymentProcessService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshPaymentJob {

    private final CustomTriggersSchedulerService customTriggersSchedulerService;
    private final PaymentAliasRepository paymentAliasRepository;
    private final PaymentProcessService paymentProcessService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkEndJob() {
        try {
            CustomTriggersRequest request = new CustomTriggersRequest();
            request.setState(Collections.singletonList("RUNNING"));
            request.setPaymentType(1L);
            List<CustomTriggers> customTriggers = customTriggersSchedulerService.getAll(request);
//            log.info("customTriggers : {}", customTriggers);
            if (null != customTriggers) {
                for (CustomTriggers customTrigger : customTriggers) {
                    PaymentAlias paymentAlias = this.paymentAliasRepository.findOneById(customTrigger.getPaymentAliasId());
                    if (null != paymentAlias  && null != paymentAlias.getRunStatus() && paymentAlias.getRunStatus().equalsIgnoreCase("S")) {
//                    Long paymentProcessCount = paymentProcessService.findOneByPaymentAliasIdNotParent(checkDuplicate.getId());
                        List<PaymentProcess> paymentProcessList = paymentProcessService.findAllByPaymentIdAndProposalNotChild(paymentAlias.getId(), false);
                        PaymentProcess paymentProcess = paymentProcessService.findOneIdemLastUpdatePaymentByPaymentAliasId(paymentAlias.getId());
                        Long idemCreatePaymentReply = paymentProcessService.countIdemCreatePaymentReplyByPaymentAliasId(paymentAlias.getId());
                        if (null != paymentProcess) {
                            paymentAlias.setIdemEnd(paymentProcess.getIdemUpdate());
                            paymentAlias.setRunEnd(paymentProcess.getIdemUpdate());
                        }

                        List<PaymentProcess> success = paymentProcessList.stream().filter(object -> "S".equalsIgnoreCase(object.getIdemStatus())).collect(Collectors.toList());
                        List<PaymentProcess> repair = paymentProcessList.stream().filter(object -> "N".equalsIgnoreCase(object.getIdemStatus())).collect(Collectors.toList());
                        List<PaymentProcess> error = paymentProcessList.stream().filter(object -> "E".equalsIgnoreCase(object.getIdemStatus())).collect(Collectors.toList());
                        paymentAlias.setRunSuccessDocument(success.size());
                        paymentAlias.setRunRepairDocument(repair.size());
                        paymentAlias.setRunErrorDocument(error.size());
                        paymentAlias.setIdemCreatePaymentReply(Integer.parseInt(idemCreatePaymentReply.toString()));
                        paymentAliasRepository.save(paymentAlias);

                        if (idemCreatePaymentReply == paymentAlias.getRunTotalDocument()) {
                            customTrigger.setDuration((System.currentTimeMillis() - customTrigger.getTriggerAtInMillis()) / 1000);
                            customTrigger.setEndAtInMillis(System.currentTimeMillis());
                            customTrigger.setState("FINISHED");
                            customTrigger.setStatus("SUCCESS");
                            customTriggersSchedulerService.save(customTrigger);
                            paymentProcessService.restDocumentProposalErrorAfterRealRun(paymentAlias.getId());
                            paymentProcessService.restDocumentProposalChildErrorAfterRealRun(paymentAlias.getId());
                            paymentAliasRepository.save(paymentAlias);
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
