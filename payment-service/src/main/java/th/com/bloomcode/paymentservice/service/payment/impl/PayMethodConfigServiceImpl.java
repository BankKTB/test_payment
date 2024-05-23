package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.PayMethodConfig;
import th.com.bloomcode.paymentservice.repository.idem.PayMethodForPaymentRepository;
import th.com.bloomcode.paymentservice.repository.payment.PayMethodConfigRepository;
import th.com.bloomcode.paymentservice.service.payment.PayMethodConfigService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.util.List;

@Service
public class PayMethodConfigServiceImpl implements PayMethodConfigService {
//    private final PayMethodConfigRepository payMethodConfigRepository;
    private final PayMethodForPaymentRepository payMethodForPaymentRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PayMethodConfigServiceImpl(
             PayMethodForPaymentRepository payMethodForPaymentRepository, @Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
//        this.payMethodConfigRepository = payMethodConfigRepository;
        this.payMethodForPaymentRepository = payMethodForPaymentRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

//    public PayMethodConfig save(PayMethodConfig payMethodConfig) {
//        if (null == payMethodConfig.getId() || 0 == payMethodConfig.getId()) {
//            payMethodConfig.setId(SqlUtil.getNextSeries(jdbcTemplate, PayMethodConfig.TABLE_NAME + PayMethodConfig.SEQ));
//        }
//        return payMethodConfigRepository.save(payMethodConfig);
//    }

//    @Override
//    public List<PayMethodConfig> findAll() {
//        return (List<PayMethodConfig>) payMethodConfigRepository.findAll();
//    }
//
//    @Override
//    public PayMethodConfig findOneById(Long id) {
//        return payMethodConfigRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    public List<PayMethodConfig> findAllOrderByCountryAscPayMethodAsc() {
//        return payMethodConfigRepository.findAllOrderByCountryAscPayMethodAsc();
//    }
    @Override
    public List<PayMethodConfig> findAllOrderByCountryAscPayMethodAscNew() {
        return payMethodForPaymentRepository.findAllOrderByCountryAscPayMethodAsc();
    }
}
