package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.PaymentMethod;

import java.util.List;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Long> {
    Long countAllByValueCode(String valueCode);
    List<PaymentMethod> findAllByValueCode(String valueCode);
    PaymentMethod findOneByValueCode(String valueCode);
}
