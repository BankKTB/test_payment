package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.JUHead;
import th.com.bloomcode.paymentservice.model.payment.SmartFileFooter;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.repository.payment.JuHeadRepository;
import th.com.bloomcode.paymentservice.service.payment.JUHeadService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.webservice.model.response.APPaymentResponse;

import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
public class JUHeadServiceImpl implements JUHeadService {
    private final JuHeadRepository juHeadRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JUHeadServiceImpl(JuHeadRepository juHeadRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.juHeadRepository = juHeadRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public JUHead save(JUHead juHead) {
        if (null == juHead.getId() || 0 == juHead.getId()) {
            juHead.setId(SqlUtil.getNextSeries(jdbcTemplate, JUHead.TABLE_NAME + JUHead.SEQ, null));
        }
        return juHeadRepository.save(juHead);
    }

    public JUHead findAllByPaymentDateAndPaymentNameAndTestRun(Timestamp paymentDate, String paymentName, boolean testRun) {
        log.info("==== findAllByPaymentDateAndPaymentNameAndTestRun : {}", paymentDate + "-" + paymentName + "-" + testRun);
        return juHeadRepository.findAllByPaymentDateAndPaymentNameAndTestRun(paymentDate, paymentName, testRun);
    }

    @Override
    public void deleteAllById(Long id) {
        log.info("==== delete juHead Id: {}", id);
        juHeadRepository.deleteById(id);
    }

    @Override
    public List<JUHead> findJuHeadByListPaymentDateAndListPaymentName(GenerateJuRequest request) {
        return juHeadRepository.findJuHeadByListPaymentDateAndListPaymentName(request);
    }

    @Override
    public void updateJuDocument(APPaymentResponse aPPaymentResponse, String messageQueueId) {
        log.info("==== update juHead document: {}", aPPaymentResponse.getAccDocNo());
        juHeadRepository.updateJuDocument(aPPaymentResponse,messageQueueId);
    }
}
