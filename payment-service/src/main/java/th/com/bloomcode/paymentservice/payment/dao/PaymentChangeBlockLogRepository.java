package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.payment.entity.PaymentChangeBlockLog;

import java.util.List;

public interface PaymentChangeBlockLogRepository extends CrudRepository<PaymentChangeBlockLog, Long> {


    List<PaymentChangeBlockLog> findAll();


}
