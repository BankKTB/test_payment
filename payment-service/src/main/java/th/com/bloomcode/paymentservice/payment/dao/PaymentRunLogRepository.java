package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.payment.entity.PaymentRunLog;

import java.util.List;

public interface PaymentRunLogRepository extends CrudRepository<PaymentRunLog, Long> {

    List<PaymentRunLog> findAllByPaymentAliasIdOrderBySeqAsc(Long paymentAliasId);

}
