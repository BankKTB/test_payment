package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.PaymentInformation;
import th.com.bloomcode.paymentservice.repository.payment.PaymentInformationRepository;
import th.com.bloomcode.paymentservice.service.payment.PaymentInformationService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.util.List;

@Service
public class PaymentInformationServiceImpl implements PaymentInformationService {
    private final PaymentInformationRepository paymentInformationRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PaymentInformationServiceImpl(PaymentInformationRepository paymentInformationRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.paymentInformationRepository = paymentInformationRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public PaymentInformation save(PaymentInformation paymentInformation) {
        if (null == paymentInformation.getId() || 0 == paymentInformation.getId()) {
            paymentInformation.setId(SqlUtil.getNextSeries(jdbcTemplate, PaymentInformation.TABLE_NAME + PaymentInformation.SEQ, null));
        }
        return paymentInformationRepository.save(paymentInformation);
    }

    @Override
    public List<PaymentInformation> save(List<PaymentInformation> paymentInformation) {
        return (List<PaymentInformation>) paymentInformationRepository.saveAll(paymentInformation);
    }

    @Override
    public List<PaymentInformation> findAll() {
        return (List<PaymentInformation>) paymentInformationRepository.findAll();
    }
    
    @Override
    public PaymentInformation findOneById(Long id) {
        return paymentInformationRepository.findById(id).orElse(null);
    }

    @Override
    public PaymentInformation findOneByPmGroupDocAndPmGroupNoAndProposalFalse(String pmGroupDoc, String pmGroupNo) {
        return paymentInformationRepository.findOneByPmGroupDocAndPmGroupNoAndProposalFalse(pmGroupDoc, pmGroupNo);
    }

    @Override
    public List<PaymentInformation> findAllByPaymentAliasIdAndProposal(Long paymentAliasId, String proposal) {
        return paymentInformationRepository.findAllByPaymentAliasIdAndProposalFalse(paymentAliasId, proposal);
    }

//    @Override
//    public synchronized Long getNextSeries() {
//        return paymentInformationRepository.getNextSeries();
//    }
//
//    @Override
//    public void updateNextSeries(Long lastSeq) {
//        paymentInformationRepository.updateNextSeries(lastSeq);
//    }

    @Override
    public void saveBatch(List<PaymentInformation> paymentInformations) {
        this.paymentInformationRepository.saveBatch(paymentInformations);
    }
}
