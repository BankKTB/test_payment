package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.payment.entity.JUHead;

import java.sql.Timestamp;

public interface JUHeadRepository extends CrudRepository<JUHead, Long> {


    JUHead findAllByPaymentDateAndPaymentNameAndTestRun(Timestamp paymentDate, String paymentName, boolean testRun);

    void deleteAllById(Long id);

}
