package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.SmartFileHeader;
import th.com.bloomcode.paymentservice.repository.payment.SmartFileHeaderRepository;
import th.com.bloomcode.paymentservice.service.payment.SmartFileHeaderService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.util.List;

@Service
public class SmartFileHeaderServiceImpl implements SmartFileHeaderService {
    private final SmartFileHeaderRepository smartFileHeaderRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SmartFileHeaderServiceImpl(SmartFileHeaderRepository smartFileHeaderRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.smartFileHeaderRepository = smartFileHeaderRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public SmartFileHeader save(SmartFileHeader smartFileHeader) {
        if (null == smartFileHeader.getId() || 0 == smartFileHeader.getId()) {
            smartFileHeader.setId(SqlUtil.getNextSeries(jdbcTemplate, SmartFileHeader.TABLE_NAME + SmartFileHeader.SEQ, null));
        }
        return smartFileHeaderRepository.save(smartFileHeader);
    }

    @Override
    public List<SmartFileHeader> findAll() {
        return (List<SmartFileHeader>) smartFileHeaderRepository.findAll();
    }
    
    @Override
    public SmartFileHeader findOneById(Long id) {
        return smartFileHeaderRepository.findById(id).orElse(null);
    }

    @Override
    public SmartFileHeader findOneByGenerateFileAliasId(Long id) {
        return smartFileHeaderRepository.findById(id).orElse(null);
    }
}
