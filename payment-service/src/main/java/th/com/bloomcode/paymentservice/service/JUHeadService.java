package th.com.bloomcode.paymentservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.payment.dao.JUHeadRepository;
import th.com.bloomcode.paymentservice.model.payment.JUHead;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.repository.payment.JuHeadRepository;
//import th.com.bloomcode.paymentservice.payment.entity.JUHead;
////import th.com.bloomcode.paymentservice.repository.payment.JuHeadRepository;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.List;

@Service
public class JUHeadService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final JuHeadRepository juHeadRepository;
    private final EntityManager entityManager;

    @Autowired
    public JUHeadService(JuHeadRepository juHeadRepository, @Qualifier("paymentEntityManagerFactory") EntityManager entityManager) {
        this.juHeadRepository = juHeadRepository;
        this.entityManager = entityManager;
    }

    public JUHead save(JUHead juHead) {
        logger.info("==== Save juHead : {}", juHead);
        return juHeadRepository.save(juHead);
    }

    public JUHead findAllByPaymentDateAndPaymentNameAndTestRun(Timestamp paymentDate, String paymentName, boolean testRun) {
        logger.info("==== findAllByPaymentDateAndPaymentNameAndTestRun : {}", paymentDate + "-" + paymentName + "-" + testRun);
        return juHeadRepository.findAllByPaymentDateAndPaymentNameAndTestRun(paymentDate, paymentName, testRun);
    }

    public void deleteAllById(Long id) {
        logger.info("==== delete juHead Id: {}", id);
        juHeadRepository.deleteById(id);
    }

    public List<JUHead> findJuHeadByListPaymentDateAndListPaymentName(GenerateJuRequest request) {
        return juHeadRepository.findJuHeadByListPaymentDateAndListPaymentName(request);
    }



}
