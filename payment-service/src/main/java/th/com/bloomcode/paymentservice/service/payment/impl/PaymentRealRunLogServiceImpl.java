package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentRealRunLog;
import th.com.bloomcode.paymentservice.repository.payment.PaymentRealRunLogRepository;
import th.com.bloomcode.paymentservice.service.payment.PaymentRealRunLogService;
import th.com.bloomcode.paymentservice.util.MessageConstant;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PaymentRealRunLogServiceImpl implements PaymentRealRunLogService {

    private final PaymentRealRunLogRepository paymentRealRunLogRepository;

    public PaymentRealRunLogServiceImpl(PaymentRealRunLogRepository paymentRealRunLogRepository) {
        this.paymentRealRunLogRepository = paymentRealRunLogRepository;
    }

    public void addSuccessLog(Long paymentRealRunLogId, Long sequence, String messageText, Long paymentAliasId, List<PaymentRealRunLog> paymentRealRunLogs) {

        PaymentRealRunLog paymentRealRunLog = new PaymentRealRunLog();
//        paymentRealRunLog.setId(paymentRealRunLogId);
        paymentRealRunLog.setLogDate(new Timestamp(System.currentTimeMillis()));
        paymentRealRunLog.setSequence(sequence);
        paymentRealRunLog.setMessageText(messageText);
        paymentRealRunLog.setMessageType(MessageConstant.MESSAGE_TYPE_SUCCESS);
        paymentRealRunLog.setPaymentAliasId(paymentAliasId);
        paymentRealRunLogs.add(paymentRealRunLog);
    }

    @Override
    public void addErrorLog(Long paymentRealRunLogId, Long sequence, String messageText, Long paymentAliasId, List<PaymentRealRunLog> paymentRealRunLogs) {
        PaymentRealRunLog paymentRealRunLog = new PaymentRealRunLog();
//        paymentRealRunLog.setId(paymentRealRunLogId);
        paymentRealRunLog.setLogDate(new Timestamp(System.currentTimeMillis()));
        paymentRealRunLog.setSequence(sequence);
        paymentRealRunLog.setMessageText(messageText);
        paymentRealRunLog.setMessageType(MessageConstant.MESSAGE_TYPE_ERROR);
        paymentRealRunLog.setPaymentAliasId(paymentAliasId);
        paymentRealRunLogs.add(paymentRealRunLog);
    }


    @Override
    public ResponseEntity<Result<Page<PaymentRealRunLog>>> findAllByPaymentAliasId(Long paymentAliasId, boolean success, int page, int size) {
        Result<Page<PaymentRealRunLog>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            Page<PaymentRealRunLog> paymentRealRunLogs = this.paymentRealRunLogRepository.findAllByPaymentAliasId(paymentAliasId, success, page, size);
            result.setStatus(HttpStatus.OK.value());
            result.setData(paymentRealRunLogs);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void saveBatch(List<PaymentRealRunLog> paymentProposalLogs) {
        this.paymentRealRunLogRepository.saveBatch(paymentProposalLogs);
    }

    @Override
    public Long getSequenceByPaymentAliasId(Long paymentAliasId) {
       return  this.paymentRealRunLogRepository.getSequenceByPaymentAliasId(paymentAliasId);
    }


    @Override
    public synchronized Long getNextSeries() {
        return paymentRealRunLogRepository.getNextSeries();
    }

    @Override
    public void updateNextSeries(Long lastSeq) {
        log.info("  lastSeq {} ", lastSeq);
        paymentRealRunLogRepository.updateNextSeries(lastSeq);
    }

    @Override
    public void deletePaymentRealRunLog(Long paymentAliasId) {
        this.paymentRealRunLogRepository.deletePaymentRealRunLog(paymentAliasId);
    }

}
