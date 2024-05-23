package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentDetailLog;
import th.com.bloomcode.paymentservice.repository.payment.PaymentRunErrorDocumentDetailLogRepository;
import th.com.bloomcode.paymentservice.service.payment.PaymentRunErrorDocumentDetailLogService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class PaymentRunErrorDocumentDetailLogServiceImpl implements PaymentRunErrorDocumentDetailLogService {

    private final JdbcTemplate jdbcTemplate;
    private final PaymentRunErrorDocumentDetailLogRepository paymentRunErrorDocumentDetailLogRepository;

    public PaymentRunErrorDocumentDetailLogServiceImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, PaymentRunErrorDocumentDetailLogRepository paymentRunErrorDocumentDetailLogRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.paymentRunErrorDocumentDetailLogRepository = paymentRunErrorDocumentDetailLogRepository;
    }


    @Override
    public ResponseEntity<Result<List<PaymentRunErrorDocumentDetailLog>>> findErrorDetailByInvoice(String invoiceCompanyCode, String invoiceDocumentNo, String invoiceFiscalYear) {
        Result<List<PaymentRunErrorDocumentDetailLog>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<PaymentRunErrorDocumentDetailLog> paymentRunErrorDocumentDetailLogs = paymentRunErrorDocumentDetailLogRepository.findDetailByInvoice(invoiceCompanyCode, invoiceDocumentNo, invoiceFiscalYear);
            if (paymentRunErrorDocumentDetailLogs.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(paymentRunErrorDocumentDetailLogs);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PaymentRunErrorDocumentDetailLog save(PaymentRunErrorDocumentDetailLog paymentRunErrorDocumentDetailLog) {

        if (null == paymentRunErrorDocumentDetailLog.getId() || 0 == paymentRunErrorDocumentDetailLog.getId()) {
            paymentRunErrorDocumentDetailLog.setId(SqlUtil.getNextSeries(jdbcTemplate, paymentRunErrorDocumentDetailLog.TABLE_NAME + paymentRunErrorDocumentDetailLog.SEQ, null));
            paymentRunErrorDocumentDetailLog.setCreated(new Timestamp(System.currentTimeMillis()));
        }

        return this.paymentRunErrorDocumentDetailLogRepository.save(paymentRunErrorDocumentDetailLog);
    }

    @Override
    public ResponseEntity<Result<List<PaymentRunErrorDocumentDetailLog>>> findErrorDetailByPaymentAliasId(Long paymentAliasId) {

        Result<List<PaymentRunErrorDocumentDetailLog>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<PaymentRunErrorDocumentDetailLog> paymentRunErrorDocumentDetailLogs = paymentRunErrorDocumentDetailLogRepository.findErrorDetailByPaymentAliasId(paymentAliasId);
            if (paymentRunErrorDocumentDetailLogs.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(paymentRunErrorDocumentDetailLogs);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
