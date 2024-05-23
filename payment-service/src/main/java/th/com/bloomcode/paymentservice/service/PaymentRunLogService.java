package th.com.bloomcode.paymentservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.payment.dao.PaymentRunLogRepository;
import th.com.bloomcode.paymentservice.payment.entity.PaymentRunLog;
import th.com.bloomcode.paymentservice.util.MessageConstant;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Service
public class PaymentRunLogService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final PaymentRunLogRepository paymentRunLogRepository;
    private final EntityManager entityManager;

    @Autowired
    public PaymentRunLogService(PaymentRunLogRepository paymentRunLogRepository, @Qualifier("paymentEntityManagerFactory") EntityManager entityManager) {
        this.paymentRunLogRepository = paymentRunLogRepository;
        this.entityManager = entityManager;
    }

    public PaymentRunLog save(PaymentRunLog paymentRunLog) {
        return this.paymentRunLogRepository.save(paymentRunLog);
    }

    public void addSuccessLog(int seq, String messageText, String messageClass, String messageNo, Long paymentAliasId) {
        PaymentRunLog paymentRunLog = new PaymentRunLog();
        paymentRunLog.setLogDate(new Date());
        paymentRunLog.setSeq(seq);
        paymentRunLog.setMessageText(messageText);
        paymentRunLog.setMessageClass(messageClass);
        paymentRunLog.setMessageNo(messageNo);
        paymentRunLog.setMessageType(MessageConstant.MESSAGE_TYPE_SUCCESS);
        paymentRunLog.setPaymentAliasId(paymentAliasId);
        logger.info(paymentRunLog.getMessageText());
        this.save(paymentRunLog);
    }

    public ResponseEntity<Result<List<?>>> findByPaymentAlias(Long paymentAliasId) {

        Result<List<?>> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            List<PaymentRunLog> paymentRunLogs = paymentRunLogRepository
                    .findAllByPaymentAliasIdOrderBySeqAsc(paymentAliasId);
            if (paymentRunLogs.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(paymentRunLogs);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NO_CONTENT.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    public void deleteAllByPaymentAliasId(Long paymentAliasId) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append(" FROM ");
        sb.append(" PAYMENT_PROPOSAL_LOG P");
        sb.append(" WHERE ");
        sb.append(" P.PAYMENT_ALIAS_ID  = ");
        sb.append(paymentAliasId);
        // TODO SQL Injection
        logger.info(sb.toString());
        Query q = entityManager.createNativeQuery(sb.toString());
        q.executeUpdate();

    }
}
