package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.SmartFileFooter;
import th.com.bloomcode.paymentservice.repository.payment.SmartFileFooterRepository;
import th.com.bloomcode.paymentservice.service.payment.SmartFileFooterService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.util.List;

@Service
public class SmartFileFooterServiceImpl implements SmartFileFooterService {
    private final SmartFileFooterRepository smartFileFooterRepository;
    private final JdbcTemplate jdbcTemplate;

    public SmartFileFooterServiceImpl(SmartFileFooterRepository smartFileFooterRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.smartFileFooterRepository = smartFileFooterRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public SmartFileFooter save(SmartFileFooter smartFileFooter) {
        if (null == smartFileFooter.getId() || 0 == smartFileFooter.getId()) {
            smartFileFooter.setId(SqlUtil.getNextSeries(jdbcTemplate, SmartFileFooter.TABLE_NAME + SmartFileFooter.SEQ, null));
        }
        return smartFileFooterRepository.save(smartFileFooter);
    }

    @Override
    public List<SmartFileFooter> findAll() {
        return (List<SmartFileFooter>) smartFileFooterRepository.findAll();
    }
    
    @Override
    public SmartFileFooter findOneById(Long id) {
        return smartFileFooterRepository.findById(id).orElse(null);
    }
}
