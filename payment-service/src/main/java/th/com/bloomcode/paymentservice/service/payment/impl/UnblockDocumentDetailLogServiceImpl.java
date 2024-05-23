package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentLog;
import th.com.bloomcode.paymentservice.model.payment.UnblockDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.request.UnblockDocumentLogDetailRequest;
import th.com.bloomcode.paymentservice.repository.payment.UnblockDocumentDetailLogRepository;
import th.com.bloomcode.paymentservice.service.payment.UnblockDocumentDetailLogService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class UnblockDocumentDetailLogServiceImpl implements UnblockDocumentDetailLogService {

    private final JdbcTemplate jdbcTemplate;
    private final UnblockDocumentDetailLogRepository unblockDocumentDetailLogRepository;

    public UnblockDocumentDetailLogServiceImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, UnblockDocumentDetailLogRepository unblockDocumentDetailLogRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.unblockDocumentDetailLogRepository = unblockDocumentDetailLogRepository;
    }


    @Override
    public ResponseEntity<Result<List<UnblockDocumentDetailLog>>> findErrorDetailByDocument(String companyCode, String documentNo, String fiscalYear) {
        Result<List<UnblockDocumentDetailLog>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<UnblockDocumentDetailLog> unblockDocumentDetailLogs = unblockDocumentDetailLogRepository.findDetailByDocument(companyCode, documentNo, fiscalYear);
            if (unblockDocumentDetailLogs.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(unblockDocumentDetailLogs);
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
    public UnblockDocumentDetailLog save(UnblockDocumentDetailLog unblockDocumentDetailLog) {

        if (null == unblockDocumentDetailLog.getId() || 0 == unblockDocumentDetailLog.getId()) {
            unblockDocumentDetailLog.setId(SqlUtil.getNextSeries(jdbcTemplate, UnblockDocumentDetailLog.TABLE_NAME + UnblockDocumentDetailLog.SEQ, null));
            unblockDocumentDetailLog.setCreated(new Timestamp(System.currentTimeMillis()));
        }

        return this.unblockDocumentDetailLogRepository.save(unblockDocumentDetailLog);
    }

    @Override
    public ResponseEntity<Result<List<UnblockDocumentDetailLog>>> findLogDetail(UnblockDocumentLogDetailRequest request) {
        Result<List<UnblockDocumentDetailLog>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<UnblockDocumentDetailLog> unBlock =  unblockDocumentDetailLogRepository.findLogDetail(request);
            result.setStatus(HttpStatus.OK.value());
            result.setData(unBlock);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
