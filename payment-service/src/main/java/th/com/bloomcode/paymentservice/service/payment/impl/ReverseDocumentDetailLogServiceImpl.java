package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocumentDetailLog;
import th.com.bloomcode.paymentservice.repository.payment.ReverseDocumentDetailLogRepository;
import th.com.bloomcode.paymentservice.service.payment.ReverseDocumentDetailLogService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ReverseDocumentDetailLogServiceImpl implements ReverseDocumentDetailLogService {

    private final JdbcTemplate jdbcTemplate;
    private final ReverseDocumentDetailLogRepository reverseDocumentDetailLogRepository;

    public ReverseDocumentDetailLogServiceImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, ReverseDocumentDetailLogRepository reverseDocumentDetailLogRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.reverseDocumentDetailLogRepository = reverseDocumentDetailLogRepository;
    }


    @Override
    public ResponseEntity<Result<List<ReverseDocumentDetailLog>>> findErrorDetailByDocument(String companyCode, String documentNo, String fiscalYear) {
        Result<List<ReverseDocumentDetailLog>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<ReverseDocumentDetailLog> reverseDocumentDetailLogs = reverseDocumentDetailLogRepository.findDetailByDocument(companyCode, documentNo, fiscalYear);
            if (reverseDocumentDetailLogs.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(reverseDocumentDetailLogs);
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
    public ReverseDocumentDetailLog save(ReverseDocumentDetailLog reverseDocumentDetailLog) {

        if (null == reverseDocumentDetailLog.getId() || 0 == reverseDocumentDetailLog.getId()) {
            reverseDocumentDetailLog.setId(SqlUtil.getNextSeries(jdbcTemplate, ReverseDocumentDetailLog.TABLE_NAME + ReverseDocumentDetailLog.SEQ, null));
            reverseDocumentDetailLog.setCreated(new Timestamp(System.currentTimeMillis()));
        }

        return this.reverseDocumentDetailLogRepository.save(reverseDocumentDetailLog);
    }
}
