//package th.com.bloomcode.paymentservice.service.payment.impl;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.model.Result;
//import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentLog;
//import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentLog;
//import th.com.bloomcode.paymentservice.repository.payment.PaymentRunErrorDocumentLogRepository;
//import th.com.bloomcode.paymentservice.service.payment.PaymentRunErrorDocumentLogService;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class PaymentRunErrorDocumentLogServiceImpl implements PaymentRunErrorDocumentLogService {
//    private final PaymentRunErrorDocumentLogRepository paymentRunErrorDocumentLogRepository;
//
//
//    public PaymentRunErrorDocumentLogServiceImpl(PaymentRunErrorDocumentLogRepository paymentRunErrorDocumentLogRepository) {
//        this.paymentRunErrorDocumentLogRepository = paymentRunErrorDocumentLogRepository;
//    }
//
//    @Override
//    public ResponseEntity<Result<List<PaymentRunErrorDocumentLog>>> findAll() {
//        Result<List<PaymentRunErrorDocumentLog>> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            List<PaymentRunErrorDocumentLog> paymentRunErrorDocumentLogs = paymentRunErrorDocumentLogRepository.findAllByOrderByFiscalYearAscAccDocNoAsc();
//            if (paymentRunErrorDocumentLogs.size() > 0) {
//                result.setStatus(HttpStatus.OK.value());
//                result.setData(paymentRunErrorDocumentLogs);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else {
//                result.setStatus(HttpStatus.NOT_FOUND.value());
//                result.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
//                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @Override
//    public PaymentRunErrorDocumentLog save(PaymentRunErrorDocumentLog paymentRunErrorDocumentLog) {
//       return this.paymentRunErrorDocumentLogRepository.save(paymentRunErrorDocumentLog);
//    }
//
//}
