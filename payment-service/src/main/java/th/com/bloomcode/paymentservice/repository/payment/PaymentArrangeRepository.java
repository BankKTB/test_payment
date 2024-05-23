package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.PaymentArrange;

import java.util.List;

public interface PaymentArrangeRepository extends CrudRepository<PaymentArrange, Long> {
  PaymentArrange findOneByArrangeCodeAndArrangeName(String arrangeCode, String arrangeName);
  List<PaymentArrange> findAllByArrangeCode(String arrangeCode);
  List<PaymentArrange> findByArrangeCodeDefaultArrange(String arrangeCode);
  PaymentArrange findOneByArrangeId(Long arrangeId);
  List<PaymentArrange> findAllByArrangeDefaultStatus(Long id, boolean status);
}
