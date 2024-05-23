package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.PayMethodConfig;

import java.util.List;

public interface PayMethodConfigRepository extends CrudRepository<PayMethodConfig, Long> {
    List<PayMethodConfig> findAllOrderByCountryAscPayMethodAsc();
}
