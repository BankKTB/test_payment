package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.SmartFileBatch;
import th.com.bloomcode.paymentservice.repository.payment.SmartFileBatchRepository;
import th.com.bloomcode.paymentservice.service.payment.SmartFileBatchService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.util.List;

@Service
public class SmartFileBatchServiceImpl implements SmartFileBatchService {
    private final SmartFileBatchRepository smartFileBatchRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SmartFileBatchServiceImpl(SmartFileBatchRepository smartFileBatchRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.smartFileBatchRepository = smartFileBatchRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public SmartFileBatch save(SmartFileBatch smartFileBatch) {
        if (null == smartFileBatch.getId() || 0 == smartFileBatch.getId()) {
            smartFileBatch.setId(SqlUtil.getNextSeries(jdbcTemplate, SmartFileBatch.TABLE_NAME + SmartFileBatch.SEQ, null));
        }
        return smartFileBatchRepository.save(smartFileBatch);
    }

    @Override
    public List<SmartFileBatch> findAll() {
        return (List<SmartFileBatch>) smartFileBatchRepository.findAll();
    }
    
    @Override
    public SmartFileBatch findOneById(Long id) {
        return smartFileBatchRepository.findById(id).orElse(null);
    }
}
