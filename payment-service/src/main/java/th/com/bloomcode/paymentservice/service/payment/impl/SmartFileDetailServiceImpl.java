package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.SmartFileDetail;
import th.com.bloomcode.paymentservice.repository.payment.SmartFileDetailRepository;
import th.com.bloomcode.paymentservice.service.payment.SmartFileDetailService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.util.List;

@Service
public class SmartFileDetailServiceImpl implements SmartFileDetailService {
    private final SmartFileDetailRepository smartFileDetailRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SmartFileDetailServiceImpl(SmartFileDetailRepository smartFileDetailRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.smartFileDetailRepository = smartFileDetailRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public SmartFileDetail save(SmartFileDetail smartFileDetail) {
        if (null == smartFileDetail.getId() || 0 == smartFileDetail.getId()) {
            smartFileDetail.setId(SqlUtil.getNextSeries(jdbcTemplate, SmartFileDetail.TABLE_NAME + SmartFileDetail.SEQ, null));
        }
        return smartFileDetailRepository.save(smartFileDetail);
    }

    @Override
    public List<SmartFileDetail> findAll() {
        return (List<SmartFileDetail>) smartFileDetailRepository.findAll();
    }
    
    @Override
    public SmartFileDetail findOneById(Long id) {
        return smartFileDetailRepository.findById(id).orElse(null);
    }
}
