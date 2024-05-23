package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.PaymentCenter;
import th.com.bloomcode.paymentservice.model.idem.SpecialGL;

import java.util.List;

public interface PaymentCenterRepository extends CrudRepository<PaymentCenter, Long> {
    Long countAllByValueCode(String valueCode);
    List<PaymentCenter> findAllByValueCode(String valueCode);
    PaymentCenter findOneByValueCode(String valueCode);
}
