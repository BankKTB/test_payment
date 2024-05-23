package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.MassChangeDocumentDetailLog;
import th.com.bloomcode.paymentservice.repository.payment.MassChangeDocumentDetailLogRepository;
import th.com.bloomcode.paymentservice.service.payment.MassChangeDocumentDetailLogService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class MassChangeDocumentDetailLogServiceImpl implements MassChangeDocumentDetailLogService {

    private final JdbcTemplate jdbcTemplate;
    private final MassChangeDocumentDetailLogRepository massChangeDocumentDetailLogRepository;

    public MassChangeDocumentDetailLogServiceImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, MassChangeDocumentDetailLogRepository massChangeDocumentDetailLogRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.massChangeDocumentDetailLogRepository = massChangeDocumentDetailLogRepository;
    }


    @Override
    public ResponseEntity<Result<List<MassChangeDocumentDetailLog>>> findErrorDetailByDocument(String companyCode, String documentNo, String fiscalYear) {
        Result<List<MassChangeDocumentDetailLog>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<MassChangeDocumentDetailLog> massChangeDocumentDetailLogs = massChangeDocumentDetailLogRepository.findDetailByDocument(companyCode, documentNo, fiscalYear);
            if (massChangeDocumentDetailLogs.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(massChangeDocumentDetailLogs);
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
    public MassChangeDocumentDetailLog save(MassChangeDocumentDetailLog massChangeDocumentDetailLog) {

        if (null == massChangeDocumentDetailLog.getId() || 0 == massChangeDocumentDetailLog.getId()) {
            massChangeDocumentDetailLog.setId(SqlUtil.getNextSeries(jdbcTemplate, massChangeDocumentDetailLog.TABLE_NAME + massChangeDocumentDetailLog.SEQ, null));
            massChangeDocumentDetailLog.setCreated(new Timestamp(System.currentTimeMillis()));
        }

        return this.massChangeDocumentDetailLogRepository.save(massChangeDocumentDetailLog);
    }
}
