package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.HouseBankPaymentMethod;

import java.util.List;

public interface HouseBankPaymentMethodRepository extends CrudRepository<HouseBankPaymentMethod, Long> {
    Long countAllByValueCode(String valueCode);
    List<HouseBankPaymentMethod> findAllByValueCode(String valueCode);
    HouseBankPaymentMethod findOneByValueCode(String client, String houseBankKey, String paymentMethod);
}
