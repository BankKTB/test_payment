package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.SmartFileLog;
import th.com.bloomcode.paymentservice.repository.payment.SmartFileLogRepository;
import th.com.bloomcode.paymentservice.service.payment.SmartFileLogService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.util.List;

@Service
public class SmartFileLogServiceImpl implements SmartFileLogService {
    private final SmartFileLogRepository smartFileLogRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SmartFileLogServiceImpl(SmartFileLogRepository smartFileLogRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.smartFileLogRepository = smartFileLogRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public SmartFileLog save(SmartFileLog smartFileLog) {
        if (null == smartFileLog.getId() || 0 == smartFileLog.getId()) {
            smartFileLog.setId(SqlUtil.getNextSeries(jdbcTemplate, SmartFileLog.TABLE_NAME + SmartFileLog.SEQ, null));
        }
        return smartFileLogRepository.save(smartFileLog);
    }

    @Override
    public void saveAll(List<SmartFileLog> smartFileLogs) {
        smartFileLogRepository.saveAll(smartFileLogs);
    }

    @Override
    public List<SmartFileLog> findAll() {
        return (List<SmartFileLog>) smartFileLogRepository.findAll();
    }
    
    @Override
    public SmartFileLog findOneById(Long id) {
        return smartFileLogRepository.findById(id).orElse(null);
    }
}
