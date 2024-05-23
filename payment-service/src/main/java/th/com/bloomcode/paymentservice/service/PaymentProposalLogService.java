//package th.com.bloomcode.paymentservice.service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import th.com.bloomcode.paymentservice.model.Result;
//import th.com.bloomcode.paymentservice.payment.dao.PaymentProposalLogRepository;
//import th.com.bloomcode.paymentservice.payment.entity.PaymentProposalLog;
//import th.com.bloomcode.paymentservice.util.MessageConstant;
//import th.com.bloomcode.paymentservice.util.SqlUtil;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import java.util.*;
//
//@Service
//public class PaymentProposalLogService {
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    private final PaymentProposalLogRepository paymentProposalLogRepository;
//    private final EntityManager entityManager;
//
//    List<PaymentProposalLog> paymentProposalLogs = new ArrayList<>();
//
//    @Autowired
//    public PaymentProposalLogService(PaymentProposalLogRepository paymentProposalLogRepository, @Qualifier("paymentEntityManagerFactory") EntityManager entityManager) {
//        this.paymentProposalLogRepository = paymentProposalLogRepository;
//        this.entityManager = entityManager;
//    }
//
//    public PaymentProposalLog save(PaymentProposalLog paymentProposalLog) {
//        return this.paymentProposalLogRepository.save(paymentProposalLog);
//    }
//
//    public void addSuccessLog(int seq, String messageText, String messageClass, String messageNo, Long paymentAliasId) {
//        PaymentProposalLog paymentProposalLog = new PaymentProposalLog();
//        paymentProposalLog.setLogDate(new Date());
//        paymentProposalLog.setSeq(seq);
//        paymentProposalLog.setMessageText(messageText);
//        paymentProposalLog.setMessageClass(messageClass);
//        paymentProposalLog.setMessageNo(messageNo);
//        paymentProposalLog.setMessageType(MessageConstant.MESSAGE_TYPE_SUCCESS);
//        paymentProposalLog.setPaymentAliasId(paymentAliasId);
//        paymentProposalLogs.add(paymentProposalLog);
////		logger.info(paymentProposalLog.getMessageText());
//    }
//
//    public void saveAll() {
//        this.paymentProposalLogRepository.saveAll(this.paymentProposalLogs);
//    }
//
//    public ResponseEntity<Result<List<?>>> findByPaymentAlias(Long paymentAliasId) {
//
//        Result<List<?>> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//
//            List<PaymentProposalLog> paymentProposalLogs = paymentProposalLogRepository
//                    .findAllByPaymentAliasIdOrderBySeqAsc(paymentAliasId);
//            if (paymentProposalLogs.size() > 0) {
//                result.setStatus(HttpStatus.OK.value());
//                result.setData(paymentProposalLogs);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else {
//                result.setStatus(HttpStatus.NO_CONTENT.value());
//                result.setData(null);
//                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//
//    @Transactional
//    public void deleteAllByPaymentAliasId(Long paymentAliasId) {
//        Map<String, Object> params = new HashMap<>();
//        StringBuilder sb = new StringBuilder();
//        sb.append("DELETE ");
//        sb.append(" FROM ");
//        sb.append(" PAYMENT_PROPOSAL_LOG P");
//        sb.append(" WHERE ");
//        sb.append(" 1 = 1  ");
////		sb.append(" P.PAYMENT_ALIAS_ID  = " + paymentAliasId);
//
//        if (paymentAliasId > 0) {
//            sb.append(SqlUtil.whereClause(paymentAliasId.toString(), "P.PAYMENT_ALIAS_ID", params));
//        }
//
//        logger.info(sb.toString());
//        Query q = entityManager.createNativeQuery(sb.toString());
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        q.executeUpdate();
//
//    }
//
//    public void clearLog() {
//        this.paymentProposalLogs = new ArrayList<>();
//    }
//}
