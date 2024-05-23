package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentProposalLog;
import th.com.bloomcode.paymentservice.repository.payment.PaymentProposalLogRepository;
import th.com.bloomcode.paymentservice.service.payment.PaymentProposalLogService;
import th.com.bloomcode.paymentservice.util.MessageConstant;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PaymentProposalLogServiceImpl implements PaymentProposalLogService {

    private final PaymentProposalLogRepository paymentProposalLogRepository;

    public PaymentProposalLogServiceImpl(PaymentProposalLogRepository paymentProposalLogRepository) {
        this.paymentProposalLogRepository = paymentProposalLogRepository;
    }

    public void addSuccessLog(Long paymentProposalLogId, int sequence, String messageText, String messageClass, String messageNo, Long paymentAliasId, boolean proposal, List<PaymentProposalLog> paymentProposalLogs) {

        PaymentProposalLog paymentProposalLog = new PaymentProposalLog();
        paymentProposalLog.setId(paymentProposalLogId);
//        paymentProposalLog.setId(paymentProposalLogRepository.getNextSeries());
        paymentProposalLog.setLogDate(new Timestamp(System.currentTimeMillis()));
        paymentProposalLog.setSequence(sequence);
        paymentProposalLog.setMessageText(messageText);
        paymentProposalLog.setMessageClass(messageClass);
        paymentProposalLog.setMessageNo(messageNo);
        paymentProposalLog.setMessageType(MessageConstant.MESSAGE_TYPE_SUCCESS);
        paymentProposalLog.setPaymentAliasId(paymentAliasId);
        paymentProposalLog.setProposal(proposal);
        paymentProposalLogs.add(paymentProposalLog);
//        paymentProposalLogRepository.save(paymentProposalLog);
//		logger.info(paymentProposalLog.getMessageText());
    }

    @Override
    public void addErrorLog(Long paymentProposalLogId, int sequence, String messageText, String messageClass, String messageNo, Long paymentAliasId, boolean proposal, List<PaymentProposalLog> paymentProposalLogs) {
        PaymentProposalLog paymentProposalLog = new PaymentProposalLog();
        paymentProposalLog.setId(paymentProposalLogId);
//        paymentProposalLog.setId(paymentProposalLogRepository.getNextSeries());
        paymentProposalLog.setLogDate(new Timestamp(System.currentTimeMillis()));
        paymentProposalLog.setSequence(sequence);
        paymentProposalLog.setMessageText(messageText);
        paymentProposalLog.setMessageClass(messageClass);
        paymentProposalLog.setMessageNo(messageNo);
        paymentProposalLog.setMessageType(MessageConstant.MESSAGE_TYPE_ERROR);
        paymentProposalLog.setPaymentAliasId(paymentAliasId);
        paymentProposalLog.setProposal(proposal);
        paymentProposalLogs.add(paymentProposalLog);
//        paymentProposalLogRepository.save(paymentProposalLog);
//		logger.info(paymentProposalLog.getMessageText());
    }

    @Override
    public void deletePaymentProposalLog(Long paymentAliasId, boolean isProposal) {
        this.paymentProposalLogRepository.deletePaymentProposalLog(paymentAliasId, isProposal);
    }

    @Override
    public ResponseEntity<Result<Page<PaymentProposalLog>>> findAllByPaymentAliasIdAndProposal(Long paymentAliasId, boolean success, int page, int size) {
        Result<Page<PaymentProposalLog>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            Page<PaymentProposalLog> paymentProposalLog = this.paymentProposalLogRepository.findAllByPaymentAliasIdAndProposal(paymentAliasId, success, page, size);
            result.setStatus(HttpStatus.OK.value());
            result.setData(paymentProposalLog);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void saveBatch(List<PaymentProposalLog> paymentProposalLogs) {
        this.paymentProposalLogRepository.saveBatch(paymentProposalLogs);
    }


    @Override
    public synchronized Long getNextSeries() {
        return paymentProposalLogRepository.getNextSeries();
    }

    @Override
    public void updateNextSeries(Long lastSeq) {
        log.info("  lastSeq {} ", lastSeq);
        paymentProposalLogRepository.updateNextSeries(lastSeq);
    }

}
